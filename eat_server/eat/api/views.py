import uuid
from rest_framework import status , permissions , generics , viewsets
from rest_framework.renderers import JSONRenderer
from rest_framework.decorators import api_view , authentication_classes , permission_classes
from rest_framework.response import Response

from django.contrib.auth.hashers import make_password
from django.contrib.auth import authenticate
from copy import copy
from .serializers import *
from .exceptions import *
from .authentication import TokenAuthentication
# Create your views here.

@api_view(['POST'])
@permission_classes([permissions.AllowAny])
def register(request):
	data=copy(request.DATA)
	data['password'] = make_password(data['password'])

	if (request.FILES.get('image')):
		data['image'] = request.FILES['image']

	serializer = UserSerializer(data = data , context={'request': request})
	if serializer.is_valid() :
		serializer.save()
		response = serializer.data
		if request.DATA.get('tags') :
			User.objects.get(pk=response['id'])
		response['token'] = str(fetch_auth_token_for_user(serializer.data['id']))
		return Response(response, status=status.HTTP_201_CREATED)
	return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

def fetch_auth_token_for_user(user_id):
	try :
		authToken = AuthToken.objects.get(user_id = user_id)
		print authToken.token
		return authToken.token
	except :
		authToken = AuthToken(token = uuid.uuid4() , user_id = user_id)
		authToken.save()
		return authToken.token

@api_view(['POST'])
@permission_classes([permissions.AllowAny])
def user_login(request):
	data = request.DATA

	username = data.get('username', '')
	password = data.get('password', '')
	user = authenticate(username=username, password=password)
	if user:
		if user.is_active:
			user.token = fetch_auth_token_for_user(user.id)
			serializer = UserSerializer(user , context={'request': request})
			serialized_data = serializer.data
			serialized_data['auth_token'] = str(fetch_auth_token_for_user(user.id))
			return Response(serialized_data , status=status.HTTP_201_CREATED)
		else :
			raise InvlalidLoginCredentialsException()
	else :
		raise InvlalidLoginCredentialsException()

@api_view(['GET'])
@permission_classes([permissions.IsAuthenticated])
@authentication_classes([TokenAuthentication])
def fetch_items(request):
	items = Item.objects.all()
	filters = request.GET
	if filters.get('eggs') :
		items = items.filter(eggs = True)
	if filters.get('gluten') :
		items = items.filter(gluten = True)
	if filters.get('dairy') :
		items = items.get('dairy')
	if filters.get('nuts') :
		items = items.get('nuts')
	if filters.get('q') :
		items = items.filter(name__istartswith=filters.get('q'))
	
	serializer = ItemSerializer(items , many=True , context={'request' : request})
	return Response(serializer.data)

@api_view(['GET'])
@permission_classes([permissions.IsAuthenticated])
@authentication_classes([TokenAuthentication])
def fetch_history_params(request):
	cart_ids = Cart.objects.filter(user_id = request.user.id).values_list('id' , flat=True)
	items_carted = CartItem.objects.filter(cart_id__in =cart_ids)
	total_items_carted_by_user = len(items_carted)
	
	total_carts_created = len(cart_ids)
	
	total_money_saved = 0
	for cart_item in items_carted :
		total_money_saved += int(cart_item.item.price)
	
	return Response({
		"total_items_carted_by_user" : total_items_carted_by_user ,
		"total_money_saved" : total_money_saved ,
		"total_carts_created" : total_carts_created
	})

@api_view(['POST'])
@permission_classes([permissions.IsAuthenticated])
@authentication_classes([TokenAuthentication])
def mark_item_carted(request , pk):
	carts = Cart.objects.filter(user_id=request.user.id).order_by('-created')
	if not len(carts) :
		cart = Cart(user_id = request.user.id)
		cart.save()
	else :
		cart = carts[0]
	cart_item = CartItem(item_id=pk , cart_id = cart.id)
	cart_item.save()
	return Response()

def mark_item_uncarted(request , pk):
	carts = Cart.objects.filter(user_id=request.user.id).order_by('-created')
	if not len(carts) :
		raise ApiInvalidArgumentException('No cart yet.')
	CartItem.objects.filter(item_id=pk , cart_id = carts[0].id).delete()
	return Response()

import uuid
from rest_framework import status , permissions , generics , viewsets
from rest_framework.renderers import JSONRenderer
from rest_framework.decorators import api_view , authentication_classes , permission_classes
from rest_framework.response import Response

from django.contrib.auth.hashers import make_password
from django.contrib.auth import authenticate
from django.conf import settings

from copy import copy
from .serializers import *
from .exceptions import *
from .authentication import TokenAuthentication
from .utils import lsqnonneg

from numpy import array , identity , concatenate , zeros , ones , empty

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
	if filters.get('carted') :
		cart = Cart.objects.filter(user_id = request.user.id).order_by('-created')
		
		item_ids = CartItem.objects.filter(cart_id=cart[0].id).values_list('item_id' , flat = True)
		items = items.filter(id__in=item_ids)

	serializer = ItemSerializer(items , many=True , context={'request' : request})
	return Response(serializer.data)

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

@api_view(['POST'])
@permission_classes([permissions.IsAuthenticated])
@authentication_classes([TokenAuthentication])
def mark_item_uncarted(request , pk):
	carts = Cart.objects.filter(user_id=request.user.id).order_by('-created')
	if not len(carts) :
		raise ApiInvalidArgumentException('No cart yet.')
	CartItem.objects.filter(item_id=pk , cart_id = carts[0].id).delete()
	
	return Response()

@api_view(['GET'])
@permission_classes([permissions.IsAuthenticated])
@authentication_classes([TokenAuthentication])
def fetch_recommendations(request):
	import pprint
	items = Item.objects.all().order_by('id')
	# Create A 2*2 array
	# row 1 :- carbs
	# row 2 :- protein
	# row 3 :- vitamins
	# row 4 :- fats
	print items
	A = [[0 for x in range(items.count())] for x in range(5)]
	
	row_index = 0
	for row in A :
		column_index=0
		for item in items :
			if row_index == 0 :
				A[row_index][column_index] = float(item.carbs)
			elif row_index == 1 :
				A[row_index][column_index] = float(item.protein)
			elif row_index==2 :
				A[row_index][column_index] = float(item.vitamins)
			elif row_index == 3:
				A[row_index][column_index] = float(item.fats)
			elif row_index == 4:
				A[row_index][column_index] = float(item.price)
			column_index +=1
		row_index+=1
		
	# Define a plan.
	# row 1 :- required carbs for the week
	# row 2 :- required protein for the week
	# row 3 :- required vits for the week
	# row 4 :- required fats for the week
	
	d = [0 for x in range(5)]
	
	if (request.user.id == 1) :
		plan = settings.WEIGHT_LOSS_PLAN
	else :
		plan = settings.WEIGHT_GAIN_PLAN
	
	d[0] = plan['carbs']
	d[1] = plan['protein']
	d[2] = plan['fats']
	d[3] = plan['vitamins']
	d[4] = 0 # cost
	
	z = empty(items.count())
	z.fill(100)
	d = concatenate((d,z) , axis = 0)
	
	A = array(A)
	identity_array = identity(items.count())*100
	# for row in identity_array :
	# 	A.app
	# print A
	# print identity_array
	A = concatenate((A , identity_array) , axis=0)
	
	print "\n Input Matrix : \n"
	print A
	
	print "\n Required Output : \n "
	print d

	# d = array(d)
	[x, resnorm, residual] = lsqnonneg(A,d)
	#
	print "\n Solution Vector \n"
	print x
	#
	index = 0
	recommended_item_ids = []
	for item in items :
		if x[index] >= 0.5 :
			recommended_item_ids.append(item.id)
		index+=1
	#
	recommended_items = Item.objects.filter(id__in=recommended_item_ids)
	serializer = ItemSerializer(recommended_items , many=True , context={'request' : request})
	return Response(serializer.data)
	return Response()

@api_view(['GET'])
@permission_classes([permissions.IsAuthenticated])
@authentication_classes([TokenAuthentication])
def fetch_cart_progress(request):
	if (request.user.id == 1) :
		plan = settings.WEIGHT_LOSS_PLAN
	else :
		plan = settings.WEIGHT_GAIN_PLAN
	
	cart = Cart.objects.filter(user_id = request.user.id).order_by('-created')
	cart_items = CartItem.objects.filter(cart_id=cart[0].id)
	
	completed = {
		'carbs' : 0.0 ,
		'protein' : 0.0 ,
		'fats' : 0.0 ,
		'vitamins' :0.0
	}
	
	for cart_item in cart_items :
		completed['carbs'] += ((float(cart_item.item.carbs)/plan['carbs'])*100)
		completed['protein'] += (float(cart_item.item.protein)/plan['protein']*100)
		completed['fats'] += (float(cart_item.item.fats)/plan['fats']*100)
		completed['vitamins'] += (float(cart_item.item.vitamins)/plan['vitamins']*100)
	
	return Response(completed)

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
		total_money_saved += float(cart_item.item.price)
	if (request.user.id == 1) :
		plan = settings.WEIGHT_LOSS_PLAN
	else :
		plan = settings.WEIGHT_GAIN_PLAN
	total_money_saved = round(total_money_saved , 2)
	
	# Fetch average nutrient intake per cart
	carts = Cart.objects.filter(user_id = request.user.id).values_list('id' , flat=True)
	cart = carts[0]
	
	completed = {
		'carbs' : 0.0 ,
		'protein' : 0.0 ,
		'fats' : 0.0 ,
		'vitamins' :0.0
	}
	completed_in_grams = {
		'carbs' : 0.0 ,
		'protein' : 0.0 ,
		'fats' : 0.0 ,
		'vitamins' :0.0
	}
	
	for cart_item in items_carted :
		completed['carbs'] += round((float(cart_item.item.carbs)/plan['carbs'])*100 , 2)
		completed['protein'] += round(float(cart_item.item.protein)/plan['protein']*100 , 2)
		completed['fats'] += round(float(cart_item.item.fats)/plan['fats']*100 , 2)
		completed['vitamins'] += round(float(cart_item.item.vitamins)/plan['vitamins']*100 ,2)
	
	for cart_item in items_carted :
		completed_in_grams['carbs'] += float(cart_item.item.carbs)
		completed_in_grams['protein'] += float(cart_item.item.protein)
		completed_in_grams['fats'] += float(cart_item.item.fats)
		completed_in_grams['vitamins'] += float(cart_item.item.vitamins)
	
	average_cart_completed = round((completed['carbs'] + completed['protein'] + completed['fats'] + completed['vitamins'])/4 ,2)
	return Response({
		"total_items_carted_by_user" : total_items_carted_by_user ,
		"total_money_saved" : total_money_saved ,
		"total_carts_created" : total_carts_created ,
		"average_cart_completed" : int(average_cart_completed),
		"nurtients_in_cart" : completed ,
		"nutrients_in_gram" : completed_in_grams
	})

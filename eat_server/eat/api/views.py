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
@permission_classes([permissions.AllowAny])
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
	
	serializer = ItemSerializer(items , many=True)
	
	return Response(serializer.data)

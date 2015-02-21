import uuid
from rest_framework import status , permissions , generics , viewsets
from rest_framework.renderers import JSONRenderer
from rest_framework.decorators import api_view , authentication_classes , permission_classes
from rest_framework.response import Response

from django.contrib.auth.hashers import make_password
from copy import copy

from .serializers import *
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
			# No need to log the user in and start a session
			# login(request, user)
			# user.token = self.fetch_auth_token_for_user(user)
			serializer = UserSerializer(user , context={'request': request})
			serialized_data = serializer.data
			serialized_data['auth_token'] = str(fetch_auth_token_for_user(user.id))
			
			return Response(serializer.data , status=status.HTTP_201_CREATED)
		else :
			raise InvlalidLoginCredentialsException()

	else :
		raise InvlalidLoginCredentialsException()
	
def fetch_items_view():
	pass
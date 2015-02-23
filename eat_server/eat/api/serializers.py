from rest_framework import serializers
from core.models import *


class UserSerializer(serializers.ModelSerializer):
	class Meta:
		model = User
		fields = ('id' , 'username' , 'email')

class ItemSerializer(serializers.ModelSerializer):
	class Meta:
		model = Item
		fields = ('id' , 'name' , 'store' , 'created' , 'price')

from rest_framework import serializers
from core.models import *


class UserSerializer(serializers.ModelSerializer):
	class Meta:
		model = User
		fields = ('id' , 'username' , 'email' , 'password')

class ItemSerializer(serializers.ModelSerializer):
	check_carted_by_user = serializers.SerializerMethodField()
	
	class Meta:
		model = Item
		fields = ('id' , 'name' , 'store' , 'created' , 'price' , 'check_carted_by_user' , 'image_url')
		
	def to_native(self , obj):
		serialized_obj = super(ItemSerializer , self).to_native(obj)
		serialized_obj['check_carted_by_user'] = obj.check_carted_by_user
		return serialized_obj
	
	
	def get_check_carted_by_user(self , obj) :
		request = self.context['request']
		return obj.check_carted(request.user)
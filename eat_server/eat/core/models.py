import datetime , time
from hashlib import md5
# from core.managers import TrucksManager

from django.db import models
from django.contrib.auth.models import AbstractUser , UserManager

from django.utils.translation import gettext as _
from django.core.exceptions import ValidationError

# from django_extensions.db.fields.encrypted import EncryptedCharField
# from taggit.managers import TaggableManager
from model_utils import Choices

from django_extensions.db.models import TimeStampedModel
from django.conf import settings

class User(AbstractUser):
	TYPE = Choices(("STORE" , 'SELLER') , ("USER" , 'USER'))
	type  = models.CharField('type' ,
							max_length = 255 ,
							choices = TYPE ,
							default = TYPE.USER ,
							db_index = True
							)
	modifed = models.DateTimeField(_('Modified') ,
									help_text="Last Modified Date" ,
									auto_now = True
									)

class Store(TimeStampedModel):
	name = models.CharField('name' ,
							max_length = 255 ,
							db_index = True ,
							null = False
							)
	lat = models.FloatField(_('Latitude') ,
										blank = False ,
										default = 20.0
				)
	lon = models.FloatField(_('Longitude') ,
								blank = False ,
								default = 20.0
								)
	user = models.ForeignKey('User')
	
	def __unicode__(self) :
		return self.name
		
class Item(TimeStampedModel):
	TYPE = Choices('VEGETABLES' , 'FRUITS' , 'DAIRY' , 'PROTEIN' , 'GRAINS')
	name = models.CharField('name' ,
							max_length = 255 ,
							db_index = True ,
							null = False
							)
	item_type = models.CharField('Type' ,
								max_length = 255 ,
								choices = TYPE ,
								default = TYPE.VEGETABLES
								)
	store = models.ForeignKey('Store')
	price = models.CharField(_('Price') ,
								max_length = 255 ,
								null = False ,
								blank = False
								)
	price_per_saving = models.CharField('Price per serving',
										max_length = 255 ,
										null = True
										)
	# calories = models.CharField('')
	CATEGORIES = (
				'VEGETABLE', 
				'FRUIT',
				'MEAT'
				)
	
	vegetarian = models.BooleanField()
	nuts = models.BooleanField()
	dairy = models.BooleanField()
	gluten = models.BooleanField()
	eggs = models.BooleanField()
	
	def price_to_float(self):
		return self
	def __unicode__(self) :
		return self.name
	
	def check_carted_by_user(self , user=False) :
		return CartItem.objects.filter(item_id=self.id).count()

class AuthToken(models.Model):
	user = models.ForeignKey(User)
	token = models.CharField("token" ,
								max_length = 255 ,
								blank = False
							)
	created = models.DateTimeField("created" ,
										default=datetime.datetime.now
									)
	modified = models.DateTimeField("modified" ,
										auto_now = True)

class Cart(TimeStampedModel):
	TYPE = Choices("WEIGHT_LOSS" , "WEIGHT_GAIN" , "HEALTHY" , "VEGAN")
	user = models.ForeignKey(User)
	cart_type = models.CharField('Type' ,
							max_length = 255 ,
							blank = False ,
							default = "WEIGHT_LOSS"
							)
	def __unicode__(self):
		return self.user.username + " - " + self.cart_type

class CartItem(TimeStampedModel):
	item = models.ForeignKey(Item)
	cart = models.ForeignKey(Cart)

	# def __unicode__(self):
	# 	return "Cart :-" + self.cart.id + " | Item :-" + self.item.name
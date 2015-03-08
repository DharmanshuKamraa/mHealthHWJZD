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

def food_item_upload_to(instance, filename):
	return 'food_item/{}.{}'.format(instance.name, filename.split('.')[-1])

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
	image = models.ImageField(_('Cover Image'),
									upload_to=food_item_upload_to,
									blank = True ,
									null = False
									)
	
	def image_url(self):
		return 'http://ec2-54-191-192-216.us-west-2.compute.amazonaws.com:8001/media/' + str(self.image)
	
	vegetarian = models.BooleanField()
	nuts = models.BooleanField()
	dairy = models.BooleanField()
	gluten = models.BooleanField()
	eggs = models.BooleanField()
	
	carbs = models.CharField('Carbs' ,
								max_length = 255 ,
								null = True ,
								default = '27'
							)
							
	protein = models.CharField('Protein' ,
								max_length = 255 ,
								null = True ,
								default = '13'
								)
	fats = models.CharField('Fat' ,
							max_length = 255 ,
							null = True, 
							default = '2'
							)
	
	vitamins = models.CharField('Vitamins' ,
								max_length = 255 ,
								null = True ,
								default = '10'
								)
	
	def price_to_float(self):
		return self
	def __unicode__(self) :
		return self.name
	
	def check_carted(self , user=False) :
		if not user :
			return CartItem.objects.filter(item_id=self.id).count()
		else :
			cart_id = Cart.objects.filter(user_id = user.id).order_by('-created')
			return CartItem.objects.filter(item_id=self.id , cart_id=cart_id[0].id).count()

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
from django.contrib import admin
from .models import *

class MyUserAdmin(admin.ModelAdmin):
	list_display = ('__unicode__', 'id' , 'email' , 'type')

class StoreAdmin(admin.ModelAdmin):
	list_display = ('__unicode__' , 'id' , 'name')

class ItemAdmin(admin.ModelAdmin):
	list_display = ('__unicode__' , 'id' , 'name' , 'store')

# Register your models here.
admin.site.register(User , MyUserAdmin)
admin.site.register(Store , StoreAdmin)
admin.site.register(Item , ItemAdmin)
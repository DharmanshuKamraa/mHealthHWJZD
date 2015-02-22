from django.conf.urls import patterns, include, url
from rest_framework import routers
import views
from rest_framework.routers import DefaultRouter

urlpatterns = patterns('',
				url(r'^users/register/$' , views.register) ,
				)
router = routers.DefaultRouter()
urlpatterns += router.urls
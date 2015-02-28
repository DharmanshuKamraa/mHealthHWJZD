from django.conf.urls import patterns, include, url
from rest_framework import routers
import views
from rest_framework.routers import DefaultRouter

urlpatterns = patterns('',
				url(r'^users/register/$' , views.register) ,
				url(r'^users/login/$' , views.user_login) ,
				url(r'^items' , views.fetch_items) ,
				url(r'^fetch_history_params' , views.fetch_history_params)
				)
router = routers.DefaultRouter()
urlpatterns += router.urls
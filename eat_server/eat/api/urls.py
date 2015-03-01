from django.conf.urls import patterns, include, url
from rest_framework import routers
import views
from rest_framework.routers import DefaultRouter

urlpatterns = patterns('',
				url(r'^users/register/$' , views.register) ,
				url(r'^users/login/$' , views.user_login) ,
				url(r'^items' , views.fetch_items) ,
				url(r'^fetch_history_params' , views.fetch_history_params),
				url(r'^mark_item_carted/(?P<pk>[0-9]+)$' , views.mark_item_carted) ,
				url(r'^mark_item_uncarted/(?P<pk>[0-9]+)$' , views.mark_item_uncarted) ,
				)
router = routers.DefaultRouter()
urlpatterns += router.urls
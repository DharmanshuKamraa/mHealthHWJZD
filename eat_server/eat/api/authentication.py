from core.models import AuthToken
from rest_framework import authentication
from rest_framework import exceptions

class TokenAuthentication(authentication.BaseAuthentication):
	def authenticate(self, request):
		print request.META
		if 'HTTP_AUTH_TOKEN' in request.META :
			print "AUTH_TOKEN:-" +request.META.get('HTTP_AUTH_TOKEN')
			try :
				auth_token = AuthToken.objects.get(token=request.META['HTTP_AUTH_TOKEN'])
				if auth_token.user :
					# Set user
					return (auth_token.user , None)

			except AuthToken.DoesNotExist:
				raise exceptions.AuthenticationFailed('Invalid auth token.')

			return True
		else :
			raise exceptions.AuthenticationFailed('Provide a valid auth token.')

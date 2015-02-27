from rest_framework.exceptions import APIException

class InvlalidLoginCredentialsException(APIException):
    status_code = 401
    detail = 'Invalid login credentials.'
    def __init__(self, detail=None):
        self.detail = detail or self.detail

class ApiInvalidArgumentException(APIException):
    status_code = 400
    detail = 'Incorrect Arguments provided'

    def __init__(self, detail=None):
        self.detail = detail or self.detail

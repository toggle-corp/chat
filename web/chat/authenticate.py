from django.http import HttpResponse
from django.contrib.auth import authenticate

import base64


def basic_authenticate(request):
    if 'HTTP_AUTHORIZATION' in request.META:
        auth = request.META['HTTP_AUTHORIZATION'].split()
        if len(auth) == 2:
            if auth[0].lower() == "basic":
                auth = base64.b64decode(auth[1]).decode('utf-8')
                username, password = auth.split(':', 1)
                user = authenticate(username=username, password=password)
                if user:
                    return True
    return False


authentication_error = HttpResponse("Unauthorized", status=401)



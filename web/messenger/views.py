from django.shortcuts import render
from django.views.generic import View
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

import json

from chat.authenticate import *


# @method_decorator(csrf_exempt, name='dispatch')
# class UserVerification(View):
#     def post(self, request):
#         if not basic_authenticate(request):
#             return authentication_error
#         inData = json.loads(request.body.decode('utf-8'))
#         outData = inData
#         return JsonResponse(inData)

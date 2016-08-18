from django.shortcuts import render
from django.views.generic import View
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
from django.contrib.auth.models import User

import json

from users.models import *
from chat.authenticate import *


def get_user_data(user):
    return {
        "pk": user.pk,
        "username": user.username,
        "full_name": user.get_full_name(),
    }


@method_decorator(csrf_exempt, name='dispatch')
class UserVerification(View):
    def post(self, request):
        if not basic_authenticate(request):
            return authentication_error
        in_data = json.loads(request.body.decode('utf-8'))
        out_data = in_data
        return JsonResponse(out_data)


@method_decorator(csrf_exempt, name='dispatch')
class UserApiView(View):
    def post(self, request, id=None):
        if not basic_authenticate(request):
            return authentication_error

        if id:
            user = User.objects.get(pk=id)
            return JsonResponse(get_user_data(user))

        users = User.objects.all()
        user_list = []
        for user in users:
            user_list.append(get_user_data(user))

        return JsonResponse({"users": user_list})


@method_decorator(csrf_exempt, name='dispatch')
class FcmApiView(View):
    def post(self, request):
        if not basic_authenticate(request):
            return authentication_error

        in_data = json.loads(request.body.decode('utf-8'))
        device_id = in_data["device_id"]
        token = in_data["token"]
        user = request.user

        try:
            ft = FcmToken.objects.get(device_id=device_id, user=user)
        except:
            ft = FcmToken()
            ft.user = user
            ft.device_id = device_id

        ft.token = token
        ft.save()

        return JsonResponse({"success": True})

from django.shortcuts import render, redirect
from django.views.generic import View
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout

import json

from users.models import *
from chat.authenticate import *


class SigninView(View):
    def get(self, request):
        if request.user.is_authenticated():
            return redirect('messenger')
        return render(request, "users/signin.html")

    def post(self, request):
        username = request.POST.get("username")
        password = request.POST.get("password")

        if User.objects.filter(username=username).count() == 0:
            return render(request, "users/signin.html", {
                "error": "User with this username doesn't exist."
            })


        user = authenticate(username=username, password=password)
        if not user:
            return render(request, "users/signin.html", {
                "error": "Invalid password."
            })

        login(request, user)
        return redirect('index')


class SignoutView(View):
    def get(self, request):
        logout(request)
        return redirect('index')


# API Views

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

from django.shortcuts import render
from django.views.generic import View
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

import json

from chat.authenticate import *
from messenger.models import *


def get_conversation_data(conversation):
    return {
        "pk": conversation.pk,
        "title": conversation.title,
        "users": [u.pk for u in conversation.users.all()],
    }


@method_decorator(csrf_exempt, name='dispatch')
class ConversationApiView(View):
    def get(self, request, id=None):
        if not basic_authenticate(request):
            return authentication_error

        if id:
            conversation = Conversation.objects.get(pk=id,
                users__id=request.user.pk)
            return JsonResponse(get_conversation_data(conversation))

        conversations = Conversation.objects.filter(
            users__id=request.user.pk)
        clist = []
        for conversation in conversations:
            clist.append(get_conversation_data(conversation))

        return JsonResponse({"conversations": clist})


@method_decorator(csrf_exempt, name='dispatch')
class ConversationAddApiView(View):
    def get(self, request, id=None):
        if not basic_authenticate(request):
            return authentication_error
        return JsonResponse({"success": "false"})

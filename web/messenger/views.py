from django.shortcuts import render
from django.views.generic import View
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

import json
import time
from datetime import datetime

from chat.authenticate import *
from messenger.models import *
from messenger.cloud_messaging import *


def get_conversation_data(conversation):
    return {
        "pk": conversation.pk,
        "title": conversation.title,
        "users": [u.pk for u in conversation.users.all()],
    }


@method_decorator(csrf_exempt, name='dispatch')
class ConversationApiView(View):
    def post(self, request, id=None):
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
    def post(self, request, id=None):
        if not basic_authenticate(request):
            return authentication_error

        if id:
            conversation = Conversation.objects.get(pk=id,
                users__id=request.user.pk)
        else:
            conversation = Conversation()
            conversation.save()
            conversation.users.add(request.user)

        in_data = json.loads(request.body.decode('utf-8'))

        if "users" in in_data:
            users = in_data["users"]
            for user in users:
                conversation.users.add(
                    User.objects.get(pk=user)
                )

        if "title" in in_data:
            conversation.title = in_data["title"]
            conversation.save()

        return JsonResponse(get_conversation_data(conversation))


@method_decorator(csrf_exempt, name='dispatch')
class ConversationDeleteApiView(View):
    def post(self, request, id):
        if not basic_authenticate(request):
            return authentication_error

        conversation = Conversation.objects.get(pk=id,
            users__id=request.user.pk)

        in_data = json.loads(request.body.decode('utf-8'))
        if "users" in in_data:
            users = in_data["users"]
            for user in users:
                conversation.users.remove(
                    User.objects.get(pk=user)
                )
            if conversation.users.count() == 0:
                conversation.delete()
        else:
            conversation.delete()
        return JsonResponse({"success": "true"})


@method_decorator(csrf_exempt, name='dispatch')
class ConversationEditApiView(View):
    def post(self, request, id):
        if not basic_authenticate(request):
            return authentication_error

        conversation = Conversation.objects.get(pk=id,
            users__id=request.user.pk)

        in_data = json.loads(request.body.decode('utf-8'))

        if "users" in in_data:
            users = in_data["users"]
            for user in users:
                conversation.users.add(
                    User.objects.get(pk=user)
                )

        if "title" in in_data:
            conversation.title = in_data["title"]
            conversation.save()

        return JsonResponse(get_conversation_data(conversation))


def get_message_data(message):
    return {
        'pk': message.pk,
        'posted_by': message.posted_by.pk,
        'posted_at': int(time.mktime(message.posted_at.timetuple())*1000),
        'message': message.message,
        'conversation_id': message.conversation.pk
    }


@method_decorator(csrf_exempt, name='dispatch')
class MessageApiView(View):
    def post(self, request, conversation_id):
        if not basic_authenticate(request):
            return authentication_error

        conversation = Conversation.objects.get(pk=conversation_id,
            users__id=request.user.pk)

        messages = Message.objects.filter(conversation=conversation)

        in_data = json.loads(request.body.decode('utf-8'))

        if "start_time" in in_data:
            st = datetime.utcfromtimestamp(in_data["start_time"])
            messages = messages.filter(posted_at__gte=st)
        if "end_time" in in_data:
            et = datetime.utcfromtimestamp(in_data["end_time"])
            messages = messages.filter(posted_at__lte=et)
        if "count" in in_data:
            messages = messages[:in_data["count"]]

        mlist = []
        for message in messages:
            mlist.append(get_message_data(message))

        return JsonResponse({"messages": mlist})


@method_decorator(csrf_exempt, name='dispatch')
class MessageAddApiView(View):
    def post(self, request, conversation_id):
        if not basic_authenticate(request):
            return authentication_error

        conversation = Conversation.objects.get(pk=conversation_id,
            users__id=request.user.pk)

        in_data = json.loads(request.body.decode('utf-8'))

        message = Message()
        message.conversation = conversation

        if "posted_at" in in_data:
            pt = datetime.utcfromtimestamp(in_data["posted_at"])
            message.posted_at = pt

        else:
            message.posted_at = datetime.now()

        message.posted_by = request.user
        message.message = in_data["message"]
        message.save()

        message_data = get_message_data(message);
        message_data.update({"type": "new_message"})
        send(message.conversation.users.all(), message_data);

        return JsonResponse(message_data)

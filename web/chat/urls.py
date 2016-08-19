"""chat URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.10/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin

from chat.views import *
from users.views import *
from messenger.views import *


urlpatterns = [
    url(r'^admin/', admin.site.urls),

    url(r'^$', IndexView.as_view(), name="index"),
    url(r'^signin/$', SigninView.as_view(), name="signin"),
    url(r'^signout/$', SignoutView.as_view(), name="signout"),
    url(r'^messenger/$', MessengerView.as_view(), name="messenger"),

    # API
    url(r'^api/v1/user/verify/$', UserVerification.as_view()),
    url(r'^api/v1/user/get/(?P<id>\d+)/$', UserApiView.as_view()),
    url(r'^api/v1/user/get/$', UserApiView.as_view()),

    url(r'^api/v1/fcm/register/$', FcmApiView.as_view()),

    url(r'^api/v1/conversation/add/(?P<id>\d+)/$', ConversationAddApiView.as_view()),
    url(r'^api/v1/conversation/add/$', ConversationAddApiView.as_view()),
    url(r'^api/v1/conversation/get/(?P<id>\d+)/$', ConversationApiView.as_view()),
    url(r'^api/v1/conversation/get/$', ConversationApiView.as_view()),
    url(r'^api/v1/conversation/delete/(?P<id>\d+)/$', ConversationDeleteApiView.as_view()),
    url(r'^api/v1/conversation/edit/(?P<id>\d+)/$', ConversationEditApiView.as_view()),

    url(r'^api/v1/message/get/(?P<conversation_id>\d+)/$', MessageApiView.as_view()),
    url(r'^api/v1/message/add/(?P<conversation_id>\d+)/$', MessageAddApiView.as_view()),
]

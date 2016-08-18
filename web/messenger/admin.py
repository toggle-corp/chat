from django.contrib import admin

from messenger.models import *


class MessageInline(admin.StackedInline):
    model = Message


class ConversationAdmin(admin.ModelAdmin):
    inlines = [MessageInline, ]



admin.site.register(Conversation, ConversationAdmin)

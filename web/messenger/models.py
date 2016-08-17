from django.db import models
from django.contrib.auth.models import User


class Conversation(models.Model):
    title = models.CharField(max_length=200, blank=True,
                             default=None, null=True)
    users = models.ManyToManyField(User)

    def __str__(self):
        if self.title:
            return self.title
        return ",".join([u.get_full_name() for u in users])


class Message(models.Model):
    conversation = models.ForeignKey(Conversation)
    posted_by = models.ForeignKey(User)
    posted_at = models.DateTimeField(auto_now_add=True)
    message = models.TextField()

    def __str__(self):
        return self.message

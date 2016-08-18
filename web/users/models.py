from django.db import models
from django.contrib.auth.models import User


class FcmToken(models.Model):
    user = models.ForeignKey(User)
    device_id = models.TextField()
    token = models.TextField()

from users.models import *
from messenger.models import *

import requests
import json


def send(users, data):
    tokens = []
    for u in users:
        ts = FcmToken.objects.filter(user=u)
        for t in ts:
            tokens.append(t.token)

    # TODO: Apparently tokens can be 1000 at max.

    url = "https://fcm.googleapis.com/fcm/send"

    message = {
        "data": data,
        "registration_ids": tokens
    }

    key = "key=AIzaSyD7e2QASMUE1Wnc1rzkxJR5dUtWduMCvnk"
    headers = {
        'Content-type': 'application/json',
        'Authorization': key
    }
    r = requests.post(url, data=json.dumps(message), headers=headers)

    response = json.loads(r.text)
    print(r.text)

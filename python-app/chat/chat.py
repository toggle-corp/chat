import requests
import json
from datetime import datetime


class Chat:
    def __init__(self, url, username, password):
        self.base_url = url
        self.username = username
        self.password = password

        self.users = {}
        self.conversations = {}
        self.messages = {}

        self.get_users()
        self.get_conversations()

    def get_users(self):
        url = self.base_url + "api/v1/user/get/"
        try:
            response = requests.post(
                url,
                auth=requests.auth.HTTPBasicAuth(self.username, self.password)
            )
            if response.status_code == 200:
                users = response.json()["users"]
                for user in users:
                    self.users[user["pk"]] = user
        except:
            print("Couldn't connect")

    def get_conversations(self):
        url = self.base_url + "api/v1/conversation/get/"
        try:
            response = requests.post(
                url,
                auth=requests.auth.HTTPBasicAuth(self.username, self.password)
            )
            if response.status_code == 200:
                conversations = response.json()["conversations"]
                for conversation in conversations:
                    self.conversations[conversation["pk"]] = conversation
                    self.messages[conversation["pk"]] = []
        except:
            print("Couldn't connect")

    def get_messages(self, conversation, start_time=None, end_time=None, count=None):
        url = self.base_url + "api/v1/message/get/" + str(conversation["pk"]) + "/"

        data = {}
        if start_time:
            data["start_time"] = start_time
        if end_time:
            data["end_time"] = end_time
        if count:
            data["count"] = count

        try:
            response = requests.post(
                url,
                json=data,
                auth=requests.auth.HTTPBasicAuth(self.username, self.password)
            )
            if response.status_code == 200:
                messages = response.json()["messages"]
                for message in reversed(messages):
                    self.add_message(conversation, message)
        except:
            print("Couldn't connect")


    def add_message(self, conversation, message):
        messages = self.messages[conversation["pk"]]
        for m in messages:
            if m["pk"] == message["pk"]:
                m.update(message)
                return
        messages.append(message)

chat = Chat("http://localhost:8000/", "bibek", "danphe17")

now = int(datetime.now().strftime("%s")) * 1000 
chat.get_messages(chat.conversations[1],
                  end_time=now,
                  count=10)
print(chat.messages)

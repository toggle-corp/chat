from django.shortcuts import render, redirect
from django.views.generic import View, TemplateView


class IndexView(View):
    def get(self, request):
        if request.user.is_authenticated():
            return redirect('messenger')
        return redirect('signin')

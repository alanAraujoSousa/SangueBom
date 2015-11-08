from rest_framework import viewsets

from engine.serializers import UserProfileSerializer
from engine.models import UserProfile
from rest_framework.decorators import detail_route
from rest_framework.response import Response


class UserViewSet(viewsets.ModelViewSet):
    queryset = UserProfile.objects.all()
    serializer_class = UserProfileSerializer

    @detail_route
    def blood_type(self, request, pk=None):
        users = UserProfile.objects.all().filter(blood_type=pk)
        serializer = self.get_serializer(users, many=True)
        return Response(serializer.data())
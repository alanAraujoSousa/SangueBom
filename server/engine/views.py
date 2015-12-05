from django.contrib.auth.models import User
from rest_framework import mixins, status
from rest_framework.decorators import detail_route
from rest_framework.response import Response
from rest_framework.viewsets import GenericViewSet, ModelViewSet

from engine.models import UserProfile, Donation, Patient
from engine.serializers import UserSerializer, PatientSerializer


class UserViewSet(mixins.RetrieveModelMixin,
                  mixins.ListModelMixin,
                  GenericViewSet):
    
    queryset = User.objects.all()
    serializer_class = UserSerializer
    
    @detail_route
    def last_donation(self, request, pk=None):
        if pk == None :
            pk = request.user.username
        latestDonation = Donation.objects.filter(userProfile__user__username=pk).latest('donation_date')
        return latestDonation

    @detail_route
    def blood_type(self, request, pk=None):
        users = UserProfile.objects.filter(blood_type=pk)
        serializer = self.get_serializer(users, many=True)
        return Response(serializer.data())
    
    
    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return Response(status=status.HTTP_201_CREATED)
    
class PatientViewSet(ModelViewSet):
    
    queryset = Patient.objects.all()
    serializer_class = PatientSerializer

        
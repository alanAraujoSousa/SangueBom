from django.contrib.auth.models import User
from rest_framework import mixins, status
from rest_framework.decorators import detail_route
from rest_framework.response import Response
from rest_framework.viewsets import GenericViewSet, ModelViewSet

from engine.models import UserProfile, Donation, Patient
from engine.serializers import UserSerializer, PatientSerializer,\
    DonationSerializer


class UserViewSet(mixins.RetrieveModelMixin,
                  mixins.ListModelMixin,
                  GenericViewSet):
    
    queryset = User.objects.all()
    serializer_class = UserSerializer
    
    @detail_route(methods=['get'],
                  url_path="last_donation")
    def last_donation(self, request, pk=None):
        if pk == None :
            pk = request.user.username
        latestDonation = Donation.objects.filter(userProfile__user__username=pk)
        if latestDonation.count() != 0:
            latestDonation = latestDonation.latest('donation_date')
        serializer = DonationSerializer(latestDonation)
        return Response(data=serializer.data)

    @detail_route(methods=['get'],
                  url_path="blood_type")
    def blood_type(self, request, pk=None):
        users = UserProfile.objects.filter(blood_type=pk)
        serializer = self.get_serializer(users, many=True)
        return Response(data=serializer)
    
    
    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return Response(status=status.HTTP_201_CREATED)
    
class PatientViewSet(ModelViewSet):
    
    queryset = Patient.objects.all()
    serializer_class = PatientSerializer

        
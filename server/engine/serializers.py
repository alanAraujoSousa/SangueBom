'''
Created on 30 de out de 2015

@author: alan
'''

from django.contrib.auth.models import User, Group
from rest_framework import serializers

from engine.models import UserProfile, Patient, Donation


class UserProfileSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = UserProfile
        fields = ('birth_date', 'blood_type', 'gender')
        depth = 1
    
class UserSerializer(serializers.ModelSerializer):
    userProfile = UserProfileSerializer()
    
    class Meta:
        model = User
        fields = ('username', 'password', 'email', 'groups', 'first_name',
                  'last_name', 'userProfile')
        extra_kwargs = {'password': {'write_only': True}}

    def create(self, validated_data):
        profile_data = validated_data.pop('userProfile', None)
        user = User.objects.create_user(**validated_data)
        UserProfile.objects.create(user=user, **profile_data)
        return user
        
class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = ('name',) 
        
class DonationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Donation
        fields = ('donation_date',)
    
class PatientSerializer(serializers.ModelSerializer):

    class Meta:
        model = Patient
        fields = ('id', 'first_name', 'last_name', 'blood_type', 'gender')
        read_only_fields = ('id',)
        
    def create(self, validated_data):
        patient = Patient.objects.create(**validated_data)
        return patient

  

'''
Created on 30 de out de 2015

@author: alan
'''

from django.contrib.auth.models import User, Group
from engine.models import UserProfile, Patient
from rest_framework import serializers
from django.forms.models import fields_for_model

class UserProfileSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = UserProfile
        fields = ('birth_date', 'blood_type')
        depth = 1
    
class UserSerializer(serializers.ModelSerializer):
    userProfile = UserProfileSerializer()
    
    class Meta:
        model = User
        fields = ('username', 'password', 'email', 'groups', 'first_name',
                  'last_name', 'userProfile')
        write_only_fields = ('password',)

    def create(self, validated_data):
        profile_data = validated_data.pop('userProfile', None)
        user = User.objects.create_user(**validated_data)
        UserProfile.objects.create(user=user, **profile_data)
        return user
        
class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = ('name')


class PatientSerializer(serializers.ModelSerializer):

    class Meta:
        model = Patient
        fields = fields_for_model(Patient)
        
    def create(self, validated_data):
        patient = Patient.objects.create(**validated_data)
        return patient

  

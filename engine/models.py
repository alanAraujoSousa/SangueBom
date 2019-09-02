from django.db.models.signals import post_save
from django.dispatch.dispatcher import receiver
from rest_framework.authtoken.models import Token
from django.db import models
from datetime import datetime
from django.contrib.auth.models import User

GENDERS = ((u'M', u'Male'), (u'F', u'Female'))
BLOOD_TYPES = ((u'O+', u'O positive'), (u'O-', u'O negative'), 
               (u'A+', u'A positive'), (u'A-', u'A negative'), 
               (u'B+', u'B positive'), (u'B-', u'B negative'), 
               (u'AB+', u'AB positive'), (u'AB-', u'AB negative'))
        
class UserProfile(models.Model):
    user = models.OneToOneField(User, related_name="userProfile",)
    birth_date = models.DateField()
    blood_type = models.CharField(max_length=3, choices=BLOOD_TYPES)
    gender = models.CharField(max_length=1, choices=GENDERS)

    def __unicode__(self):
        return u'%s' % self.user.username
    
class Donation(models.Model):
    userProfile = models.ForeignKey(UserProfile, related_name='donations')
    donation_date = models.DateField(default=datetime.now)
    
    def __unicode__(self):
        return u'%s' % self.donation_date
    
    class Meta :
        ordering = ('donation_date',)
        
class Patient(models.Model):
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    blood_type = models.CharField(max_length=3, choices=BLOOD_TYPES)
    gender = models.CharField(max_length=1, choices=GENDERS)
    
    def __unicode__(self):
        return u'%s %s %s' % (self.id, self.first_name, self.last_name)

@receiver(post_save, sender=User)
def create_auth_token(sender, instance=None, created=False, **Kwargs):
    if created:
        Token.objects.create(user=instance)



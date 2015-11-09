from django.db.models.signals import post_save
from django.dispatch.dispatcher import receiver
from rest_framework.authtoken.models import Token
from django.db import models
from datetime import datetime
from django.contrib.auth.models import User

        
class UserProfile(models.Model):
    user = models.OneToOneField(User,related_name="userProfile",)
    birth_date = models.DateField()
    blood_type = models.CharField(max_length=3)

    def __unicode__(self):
        return u'%s' % self.user.username
    
class Donation(models.Model):
    user = models.ForeignKey(UserProfile, related_name='donations')
    donation_date = models.DateField(default=datetime.now)
    
    def __unicode__(self):
        return u'%s' % self.donation_date

@receiver(post_save, sender=User)
def create_auth_token(sender, instance=None, created=False, **Kwargs):
    if created:
        Token.objects.create(user=instance)



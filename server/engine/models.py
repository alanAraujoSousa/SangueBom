from django.conf import settings
from django.db.models.signals import post_save
from django.dispatch.dispatcher import receiver
from rest_framework.authtoken.models import Token
from django.db import models
from datetime import datetime

        
@receiver(post_save, sender=settings.AUTH_USER_MODEL)
def create_auth_token(sender, instance=None, created=False, **Kwargs):
    if created:
        Token.objects.create(user=instance)


class UserProfile(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL)
    birth_day = models.DateField()
    blood_type = models.CharField(max_length=3)
    
        
class Donations(models.Model):
    user = models.ForeignKey(UserProfile, related_name='donations')
    donation_date = models.DateField(default=datetime.now)

from django.contrib import admin
from engine.models import Donation, UserProfile, Patient

admin.site.register(Donation)
admin.site.register(UserProfile)
admin.site.register(Patient)
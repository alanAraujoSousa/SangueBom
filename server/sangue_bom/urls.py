"""
sangue_bom URL Configuration

"""

from django.conf.urls import patterns, include, url
from django.contrib import admin
from rest_framework.authtoken import views
from rest_framework.routers import DefaultRouter

from engine.views import UserViewSet, PatientViewSet
admin.autodiscover()

router = DefaultRouter()
router.register(r'users', UserViewSet)
router.register(r'patients', PatientViewSet)

urlpatterns = patterns('',
    url(r'^engine/', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^api-auth-token/', views.obtain_auth_token, name='authentication'),
    url(r'^admin/', include(admin.site.urls)),
)

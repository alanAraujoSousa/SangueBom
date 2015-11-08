from rest_framework.test import APITestCase
from rest_framework.reverse import reverse
from rest_framework import status


class LoginTest(APITestCase):
    def test_login(self):
        """
        Test login with superUser \o/
        """
        data = {'username':'root','password':'root'}
        
        url = reverse('authentication')
        
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_200_OK)

class CreateUserTest(APITestCase):
    def create_user(self):
        """
        Test create a User profile
        """
        
        data = {'blood_type':'o+', 'user':{'username':'root','password':'root'}}
        
        url = '/users/'
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
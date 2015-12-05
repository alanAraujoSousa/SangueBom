# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
import datetime
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        ('engine', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Donation',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('donation_date', models.DateField(default=datetime.datetime.now)),
            ],
        ),
        migrations.RemoveField(
            model_name='donations',
            name='user',
        ),
        migrations.RenameField(
            model_name='userprofile',
            old_name='birth_day',
            new_name='birth_date',
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='user',
            field=models.OneToOneField(related_name='userProfile', to=settings.AUTH_USER_MODEL),
        ),
        migrations.DeleteModel(
            name='Donations',
        ),
        migrations.AddField(
            model_name='donation',
            name='user',
            field=models.ForeignKey(related_name='donations', to='engine.UserProfile'),
        ),
    ]

# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('engine', '0002_auto_20151109_0313'),
    ]

    operations = [
        migrations.CreateModel(
            name='Patient',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('first_name', models.CharField(max_length=30)),
                ('last_name', models.CharField(max_length=30)),
                ('blood_type', models.CharField(max_length=3)),
            ],
        ),
        migrations.AlterModelOptions(
            name='donation',
            options={'ordering': ('donation_date',)},
        ),
        migrations.RenameField(
            model_name='donation',
            old_name='user',
            new_name='userProfile',
        ),
    ]

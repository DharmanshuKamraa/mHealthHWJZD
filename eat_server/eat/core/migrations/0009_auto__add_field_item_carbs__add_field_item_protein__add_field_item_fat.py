# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding field 'Item.carbs'
        db.add_column(u'core_item', 'carbs',
                      self.gf('django.db.models.fields.CharField')(default='27', max_length=255, null=True),
                      keep_default=False)

        # Adding field 'Item.protein'
        db.add_column(u'core_item', 'protein',
                      self.gf('django.db.models.fields.CharField')(default='13', max_length=255, null=True),
                      keep_default=False)

        # Adding field 'Item.fat'
        db.add_column(u'core_item', 'fat',
                      self.gf('django.db.models.fields.CharField')(default='2', max_length=255, null=True),
                      keep_default=False)

        # Adding field 'Item.vitamins'
        db.add_column(u'core_item', 'vitamins',
                      self.gf('django.db.models.fields.CharField')(default='10', max_length=255, null=True),
                      keep_default=False)


    def backwards(self, orm):
        # Deleting field 'Item.carbs'
        db.delete_column(u'core_item', 'carbs')

        # Deleting field 'Item.protein'
        db.delete_column(u'core_item', 'protein')

        # Deleting field 'Item.fat'
        db.delete_column(u'core_item', 'fat')

        # Deleting field 'Item.vitamins'
        db.delete_column(u'core_item', 'vitamins')


    models = {
        u'auth.group': {
            'Meta': {'object_name': 'Group'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '80'}),
            'permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': u"orm['auth.Permission']", 'symmetrical': 'False', 'blank': 'True'})
        },
        u'auth.permission': {
            'Meta': {'ordering': "(u'content_type__app_label', u'content_type__model', u'codename')", 'unique_together': "((u'content_type', u'codename'),)", 'object_name': 'Permission'},
            'codename': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'content_type': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['contenttypes.ContentType']"}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'})
        },
        u'contenttypes.contenttype': {
            'Meta': {'ordering': "('name',)", 'unique_together': "(('app_label', 'model'),)", 'object_name': 'ContentType', 'db_table': "'django_content_type'"},
            'app_label': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'model': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '100'})
        },
        u'core.authtoken': {
            'Meta': {'object_name': 'AuthToken'},
            'created': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'token': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['core.User']"})
        },
        u'core.cart': {
            'Meta': {'ordering': "('-modified', '-created')", 'object_name': 'Cart'},
            'cart_type': ('django.db.models.fields.CharField', [], {'default': "'WEIGHT_LOSS'", 'max_length': '255'}),
            'created': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['core.User']"})
        },
        u'core.cartitem': {
            'Meta': {'ordering': "('-modified', '-created')", 'object_name': 'CartItem'},
            'cart': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['core.Cart']"}),
            'created': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'item': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['core.Item']"}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'})
        },
        u'core.item': {
            'Meta': {'ordering': "('-modified', '-created')", 'object_name': 'Item'},
            'carbs': ('django.db.models.fields.CharField', [], {'default': "'27'", 'max_length': '255', 'null': 'True'}),
            'created': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'}),
            'dairy': ('django.db.models.fields.BooleanField', [], {}),
            'eggs': ('django.db.models.fields.BooleanField', [], {}),
            'fat': ('django.db.models.fields.CharField', [], {'default': "'2'", 'max_length': '255', 'null': 'True'}),
            'gluten': ('django.db.models.fields.BooleanField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'image': ('django.db.models.fields.files.ImageField', [], {'max_length': '100', 'blank': 'True'}),
            'item_type': ('django.db.models.fields.CharField', [], {'default': "'VEGETABLES'", 'max_length': '255'}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'db_index': 'True'}),
            'nuts': ('django.db.models.fields.BooleanField', [], {}),
            'price': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'price_per_saving': ('django.db.models.fields.CharField', [], {'max_length': '255', 'null': 'True'}),
            'protein': ('django.db.models.fields.CharField', [], {'default': "'13'", 'max_length': '255', 'null': 'True'}),
            'store': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['core.Store']"}),
            'vegetarian': ('django.db.models.fields.BooleanField', [], {}),
            'vitamins': ('django.db.models.fields.CharField', [], {'default': "'10'", 'max_length': '255', 'null': 'True'})
        },
        u'core.store': {
            'Meta': {'ordering': "('-modified', '-created')", 'object_name': 'Store'},
            'created': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lat': ('django.db.models.fields.FloatField', [], {'default': '20.0'}),
            'lon': ('django.db.models.fields.FloatField', [], {'default': '20.0'}),
            'modified': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now', 'blank': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'db_index': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['core.User']"})
        },
        u'core.user': {
            'Meta': {'object_name': 'User'},
            'date_joined': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'email': ('django.db.models.fields.EmailField', [], {'max_length': '75', 'blank': 'True'}),
            'first_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'groups': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'related_name': "u'user_set'", 'blank': 'True', 'to': u"orm['auth.Group']"}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'True'}),
            'is_staff': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'is_superuser': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'last_login': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'last_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'modifed': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'password': ('django.db.models.fields.CharField', [], {'max_length': '128'}),
            'type': ('django.db.models.fields.CharField', [], {'default': "'USER'", 'max_length': '255', 'db_index': 'True'}),
            'user_permissions': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'related_name': "u'user_set'", 'blank': 'True', 'to': u"orm['auth.Permission']"}),
            'username': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '30'})
        }
    }

    complete_apps = ['core']
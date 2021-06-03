from django.contrib import admin
from import_export import resources
from table.models import Syslog
from import_export.admin import ImportExportModelAdmin
# Register your models here.
 



class SyslogResource(resources.ModelResource):

    class Meta:
        model = Syslog



class SyslogAdmin(ImportExportModelAdmin):
    resource_class = SyslogResource

admin.site.register(Syslog, SyslogAdmin)
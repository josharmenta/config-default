sudo -u ctsms /ctsms/dbtool.sh -i -f
sudo -u ctsms /ctsms/dbtool.sh -icp /ctsms/master_data/criterion_property_definitions.csv
sudo -u ctsms /ctsms/dbtool.sh -ipd /ctsms/master_data/permission_definitions.csv
sudo -u ctsms /ctsms/dbtool.sh -imi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -ims /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -imc /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -imt /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -imp /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -immm /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -imifi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -imsi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -impi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -imjf /ctsms/master_data/mime.types -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -imcc /ctsms/master_data/mime.types -e ISO-8859-1

sudo -u ctsms /ctsms/dbtool.sh -it /ctsms/master_data/titles.csv -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -ib /ctsms/master_data/kiverzeichnis_gesamt_de_1347893202433.csv -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -ic /ctsms/master_data/countries.txt -e ISO-8859-1
sudo -u ctsms /ctsms/dbtool.sh -iz /ctsms/master_data/streetnames.csv -e utf-8
sudo -u ctsms /ctsms/dbtool.sh -is /ctsms/master_data/streetnames.csv -e utf-8

sudo -u ctsms /ctsms/dbtool.sh -iis /ctsms/master_data/icd10gm2012syst_claml_20110923.xml -sl de
sudo -u ctsms /ctsms/dbtool.sh -iai /ctsms/master_data/icd10gm2012_alphaid_edv_ascii_20110930.txt -e ISO-8859-1 -isr icd10gm2012syst_claml_20110923
sudo -u ctsms /ctsms/dbtool.sh -ios /ctsms/master_data/ops2012syst_claml_20111103.xml -sl de
sudo -u ctsms /ctsms/dbtool.sh -ioc /ctsms/master_data/ops2011alpha_edv_ascii_20111031.txt -osr ops2012syst_claml_20111103
sudo -u ctsms /ctsms/dbtool.sh -ia /ctsms/master_data/asp_register_20181005.xls

DEPARTMENT_PASSWORD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)
USER_PASSWORD=$(cat /dev/urandom | tr -dc 'a-z' | fold -w 3 | head -n 1)
sudo -u ctsms /ctsms/dbtool.sh -cd -dlk my_department -dp "$DEPARTMENT_PASSWORD"
sudo -u ctsms /ctsms/dbtool.sh -cu -dlk my_department -dp "$DEPARTMENT_PASSWORD" -u "phoenix" -p "$USER_PASSWORD" -pp "INVENTORY_MASTER_ALL_DEPARTMENTS,STAFF_MASTER_ALL_DEPARTMENTS,COURSE_MASTER_ALL_DEPARTMENTS,TRIAL_MASTER_ALL_DEPARTMENTS,PROBAND_MASTER_ALL_DEPARTMENTS,USER_ALL_DEPARTMENTS,INPUT_FIELD_MASTER,MASS_MAIL_MASTER_ALL_DEPARTMENTS,INVENTORY_MASTER_SEARCH,STAFF_MASTER_SEARCH,COURSE_MASTER_SEARCH,TRIAL_MASTER_SEARCH,PROBAND_MASTER_SEARCH,USER_MASTER_SEARCH,INPUT_FIELD_MASTER_SEARCH,MASS_MAIL_MASTER_SEARCH"

sudo -u ctsms /ctsms/dbtool.sh -cu -dlk my_department -dp "$DEPARTMENT_PASSWORD" -u "my_department_signup_de" -p "my_department_signup_de" -ul de -pp "INVENTORY_VIEW_USER_DEPARTMENT,STAFF_DETAIL_IDENTITY,COURSE_VIEW_USER_DEPARTMENT,TRIAL_SIGNUP,PROBAND_SIGNUP,USER_ACTIVE_USER,INPUT_FIELD_VIEW,MASS_MAIL_SIGNUP,INVENTORY_NO_SEARCH,STAFF_NO_SEARCH,COURSE_NO_SEARCH,TRIAL_NO_SEARCH,PROBAND_NO_SEARCH,USER_NO_SEARCH,INPUT_FIELD_NO_SEARCH,MASS_MAIL_NO_SEARCH"
sudo -u ctsms /ctsms/dbtool.sh -cu -dlk my_department -dp "$DEPARTMENT_PASSWORD" -u "my_department_signup_en" -p "my_department_signup_en" -ul en -pp "INVENTORY_VIEW_USER_DEPARTMENT,STAFF_DETAIL_IDENTITY,COURSE_VIEW_USER_DEPARTMENT,TRIAL_SIGNUP,PROBAND_SIGNUP,USER_ACTIVE_USER,INPUT_FIELD_VIEW,MASS_MAIL_SIGNUP,INVENTORY_NO_SEARCH,STAFF_NO_SEARCH,COURSE_NO_SEARCH,TRIAL_NO_SEARCH,PROBAND_NO_SEARCH,USER_NO_SEARCH,INPUT_FIELD_NO_SEARCH,MASS_MAIL_NO_SEARCH"

CRON_PASSWORD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 8 | head -n 1)
sudo -u ctsms /ctsms/dbtool.sh -cu -dlk my_department -dp "$DEPARTMENT_PASSWORD" -u "my_department_cron" -p "$CRON_PASSWORD" -pp "INVENTORY_MASTER_ALL_DEPARTMENTS,STAFF_MASTER_ALL_DEPARTMENTS,COURSE_MASTER_ALL_DEPARTMENTS,TRIAL_MASTER_ALL_DEPARTMENTS,PROBAND_MASTER_ALL_DEPARTMENTS,USER_ALL_DEPARTMENTS,INPUT_FIELD_MASTER,MASS_MAIL_MASTER_ALL_DEPARTMENTS,INVENTORY_MASTER_SEARCH,STAFF_MASTER_SEARCH,COURSE_MASTER_SEARCH,TRIAL_MASTER_SEARCH,PROBAND_MASTER_SEARCH,USER_MASTER_SEARCH,INPUT_FIELD_MASTER_SEARCH,MASS_MAIL_MASTER_SEARCH"
sed -r -i "s|ctsmsrestapi_password.*|ctsmsrestapi_password = ${CRON_PASSWORD}|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/Criteria/config.cfg
sed -r -i "s|ctsmsrestapi_password.*|ctsmsrestapi_password = ${CRON_PASSWORD}|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/Duplicates/config.cfg
sed -r -i "s|ctsmsrestapi_password.*|ctsmsrestapi_password = ${CRON_PASSWORD}|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/EcrfExporter/config.cfg
sed -r -i "s|ctsmsrestapi_password.*|ctsmsrestapi_password = ${CRON_PASSWORD}|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/EcrfImporter/config.cfg
sed -r -i "s|ctsmsrestapi_password.*|ctsmsrestapi_password = ${CRON_PASSWORD}|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/InquiryExporter/config.cfg

IP=$(ip addr | grep 'state UP' -A2 | tail -n1 | awk '{print $2}' | cut -f1  -d'/')
sed -r -i "s|ctsms_base_uri.*|ctsms_base_uri: 'https://${IP}'|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/EcrfExporter/settings.yml
sed -r -i "s|ctsms_base_uri.*|ctsms_base_uri: 'https://${IP}'|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/EcrfImporter/settings.yml
sed -r -i "s|ctsms_base_uri.*|ctsms_base_uri: 'https://${IP}'|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/InquiryExporter/settings.yml
sed -r -i "s|ctsms_base_uri.*|ctsms_base_uri: 'https://${IP}'|" /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/WebApps/Signup/settings.yml

###create some default queries/reports
cd /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/Criteria
perl process.pl --task=create_criteria --force --skip-errors

echo "The department passphrase for 'my_department' when adding users with /ctsms/dbtool.sh is '$DEPARTMENT_PASSWORD'."
echo "Log in at https://$IP with username 'phoenix' password '$USER_PASSWORD'."
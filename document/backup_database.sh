#!/bin/bash
databases=$( mysql -uroot -proot -N information_schema -e "SELECT DISTINCT(TABLE_SCHEMA) FROM tables WHERE TABLE_SCHEMA LIKE 'mg%'");
for db in ${databases[@]}
do
    mysqldump -u root -proot --databases $db | gzip > /opt/backup_databases/backup_"$db"_`date '+%Y%m%d'`.sql.gz;
done


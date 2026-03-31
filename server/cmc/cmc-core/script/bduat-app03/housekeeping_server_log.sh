#!/bin/sh

echo `date '+%F %H:%M:%S'` $0 ' Start --'

DATE_TO_ZIP=5
DATE_TO_REMOVE=60

HOUSEKEEP_PATH_BATCHJOB=/bd-ogcmr/bduat/cmc/*/script/log
FILE_PATTERN_BATCHJOB="batchjob.log.*"
# zip file
find $HOUSEKEEP_PATH_BATCHJOB -type f -mtime +$DATE_TO_ZIP -name "$FILE_PATTERN_BATCHJOB" -not \( -name "*.gz" \) -exec gzip '{}' \;
# clean file
find $HOUSEKEEP_PATH_BATCHJOB -type f -mtime +$DATE_TO_REMOVE -name "$FILE_PATTERN_BATCHJOB.gz" -exec rm '{}' \;

HOUSEKEEP_PATH_JBOSS=/bd-ogcmr/bduat/jboss/wildfly/*/log/
FILE_PATTERN_JBOSS="server.log.*"
# zip file
find $HOUSEKEEP_PATH_JBOSS -type f -mtime +$DATE_TO_ZIP -name "$FILE_PATTERN_JBOSS" -not \( -name "*.gz" \) -exec gzip '{}' \;
# clean file
find $HOUSEKEEP_PATH_JBOSS -type f -mtime +$DATE_TO_REMOVE -name "$FILE_PATTERN_JBOSS.gz" -exec rm '{}' \;

echo `date '+%F %H:%M:%S'` $0 ' End --'


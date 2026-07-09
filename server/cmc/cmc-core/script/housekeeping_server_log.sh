#!/bin/sh

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv.sh

DATE_TO_ZIP=5
DATE_TO_REMOVE=60

# zip file
find $HOUSEKEEP_PATH_BATCHJOB -type f -mtime +$DATE_TO_ZIP -name "$FILE_PATTERN_BATCHJOB" -not \( -name "*.gz" \) -exec gzip '{}' \;
# clean file
find $HOUSEKEEP_PATH_BATCHJOB -type f -mtime +$DATE_TO_REMOVE -name "$FILE_PATTERN_BATCHJOB.gz" -exec rm '{}' \;

# zip file
find $HOUSEKEEP_PATH_JBOSS -type f -mtime +$DATE_TO_ZIP -name "$FILE_PATTERN_JBOSS" -not \( -name "*.gz" \) -exec gzip '{}' \;
# clean file
find $HOUSEKEEP_PATH_JBOSS -type f -mtime +$DATE_TO_REMOVE -name "$FILE_PATTERN_JBOSS.gz" -exec rm '{}' \;

echo `date '+%F %H:%M:%S'` $0 ' End --'


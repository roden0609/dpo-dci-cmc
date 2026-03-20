#!/bin/sh

. /etc/bashrc

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv.sh

ROOT_PATH=/bd-ogcmr/uat/cmc/cmc-core/script/
FILE_DATE=`${ROOT_PATH}previous_date.sh`
TODAY=`date +%Y%m%d`

OLD_LOG_FILE=${ROOT_PATH}log/batchjob.log
NEW_LOG_FILE=${ROOT_PATH}log/batchjob.log.${FILE_DATE}

mv ${OLD_LOG_FILE} ${NEW_LOG_FILE}
touch ${OLD_LOG_FILE}

echo `date '+%F %H:%M:%S'` $0 ' End --'


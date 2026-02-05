#!/bin/sh

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv.sh
. `dirname $0`/setEnvHouseKeep.sh

BATCH_CLASS=hk.gov.cmc.batchjob.HouseKeepingRecordService

INPUT_DATE=$1

${JAVA_HOME}/bin/java -Djboss.ejb.client.properties.file.path=${PROPERTIES_FILE} -Dlog4j2.configurationFile=file:${CONFIG}/log4j2.properties -cp ${CLASSPATH} ${BATCH_CLASS} ${PROPERTIES_FILE} 2 ${INPUT_DATE}

rc=$?

echo `date '+%F %H:%M:%S'` $0 'End --'
exit $rc


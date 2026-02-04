#!/bin/sh

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv_uat_WC-1.sh

PROPERTIES_FILE=${CONFIG}/cmc_batchjob_uat_WC-1_CMC-core.properties
BATCH_CLASS=hk.gov.ogcio.mars_cmc.cmc.service.batchjob.housekeepingrecord.HouseKeepingRecordService

CLASSPATH=$LIB/cmc_app.jar
CLASSPATH=$CLASSPATH:$LIB/egis_common_web-1.0.8.jar
CLASSPATH=$CLASSPATH:${JBOSS_HOME}/bin/client/jboss-client.jar
CLASSPATH=$CLASSPATH:${LIB}/commons-logging-1.0.4.jar:${LIB}/log4j-1.2.17.jar

INPUT_DATE=$1

$JAVA_HOME/bin/java -Djboss.ejb.client.properties.file.path=${PROPERTIES_FILE} -Dlog4j.configuration=file:${CONFIG}/log4j_KB.properties -cp $CLASSPATH $BATCH_CLASS $PROPERTIES_FILE 3 ${INPUT_DATE}

rc=$?

echo `date '+%F %H:%M:%S'` $0 'End --'
exit $rc


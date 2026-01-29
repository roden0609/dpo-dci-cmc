#!/bin/sh

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv_uat_WC-2.sh

PROPERTIES_FILE=${CONFIG}/cmc_batchjob_uat_WC-2_CMC-core.properties
BATCH_CLASS=hk.gov.ogcio.mars_cmc.batchfw.client.FwBatchClient
IMPL_CLASS=hk.gov.ogcio.mars_cmc.cmc.batch.CmcSendIasApplicationNotiJob

CLASSPATH=${CLASSPATH}:${LIB}/reportfw_ejb3.jar
CLASSPATH=${CLASSPATH}:${JBOSS_HOME}/bin/client/jboss-client.jar
CLASSPATH=${CLASSPATH}:${LIB}/log4j-1.2.17.jar

${JAVA_HOME}/bin/java -Djboss.ejb.client.properties.file.path=${CONFIG}/cmc_batchjob_uat_WC-2_CMC-core.properties -Dlog4j.configuration=file:${CONFIG}/log4j_KB.properties -cp ${CLASSPATH} ${BATCH_CLASS} ${PROPERTIES_FILE} R ${IMPL_CLASS} execute

rc=$?

echo `date '+%F %H:%M:%S'` $0 ' End --'

exit $rc

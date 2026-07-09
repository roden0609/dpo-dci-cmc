#!/bin/sh

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv.sh

BATCH_CLASS=hk.gov.cmc.batchjob.FwBatchClient
IMPL_CLASS=hk.gov.cmc.batch.CmcQueryIasApplicationResultJob

if [ "$#" -ge 1 ]; then
  PARAM=sizeLimit=$1
fi

${JAVA_HOME}/bin/java -Djboss.ejb.client.properties.file.path=${PROPERTIES_FILE} -Dlog4j.configurationFile=file:${CONFIG}/log4j2.properties -cp ${CLASSPATH} ${BATCH_CLASS} ${PROPERTIES_FILE} R ${IMPL_CLASS} execute ${PARAM}

rc=$?

echo `date '+%F %H:%M:%S'` $0 ' End --'

exit $rc

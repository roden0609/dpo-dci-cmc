#!/bin/sh

if [ "$#" -ge 3 ];
then
    echo "Arguments 1 <asyn app Id list>"
    echo "Arguments 2 <single pull call limit>"
    exit 0
fi

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv.sh

BATCH_CLASS=hk.gov.cmc.batchjob.MaintainMessageBackgroundService

${JAVA_HOME}/bin/java -Djboss.ejb.client.properties.file.path=${PROPERTIES_FILE} -Dlog4j2.configurationFile=file:${CONFIG}/log4j2.properties -cp ${CLASSPATH} ${BATCH_CLASS} ${PROPERTIES_FILE} $1 $2

rc=$?

echo `date '+%F %H:%M:%S'` $0 ' End --'

exit $rc



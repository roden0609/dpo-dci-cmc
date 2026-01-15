#!/bin/sh

if [ "$#" -ge 3 ];
then
        echo "Arguments 1 <asyn app Id list>"
        echo "Arguments 2 <single pull call limit>"
        exit 0
fi

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv.sh

PROPERTIES_FILE=${CONFIG}/batchjob.properties
BATCH_CLASS=hk.gov.ogcio.mars_cmc.cmc.service.batchjob.maintainmessage.MaintainMessageBackgroundService

CLASSPATH=$LIB/cmc_app.jar
CLASSPATH=$CLASSPATH:$LIB/egis_common_web-1.0.8.jar
CLASSPATH=$CLASSPATH:${JBOSS_HOME}/bin/client/jboss-client.jar
CLASSPATH=$CLASSPATH:${LIB}/commons-logging-1.0.4.jar:${LIB}/log4j-1.2.17.jar
CLASSPATH=$CLASSPATH:${LIB}/castor-1.3.3-core.jar
CLASSPATH=$CLASSPATH:${LIB}/castor-1.3.3-xml.jar

${JAVA_HOME}/bin/java -Djboss.ejb.client.properties.file.path=${PROPERTIES_FILE} -Dlog4j.configuration=file:${CONFIG}/log4j_KB.properties -cp ${CLASSPATH} ${BATCH_CLASS} ${PROPERTIES_FILE} $1 $2

rc=$?

echo `date '+%F %H:%M:%S'` $0 ' End --'

exit $rc



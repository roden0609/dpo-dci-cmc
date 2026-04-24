#!/bin/sh

echo `date '+%F %H:%M:%S'` $0 ' Start --'

. `dirname $0`/setEnv.sh

BATCH_CLASS=hk.gov.cmc.batchjob.FwBatchClient
IMPL_CLASS=hk.gov.cmc.batch.CmcLogCheckJob

PRIMARY_HOSTNAME=ecs-ttcp-cmcapp03
SECONDARY_HOSTNAME=ecs-ttcp-cmcapp04
CURR_HOSTNAME=`/bin/hostname -s`

if [ x"${CURR_HOSTNAME}" = x"${PRIMARY_HOSTNAME}" ]
then
   REMOTE_HOSTNAME=${SECONDARY_HOSTNAME}
else
   REMOTE_HOSTNAME=${PRIMARY_HOSTNAME}
fi

echo ${CURR_HOSTNAME}
echo ${REMOTE_HOSTNAME}

if [ -n "$1" ];then
  BATCH_DATE=$1
else
  BATCH_DATE=`date --date="-1 days" +%Y-%m-%d`
fi

CONSOL_ERROR_LOG_PATH=${ROOT_PATH}/cmc-health-check-report
LOCAL_CONSOL_ERROR_DETAIL_LOG=${CONSOL_ERROR_LOG_PATH}/cmcConsolErrorDetail_${CURR_HOSTNAME}_${BATCH_DATE}.txt
LOCAL_CONSOL_ERROR_COUNT_LOG=${CONSOL_ERROR_LOG_PATH}/cmcConsolErrorCount_${CURR_HOSTNAME}_${BATCH_DATE}.txt
REMOTE_CONSOL_ERROR_DETAIL_LOG=${CONSOL_ERROR_LOG_PATH}/cmcConsolErrorDetail_${REMOTE_HOSTNAME}_${BATCH_DATE}.txt
REMOTE_CONSOL_ERROR_COUNT_LOG=${CONSOL_ERROR_LOG_PATH}/cmcConsolErrorCount_${REMOTE_HOSTNAME}_${BATCH_DATE}.txt

LOGFILE_PREFFIX_LIST=(
/bd-ogcmr/uat/jboss/wildfly/cmc-core/log/server.log
)

echo `date`: Daily Log Checking for ${BATCH_DATE} Start --

## Step 1: grep the fatal, error and warn message
echo "In ${CURR_HOSTNAME} on ${BATCH_DATE}:"  > ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
echo "" >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
echo "In ${CURR_HOSTNAME} on ${BATCH_DATE}:"  > ${LOCAL_CONSOL_ERROR_COUNT_LOG}
echo "" >> ${LOCAL_CONSOL_ERROR_COUNT_LOG}

for LOGFILE in ${LOGFILE_PREFFIX_LIST[@]}
do
   echo `date`: Check log file ${LOGFILE}.${BATCH_DATE}
   echo "For log file ${LOGFILE}.${BATCH_DATE}" >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   echo "list of FATAL found:" >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   # grep FATAL ${LOGFILE}.${BATCH_DATE} >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   awk '/^[0-9]{4}-.*\[FATAL\]/ {flag=1}/^[0-9]{4}-/ && !/\[FATAL\]/ {flag=0}flag' ${LOGFILE}.${BATCH_DATE} >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   echo "list of ERROR found:" >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   # grep "^[0-9]\{4\}-[0-9]\{2\}-[0-9]\{2\} [0-9]\{2\}:[0-9]\{2\}:[0-9]\{2\},[0-9]\{3\} \[ERROR\]\|^[^0-9]" ${LOGFILE}.${BATCH_DATE} >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   awk '/^[0-9]{4}-.*\[ERROR\]/ {flag=1}/^[0-9]{4}-/ && !/\[ERROR\]/ {flag=0}flag' ${LOGFILE}.${BATCH_DATE} >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   echo "list of WARN found:" >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   # grep WARN ${LOGFILE}.${BATCH_DATE} >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   awk '/^[0-9]{4}-.*\[WARN\]/ {flag=1}/^[0-9]{4}-/ && !/\[WARN\]/ {flag=0}flag' ${LOGFILE}.${BATCH_DATE} >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}

   FATAL_COUNT=`grep FATAL ${LOGFILE}.${BATCH_DATE} | wc -l`
   ERROR_COUNT=`grep ERROR ${LOGFILE}.${BATCH_DATE} | wc -l`
   WARN_COUNT=`grep WARN ${LOGFILE}.${BATCH_DATE} | wc -l`
   echo "For log file ${LOGFILE}.${BATCH_DATE}," >> ${LOCAL_CONSOL_ERROR_COUNT_LOG}
   echo "   number of FATAL, ERROR and WARN is ${FATAL_COUNT}, ${ERROR_COUNT} and ${WARN_COUNT}"  >> ${LOCAL_CONSOL_ERROR_COUNT_LOG}

   echo "" >> ${LOCAL_CONSOL_ERROR_DETAIL_LOG}
   echo "" >> ${LOCAL_CONSOL_ERROR_COUNT_LOG}
done

## Step 2: if this server is primary host, copy the consolidated information from remote server of the same site
if [ x"${CURR_HOSTNAME}" = x"${PRIMARY_HOSTNAME}" ]
then
   scp cmruatadm@ecs-ttcp-cmcapp04:${REMOTE_CONSOL_ERROR_COUNT_LOG} ${CONSOL_ERROR_LOG_PATH}
   scp cmruatadm@ecs-ttcp-cmcapp04:${REMOTE_CONSOL_ERROR_DETAIL_LOG} ${CONSOL_ERROR_LOG_PATH}
fi

## Step 3: if this server is primary host, send the consolidated information via email
if [ x"${CURR_HOSTNAME}" = x"${PRIMARY_HOSTNAME}" ]
then
   ${JAVA_HOME}/bin/java -Djboss.ejb.client.properties.file.path=${PROPERTIES_FILE} -Dlog4j.configurationFile=file:${CONFIG}/log4j2.properties -cp ${CLASSPATH} ${BATCH_CLASS} ${PROPERTIES_FILE} R ${IMPL_CLASS} execute "cmcLogCheckBatchDate=$BATCH_DATE"
fi

rc=$?

echo `date`: Daily Log Checking for ${BATCH_DATE} End --

exit $rc

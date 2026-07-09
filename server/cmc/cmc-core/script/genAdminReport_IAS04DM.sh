#!/bin/sh

. `dirname $0`/setEnv.sh

thisDay=`date +%d`

if [ "$thisDay" = "01" ]; then
        REPORT_IDS="RPT-IAS-04-D,RPT-IAS-04-M"
else
    REPORT_IDS="RPT-IAS-04-D"
fi

BATCH_NAME_SUFFIX="IAS04DM"

export SERVLET_URL="http://${DOMAIN_NAME}:${HTTP_PORT}/dci-cmc-wss/servlet/GenAdminBatchReportServlet?reportId=${REPORT_IDS}&batchName=${BATCH_NAME_SUFFIX}"

echo `date`: Run generation of batch report ${REPORT_IDS}

curl -s "${SERVLET_URL}"

rc=$?

echo `date`: End running generation of batch report ${REPORT_IDS}

exit $rc

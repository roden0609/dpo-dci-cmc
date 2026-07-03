#!/bin/sh

. `dirname $0`/setEnv.sh

REPORT_IDS="RPT-IAS-11-D"
BATCH_NAME_SUFFIX="IAS11D"

export SERVLET_URL="http://${DOMAIN_NAME}:${HTTP_PORT}/dci-cmc-wss/servlet/GenAdminBatchReportServlet?reportId=${REPORT_IDS}&batchName=${BATCH_NAME_SUFFIX}"

echo `date`: Run generation of batch report ${REPORT_IDS}

curl -s "${SERVLET_URL}"

rc=$?

echo `date`: End running generation of batch report ${REPORT_IDS}

exit $rc

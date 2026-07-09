#!/bin/sh

. `dirname $0`/setEnv.sh

export SERVLET_URL="http://${DOMAIN_NAME}:${HTTP_PORT}/dci-cmc-wss/servlet/RegenerateAdminBatchReportServlet"

echo `date`: Run regenerate batch reports

curl -s "${SERVLET_URL}"

rc=$?

echo `date`: End running regenerate batch reports

exit $rc

#!/bin/sh

. `dirname $0`/setEnv.sh

DOMAIN_NAME=172.16.67.13
HTTP_PORT=21280

export SERVLET_URL="http://${DOMAIN_NAME}:${HTTP_PORT}/dci-cmc-wss/servlet/CmcSysParamServlet?reload=REVOKECEK"

echo 'iAM Smart REVOKECEK '`date '+%F %H:%M:%S'` $0 ' Start --'

curl -sS "$SERVLET_URL"

rc=$?

echo 'iAM Smart REVOKECEK '`date '+%F %H:%M:%S'` $0 ' End --'

exit $rc

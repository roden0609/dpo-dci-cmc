#!/bin/sh

JAVA_HOME=/bd-ogcmr/uat/java/jdk/
LIB=/bd-ogcmr/uat/cmc/support/client-rm3/lib

CLASSPTH=
CLASSPATH=${CLASSPATH}:${LIB}/cmc/dci-cmc-ejb-client-rm3-3.0.0.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/xmlsec-4.0.2/xmlsec-4.0.2.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/saaj-impl-3.0.4/angus-activation-2.0.2.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/saaj-impl-3.0.4/saaj-impl-3.0.4.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/saaj-impl-3.0.4/stax-ex-2.1.0.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/jakarta-commons/commons-logging-1.3.4.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/jakarta-commons/commons-io-2.16.1.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/jakarta-commons/commons-lang-2.6.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/jakarta-commons/commons-lang3-3.1.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/rm-3/gcis_rm_common-1.1.0.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/rm-3/gcis_ss_common-1.1.0.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/rm-3/gcis_ss_msg_soap_client-1.1.0.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/rm-3/gcis_ss_noti_soap_client-1.1.0.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/jaxb-4.0.5/istack-commons-runtime-4.2.0.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/jaxb-4.0.5/jaxb-core-4.0.5.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/jaxb-4.0.5/jaxb-impl-4.0.5.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/jakarta-ee-10/jakarta.jakartaee-api-10.0.0.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/jakarta-ee-10/jakarta.jakartaee-core-api-10.0.0.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/jakarta-ee-10/jakarta.jakartaee-web-api-10.0.0.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/wss4j-3.0.3/slf4j-api-2.0.16.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/wss4j-3.0.3/wss4j-ws-security-common-3.0.3.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/wss4j-3.0.3/wss4j-ws-security-dom-3.0.3.jar

CLASSPATH=${CLASSPATH}:${LIB}/static/log4j-2.23.1/log4j-1.2-api-2.23.1.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/log4j-2.23.1/log4j-api-2.23.1.jar
CLASSPATH=${CLASSPATH}:${LIB}/static/log4j-2.23.1/log4j-core-2.23.1.jar

CLIENT_PROG=hk.gov.dpo.mars_cmc.cmc.client.maintainmessage.MaintainIasApplicationClientTest

PROPERTY_FILE=/bd-ogcmr/uat/cmc/support/client-rm3/config/kc_maint_application_hkp

${JAVA_HOME}/bin/java -Dlog4j.configuration=file:/bd-ogcmr/uat/cmc/support/client-rm3/config/log4j.properties -cp ${CLASSPATH} ${CLIENT_PROG} ${PROPERTY_FILE}

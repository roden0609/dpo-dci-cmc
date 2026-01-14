#!/bin/bash

SERVER_BASE_DIR=/bd-ogcmr/uat/jboss/wildfly/cmc-core
JAVA_CUSTOM_DIR=/bd-ogcmr/uat/java/customise

# Clean runtime directories
rm -rf $SERVER_BASE_DIR/tmp/
rm -rf $SERVER_BASE_DIR/data/
rm -rf $SERVER_BASE_DIR/configuration/standalone_xml_history/

# JVM options
JAVA_OPTS="
-Xms1024m
-Xmx1024m
-XX:MetaspaceSize=512m
-XX:MaxMetaspaceSize=512m
-Djava.net.preferIPv4Stack=true
-Djava.awt.headless=true
-Xbootclasspath/a:${JAVA_CUSTOM_DIR}/bcprov-jdk18on-1.83.jar
-Djava.security.properties=${JAVA_CUSTOM_DIR}/java.security.merge
-Djavax.net.ssl.trustStore=${JAVA_CUSTOM_DIR}/cacerts-override.jks
-Djavax.net.ssl.trustStorePassword=changeit
-Djavax.net.ssl.trustStoreType=JKS
"

export JAVA_OPTS

`dirname $0`/standalone.sh -c standalone-cmc-core.xml -b 172.16.67.13 -bmanagement=172.16.67.13 -Djboss.server.base.dir=$SERVER_BASE_DIR > /dev/null &

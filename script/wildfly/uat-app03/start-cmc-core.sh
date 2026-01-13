SERVER_BASE_DIR=/bd-ogcmr/uat/jboss/wildfly-32.0.1.Final/cmc-core

rm -rf $SERVER_BASE_DIR/tmp/
# TODO: interim solution, long term should not delete data completely, restart caching should handle in configuration file
# rm -rf $SERVER_BASE_DIR/data/
rm -rf $SERVER_BASE_DIR/data/kernel/
rm -rf $SERVER_BASE_DIR/data/content/
rm -rf $SERVER_BASE_DIR/data/tx-object-store/
rm -rf $SERVER_BASE_DIR/data/timer-service-data/
rm -rf $SERVER_BASE_DIR/data/activemq/
rm -rf $SERVER_BASE_DIR/data/infinispan/
rm -rf $SERVER_BASE_DIR/configuration/standalone_xml_history/

JAVA_OPTS="-Xms1024m -Xmx1024m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -Djava.net.preferIPv4Stack=true -Djava.awt.headless=true"
export JAVA_OPTS

`dirname $0`/standalone.sh -c standalone-cmc-core.xml -b 172.16.67.13 -bmanagement=172.16.67.13 -Djboss.server.base.dir=$SERVER_BASE_DIR > /dev/null &

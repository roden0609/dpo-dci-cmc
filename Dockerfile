FROM quay.io/wildfly/wildfly:32.0.1.Final-jdk17

# Ensure data dirs exist and owned by jboss
RUN mkdir -p /opt/jboss/wildfly/standalone/data/credentials \
 && chown -R jboss:jboss /opt/jboss/wildfly/standalone/data

# Install MySQL module
RUN mkdir -p /opt/jboss/wildfly/modules/com/mysql/main
COPY docker/wildfly/mysql/mysql-connector-j-8.4.0.jar \
     /opt/jboss/wildfly/modules/com/mysql/main/
COPY docker/wildfly/mysql/module.xml \
     /opt/jboss/wildfly/modules/com/mysql/main/

# Create config dir
RUN mkdir -p /bd-ogcmr/uat/cmc/cmc-core/config/cmc
RUN mkdir -p /bd-ogcmr/uat/cmc/cmc-core/cert
RUN mkdir -p /bd-ogcmr/uat/cmc/cmc-adm/config
RUN mkdir -p /bd-ogcmr/uat/cmc/cmc-adm/cert

# Deploy EAR
COPY source/dci-cmc-app/target/dci-cmc-app-3.0.0.ear /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

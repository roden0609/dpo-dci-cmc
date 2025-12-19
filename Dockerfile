FROM quay.io/wildfly/wildfly:32.0.1.Final-jdk17

# Deploy EAR
COPY source/dci-cmc-app/target/dci-cmc-app-3.0.0.ear /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

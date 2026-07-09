<!-- Install libraries to local maven repository  -->
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_parent -Dversion=1.1.0 -Dpackaging=pom -Dfile=lib/gcis_rm3/gcis_rm_parent-1.1.0-pom.xml
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_common -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_rm_common-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_keyservice_ejb -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_rm_keyservice_ejb-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_keyservice_persistence -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_rm_keyservice_persistence-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_keyservice_common -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_rm_keyservice_common-1.1.0.jar

mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_common -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_common-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_msg_soap_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_msg_soap_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_msg_rest_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_msg_rest_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_noti_soap_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_noti_soap_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_noti_rest_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_noti_rest_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_pki_soap_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_pki_soap_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_pki_rest_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_pki_rest_client-1.1.0.jar

mvn install:install-file -DgroupId=net.sf.jasperreports -DartifactId=jasperreports-fonts-patched -Dversion=1.0.0 -Dpackaging=jar -Dfile=lib/cmc/jasperreports-fonts-patched-1.0.0.jar

<!-- Clean and build the EAR -->
mvn -f source/pom.xml clean package

<!-- Run with docker -->
<!-- docker build -t dci-cmc-wildfly .
docker run -p 8080:8080 -p 9990:9990 dci-cmc-wildfly -->

<!-- Rebuild image and start WildFly with compose -->
docker compose up --build
<!-- docker compose up --build | tee docker-compose.log -->

<!-- Generate credential-store.cs -->
docker exec -it dci-cmc-wildfly /opt/jboss/wildfly/bin/jboss-cli.sh --connect
/subsystem=elytron/credential-store=credential-store:add-alias(alias=db-password,secret-value="Ogcio$2468")
/subsystem=elytron/credential-store=credential-store:add-alias(alias=keystore-password,secret-value="password")
/subsystem=elytron/credential-store=credential-store:add-alias(alias=truststore-password,secret-value="changeit")
<!-- /subsystem=elytron/credential-store=credential-store:remove-alias(alias=db-password) -->

<!-- Modify credential-store.cs using elytron-tool.sh -->
./elytron-tool.sh credential-store --remove=db-password --location=/bd-ogcmr/prd/jboss/wildfly-32.0.1.Final/cmc-core/credentials/credential-store.cs
./elytron-tool.sh credential-store --add=db-password --secret='Ogcio$2468' --location=/bd-ogcmr/prd/jboss/wildfly-32.0.1.Final/cmc-core/credentials/credential-store.cs
./elytron-tool.sh credential-store --remove=keystore-password --location=/bd-ogcmr/prd/jboss/wildfly-32.0.1.Final/cmc-core/credentials/credential-store.cs
./elytron-tool.sh credential-store --add=keystore-password --secret='changeit' --location=/bd-ogcmr/prd/jboss/wildfly-32.0.1.Final/cmc-core/credentials/credential-store.cs
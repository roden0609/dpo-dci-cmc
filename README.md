<!-- Install libraries to local maven repository  -->
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_parent -Dversion=1.1.0 -Dpackaging=pom -Dfile=lib/gcis_rm3/gcis_rm_parent-1.1.0-pom.xml
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_common -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_rm_common-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_keyservice_ejb -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_rm_keyservice_ejb-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.rm -DartifactId=gcis_rm_keyservice_common -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_rm_keyservice_common-1.1.0.jar

mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_common -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_common-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_msg_soap_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_msg_soap_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_msg_rest_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_msg_rest_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_noti_soap_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_noti_soap_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_noti_rest_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_noti_rest_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_pki_soap_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_pki_soap_client-1.1.0.jar
mvn install:install-file -DgroupId=hk.gov.gcis.ss -DartifactId=gcis_ss_pki_rest_client -Dversion=1.1.0 -Dpackaging=jar -Dfile=lib/gcis_rm3/gcis_ss_pki_rest_client-1.1.0.jar

<!-- Clean and build the application ear -->
mvn -f source/pom.xml clean package

<!-- Run with docker -->
docker build -t dci-cmc-wildfly .
docker run -p 8080:8080 -p 9990:9990 dci-cmc-wildfly
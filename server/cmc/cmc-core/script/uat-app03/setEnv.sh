#!/bin/sh

export ROOT_PATH=/bd-ogcmr/uat/cmc/cmc-core/script

export JAVA_HOME=/bd-ogcmr/uat/java/jdk
export WILDFLY_HOME=/bd-ogcmr/uat/jboss/wildfly

export LIB=${ROOT_PATH}/lib
export CONFIG=${ROOT_PATH}/config

export PROPERTIES_FILE=${CONFIG}/batchjob.properties

# For Servlet call
export DOMAIN_NAME=172.16.67.13
export HTTP_PORT=19680

# For Housekeep server log
export HOUSEKEEP_PATH_BATCHJOB=/bd-ogcmr/uat/cmc/*/script/log
export FILE_PATTERN_BATCHJOB="batchjob.log.*"
export HOUSEKEEP_PATH_JBOSS=/bd-ogcmr/uat/jboss/wildfly/*/log/
export FILE_PATTERN_JBOSS="server.log.*"

# This script set the exact cutoff day for the housekeeping job
export HOUSEKEEPING_DATE=`date -d "72 month ago" +%Y-%m-01`

CLASSPATH=${WILDFLY_HOME}/bin/client/jboss-client.jar
CLASSPATH=$CLASSPATH:$LIB/dci-cmc-batch-3.0.0.jar
CLASSPATH=$CLASSPATH:$LIB/dci-cmc-core-3.0.0.jar
CLASSPATH=$CLASSPATH:$LIB/dci-cmc-ejb-3.0.0.jar
CLASSPATH=$CLASSPATH:$LIB/gcis_rm_common-1.1.0.jar
CLASSPATH=$CLASSPATH:$LIB/commons-logging-1.3.4.jar
CLASSPATH=$CLASSPATH:$LIB/commons-io-2.16.1.jar
CLASSPATH=$CLASSPATH:$LIB/log4j-core-2.24.1.jar
CLASSPATH=$CLASSPATH:$LIB/log4j-api-2.24.1.jar
CLASSPATH=$CLASSPATH:$LIB/log4j-jul-2.24.1.jar
CLASSPATH=$CLASSPATH:$LIB/log4j-slf4j2-impl-2.24.1.jar

export CLASSPATH
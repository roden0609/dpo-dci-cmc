#!/bin/bash

./jboss-cli.sh controller=172.16.3.14:36990 --connect --command=":shutdown"

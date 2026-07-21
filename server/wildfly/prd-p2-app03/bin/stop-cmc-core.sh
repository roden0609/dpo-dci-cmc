#!/bin/bash

./jboss-cli.sh controller=172.16.131.13:36990 --connect --command=":shutdown"

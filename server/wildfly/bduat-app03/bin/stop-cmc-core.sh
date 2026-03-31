#!/bin/bash

./jboss-cli.sh controller=172.16.67.13:36990 --connect --command=":shutdown"

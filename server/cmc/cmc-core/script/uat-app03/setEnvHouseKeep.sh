#!/bin/sh

# This script set the exact cutoff day for the housekeeping job
HOUSEKEEPING_DATE=`date -d "72 month ago" +%Y-%m-01`

echo Housekeeping date ${HOUSEKEEPING_DATE} is set ~~

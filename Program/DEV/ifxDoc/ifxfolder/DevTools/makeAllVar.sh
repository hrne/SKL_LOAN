#!/bin/bash

for file in /home/weblogic/ifxDoc/ifxfolder/Dev/var/tran/*/*
do
if test -f $file
then
var1=${file##/*/}
var1=${var1%.*}
node gen/make.js $var1
fi
done
#!/bin/bash

F=$1
cat $F | while read L; do
    T=`echo $L | awk '{ print $3 }'`
    echo $T
done
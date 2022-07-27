#!/bin/bash

D=$1
F=$2
find $D -type f | while read O; do
    S=`ls -l $O | awk '{ print $5 }'`
    if [ $S -gt $F ]; then
        echo $O
    fi
done
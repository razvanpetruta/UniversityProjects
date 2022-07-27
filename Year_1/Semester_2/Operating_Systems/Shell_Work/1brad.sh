#!/bin/bash

if [ $# -lt 1 ]; then
    echo "ERROR: provide at least one directory"
    exit 1
fi

T=0
for D in $@; do
    if [ -d $D ]; then
        C=`find $D -type f -name "*.c" | wc -l`
        T=`expr $T + $C`
        echo "$D has $C c files"
    fi
done

echo "Total c files: $T"
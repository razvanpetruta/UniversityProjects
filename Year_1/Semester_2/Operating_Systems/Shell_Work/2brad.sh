#!/bin/bash

if [ $# -lt 3 ]; then
    echo "ERROR: Please input exactly 3 arguments"
    exit 1
fi

W=$1
M=$2
N=$3

shift 3

for F in $@; do
    C=`cat $F | head -n $N | tail -n 1 | grep -E -o "$W" | wc -l`
    if [ $C -ge $M ]; then
        echo "$F"
    fi
done
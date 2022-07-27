#!/bin/bash

if [ $# -lt 3 ]; then
    echo "ERROR: Provide at least 3 parameters"
    exit 1
fi

N=$1
W=$2

shift 2

for F in $@; do
    L=`cat $F | grep -E "$W" | head -n $N | tail -n 1`
    echo "$L"
done
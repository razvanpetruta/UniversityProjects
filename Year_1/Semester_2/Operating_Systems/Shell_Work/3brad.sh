#!/bin/bash

if [ $# -lt 2 ]; then
    echo "ERROR: Provide at least 2 arguments"
    exit 1
fi

W=$1
shift 1

for F in $@; do
    T=`cat $F | grep -E -o "$W" | wc -l`
    echo "$F: $T"
done
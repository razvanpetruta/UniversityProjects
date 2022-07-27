#!/bin/bash

if [ $# -lt 2 ]; then
    echo "ERROR: Provide at least 2 arguments"
    exit 1
fi

N=$1
shift

A=""
for F in $@; do
    while read -r L; do
        if echo "$L" | grep -E -q "^#include"; then
            A="$A$L;"
        fi
    done < $F
done

echo $A | sed "y/;/\n/"
echo $A | sed "y/;/\n/" | sort | uniq -c | sort -n -r | head -n $N
#!/bin/bash

if [ -z "$1" ]; then
    echo "No parameters provided"
    exit 1
fi

if [ ! -d "$1" ]; then
    echo "Parameter is not a folder"
    exit 1
fi

T=0
for F in `find $1 -type f | grep -E "\.c$"`; do
    N=`grep -E -c -v "^[ \t]*$" $F`
    echo "$F: $N lines"
    T=`expr $T + $N`
done

echo "Total lines: $T"
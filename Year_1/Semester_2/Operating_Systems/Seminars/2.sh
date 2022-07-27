#!/bin/bash

if [ -z "$1" ]; then
    echo "No parameters provided"
    exit 1
fi

if [ ! -d "$1" ]; then
    echo "Parameter is not a directory"
fi

T=0
for F in `ls $1 | grep -E "*.c$"`; do
    N=`grep -E -c -v "^[ \t]*$" "$1/$F"`
    echo "$F: $N"
    T=`expr $T + $N`
done

echo "Total lines: $T"
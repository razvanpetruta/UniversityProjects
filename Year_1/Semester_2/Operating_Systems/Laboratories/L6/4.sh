#!/bin/bash

D=$1
S=""

if [ -z $D ]; then
    echo "Error: directory not specified"
    exit 1
fi

if [ ! -d $D ]; then
    echo "Error: not a directory"
    exit 1
fi

while true; do
    Z=""

    for F in `find $D`; do
        if [ -f $F ]; then
            H=`ls -l $F | sha1sum`
            C=`cat $F | sha1sum`
        elif [ -d $F ]; then
            H=`ls -ld $F | sha1sum`
            C=`ls -l $F | sha1sum`
        fi
        Z="$Z\n$H $C"
    done

    if [ -n "$S" ] && [ "$S" != "$Z" ]; then
        echo "some change occurred"
    fi

    S=$Z

    sleep 1
done
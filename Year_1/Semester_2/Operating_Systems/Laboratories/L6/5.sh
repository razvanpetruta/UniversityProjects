#!/bin/bash

DIR=$1
SE=""

if [ -z $DIR ]; then
    echo "Error: directory not specified"
    exit 1
fi

if [ ! -d $DIR ]; then
    echo "Error: not a directory"
    exit 1
fi

while true; do
    ZER=""

    for F in `find $DIR`; do
        if [ -f $F ]; then
            H=`ls -l $F | sha1sum`
            C=`cat $F | sha1sum`
        elif [ -d $F ]; then
            H=`ls -ld $F | sha1sum`
            C=`ls -l $F | sha1sum`
        fi
        ZER="$ZER\n$H $C"
    done

    if [ -n "$SE" ] && [ "$SE" != "$ZER" ]; then
        echo "some change occurred"
    fi

    SE=$ZER

    sleep 1
done
#!/bin/bash

D=$1

if [ -z $D ]; then
    echo "ERROR: No directory provided for monitoring"
    exit 1
fi

if [ ! -d $D ]; then
    echo "ERROR: Directory $D does not exist"
    exit 1
fi

STATE=""
while true; do
    S=""
    for F in `find $D`; do
        if [ -f $F ]; then
            LS=`ls -l $F | sha1sum`
            CONTENT=`cat $F | sha1sum`
        elif [ -d $F ]; then
            LS=`ls -l -d $F | sha1sum`
            CONTENT=`ls -l $F | sha1sum`
        fi
    S="$S\n$LS $CONTENT"
    done
    if [ -n "$STATE" ] && [ "$S" != "$STATE" ]; then
        echo "Directory state changed"
    fi
    STATE=$S
    sleep 1
done
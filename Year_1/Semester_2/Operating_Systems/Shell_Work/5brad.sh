#!/bin/bash

if [ $# -lt 3 ]; then
    echo "ERROR: Provide at least 2 files and one username"
    exit 1
fi

f1="$1"
f2="$2"

shift 2

A=""
for U in $@; do
    D=`cat /etc/passwd | grep -E "^$U"`
    if [ ! -z "$D" ]; then
        H=`echo $D | awk -F: '{print $6}'`
        S=`du -bs $H`
        N=`echo $D | awk -F: '{print $5}'`
        A="$A$S $N;"
    else
        echo $U >> $f1
    fi
done

echo $A
echo $A | sed -E "y/;/\n/" | sort -n -r > $f2
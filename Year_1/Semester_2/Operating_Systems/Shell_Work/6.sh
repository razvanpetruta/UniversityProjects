#!/bin/bash

for X in `ps -ef | grep -E -v "^root " | tail -n +2 | awk '{print $1 ":" $2}'`; do
    U=`echo $X | cut -d: -f1`
    P=`echo $X | cut -d: -f2`

    echo $U ":" $P
    if grep -E "^$U:" /etc/passwd | cut -d: -f6 | grep -E -q "/scs/"; then
        B=`ps -o etime $P | tail -n 1 | sed -E "s/ //g"`
        if echo $B | grep -E -q "^[0-9][0-9]$"; then
            B="00:$B"
        fi
        A=`echo $B | awk -F: '{print ($1*60+$2)}'`
        if [ $A -ge $1 ]; then
            echo "Should kill $U $P $A"
        fi
    fi
done
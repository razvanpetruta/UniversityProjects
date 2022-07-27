#!/bin/bash

for F in $@; do
    if [ -f $F ]; then
        du -b $F
    fi
done | sort -n
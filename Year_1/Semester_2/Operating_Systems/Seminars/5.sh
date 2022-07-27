#!/bin/bash

while true; do
    read -p "Enter filename or stop: " F
    if [ "$F" == "stop" ]; then
        echo Done
        exit 0
    elif [ -f "$F" ]; then
        if file $F | grep -E -q "text"; then
            echo "File: $F contain `head -n 1 $F | wc -w` words"
        fi
    fi
done
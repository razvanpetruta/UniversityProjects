#!/bin/bash

F=""
while [ -z $F ] || [ ! -f $F ] || [ ! -r $F ]; do
    read -p "Provide an existing file that can be readable: " F
done
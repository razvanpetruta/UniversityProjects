#!/bin/bash

#echo "Hello, world!"

echo $@ $#

#echo "arg 1 is $1"
#echo "arg 2 is $2"
#echo "arg 3 is $3"
#echo "arg 4 is $4"

#for A in $@; do
#    echo $A
#done

#while true; do
 #   read X
  #if [ $X == "done" ]; then
   #     break
   # fi
#done

for X in $@; do
    if test -f $X; then
        echo "$X is a file"
    elif test -d $X; then
        echo "$X is a directory"
    else
        echo "I don't know man what is $X"
    fi
done

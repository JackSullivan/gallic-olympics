#!/bin/bash


for i in `seq $1`
do

java -cp target/gaulish-olympics-1.0-SNAPSHOT-jar-with-dependencies.jar so.modernized.runners.SpawnRandomTablet 127.0.0.1 2552 $2 "Rome|Gaul|Carthage" "Curling|Biathlon|Slalom" > tablet-$i-of-$1-at-$2.log &

done

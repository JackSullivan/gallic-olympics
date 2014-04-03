#!/bin/bash

JAR="target/gaulish-olympics-1.0-SNAPSHOT-with-dependencies.jar"


java -classpath target/gaulish-olympics-1.0-SNAPSHOT-jar-with-dependencies.jar so.modernized.OlympicServer "Rome|Gaul|Carthage" "Curling|Biathlon|Piathlon"


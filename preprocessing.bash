#!/bin/bash

# defines dir to look in for files 
files=./images/*.jpg

# number of characters in the filename and path
set -- ${files}
numChar=${#1}
echo $numChar

# counts the number of files in the directory
numberOfFiles=0
numberDeleted=0
for file in $files
do
	echo "Processing ${file}"
	hour=${file:(numChar-9):2}
	if [ $hour -gt 22 ] || [ $hour -lt 4 ]; then
		echo "Nighttime"
	fi
	numberOfFiles=`expr $numberOfFiles + 1`
done


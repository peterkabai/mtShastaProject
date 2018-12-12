#!/bin/bash

# defines dir to look in for files 
files=./images/*.jpg

# sets limits, images with hours between these will be kept
lowLimitHours=5
highLimitHours=22

# user confirmation before deleting images
echo "This will delete all images before $lowLimitHours:00 and after $highLimitHours:00"
read -p "Continue anyway (y/n)? " cont
if [ "$cont" != "y" ]
then
  echo "Ok, exiting."
  exit
fi
echo "Ok, continuing..."

# gets the number of characters in the filename and path
set -- ${files}
numChar=${#1}

# counts the number of files in the directory
numberOfFiles=0
numberDeleted=0
for file in $files
do
	hour=${file:(numChar-9):2}
	if [ $hour -gt $highLimitHours ] || [ $hour -lt $lowLimitHours ]
	then
		rm "$file"
		numberDeleted=`expr $numberDeleted + 1`
	fi
	numberOfFiles=`expr $numberOfFiles + 1`
done

# user feedback
echo "Done!"
echo $numberDeleted" of "$numberOfFiles" images deleted."

#!/bin/bash

# defines dir to look in for files 
files=./images/*.jpg

# sets range, images with hours between these will be kept
lowLimitHours=5
highLimitHours=21
widthToResize=480

# user confirmation before deleting images
echo "This will delete all images before $lowLimitHours:00 and after `expr $highLimitHours-1`:59"
read -p "Continue anyway (y/n)? " cont
if [ "$cont" != "y" ]
then
  echo "Ok, exiting."
  exit
fi
echo "Ok, continuing..."

# counts the number of files in the directory
numberOfFiles=0
numberDeleted=0
numberResized=0
for file in $files
do
	# gets the hour of the image
	hour="`echo $file | tail -c 10 | head -c 2`"
	
	# if the hour is outside the specified hour range
	if [ $hour -gt $highLimitHours ] || [ $hour -lt $lowLimitHours ]
	then
		# removes the file
		rm "$file"
		numberDeleted=`expr $numberDeleted + 1`
	else
		fileInfo=`file "$file"`
		width="`echo $fileInfo | tail -c 18 | head -c 3`"
		if [ $width != $widthToResize ]
		then
			# resizes the image, may only work on mac
			sips -Z $widthToResize "$file" &>/dev/null &disown
			numberResized=`expr $numberResized + 1`
		fi
	fi
	if [ $(($numberOfFiles%100)) == 0 ] && [ $numberOfFiles != 0 ]
	then
		echo $numberOfFiles" images processed..."
	fi
	numberOfFiles=`expr $numberOfFiles + 1`
done

# user feedback
echo "Done!"
echo $numberDeleted" of "$numberOfFiles" images deleted."
echo $numberResized" of "$numberOfFiles" images resized."

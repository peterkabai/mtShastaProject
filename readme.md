# Mt. Shasta Project
An CNN in TensorFlow using over 50,000 images of Mt. Shasta, in Northern California.

## Purpose:
To classify over 50,000 still images of Mt. Shasta find practical uses for such a thing.

## Background:
In May of 2018 I climbed Mt. Shasta, and found a live webcam of the mountain to check the live weather conditions. I set up a cron job to save the images, and now have many tens of thousands of images of the same location. It sounds like a great dataset to work with!

## Data Acquisition:  
Between April 2018 and April 2019 I will be collecting still images of Mt. Shasta in Northern California from a live webcam every 10 minute. One image every ten minutes is 52,560 images over a whole year. About half will have been in the dark, and many will be on cloudy days, when the mountain is obscured. To sort out the nighttime images I run a shell script to toss out all images before 5am and after 10pm. 

## Creating a Training Set:  
To create a training set, I'm building a Java application that allows the user to sort random images from one directory into multiple separate directories using only arrow keys. This speeds up the process of labeling images for training data.

## The CNN:  
The CNN will use Keras, and take the training images that have been separated into categories in unique directories. Some possible categories are 'Clear', 'Slightly Cloudy', 'Cloudy', 'Smokey' and 'Nighttime'.

## The Final Product:  
Once the images are sorted and a model has been developed, there are several useful things that can be done with the data.
* Use the 'Clear' images and assemble a time laps video to span an entire year, and show the changing snow-line.
* Connect the CNN model to an Alexa app, and ask Alexa to tell me the current conditions on Mt. Shasta.

<?php

// this file saves the images of Mt. Shasta every 10 minutes using a cronjob

// gets the date in a specific format
date_default_timezone_set('America/Los_Angeles');
$nextName = rtrim(date("Y-m-d, H-i"),".jpg");

// saves the image using the generated date as a filename
copy('https://www.snowcrest.net/camera/snowcam-high000M.jpg', 'frames/'.$nextName.'.jpg');

?>

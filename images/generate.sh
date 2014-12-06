#!/bin/bash

filename=$1
if [ "$filename" == "" ];then
    echo No file typed
else
    mdpi_size="-resize 48X48"
    hdpi_size="-resize 72X72"
    xhdpi_size="-resize 96X96"
    xxhdpi_size="-resize 144X144"

    mdpi_filename="../iobber/src/main/res/drawable-mdpi/$filename"
    hdpi_filename="../iobber/src/main/res/drawable-hdpi/$filename"
    xhdpi_filename="../iobber/src/main/res/drawable-xhdpi/$filename"
    xxhdpi_filename="../iobber/src/main/res/drawable-xxhdpi/$filename"

    convert $mdpi_size $filename $mdpi_filename
    git add $mdpi_filename

    convert $hdpi_size $filename $hdpi_filename
    git add $hdpi_filename

    convert $xhdpi_size $filename $xhdpi_filename
    git add $xhdpi_filename

    convert $xxhdpi_size $filename $xxhdpi_filename
    git add $xxhdpi_filename

    echo "Done!"
fi
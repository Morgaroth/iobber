#!/bin/bash

filename=$1

mdpi_size="-resize 48X48"
hdpi_size="-resize 72X72"
xhdpi_size="-resize 96X96"
xxhdpi_size="-resize 144X144"

convert $mdpi_size $filename "../iobber/src/main/res/drawable-mdpi/$filename"
convert $hdpi_size $filename "../iobber/src/main/res/drawable-hdpi/$filename"
convert $xhdpi_size $filename "../iobber/src/main/res/drawable-xhdpi/$filename"
convert $xxhdpi_size $filename "../iobber/src/main/res/drawable-xxhdpi/$filename"

echo "Done!"
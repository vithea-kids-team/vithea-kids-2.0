#!/bin/bash

# local vars
ROOT=`pwd`

# build VITHEA-Kids for production
cd client
ng build --e=prod --t=production
# change production names to sensible names
cd dist
# .js
find . -type f -name "inline*.bundle.js" -print0 | xargs -0 -I {} mv {} inline.bundle.js
find . -type f -name "main*.bundle.js" -print0 | xargs -0 -I {} mv {} main.bundle.js
find . -type f -name "scripts*.bundle.js" -print0 | xargs -0 -I {} mv {} scripts.bundle.js
find . -type f -name "vendor*.bundle.js" -print0 | xargs -0 -I {} mv {} vendor.bundle.js
# .css
find . -type f -name "styles*.bundle.css" -print0 | xargs -0 -I {} mv {} styles.bundle.css
# clean old production
cd "$ROOT"
cd server/public
rm -rf *

# clean local development data
cd "$ROOT"
cd client/dist/vithea-kids/assets/images
find . -type f -not -name 'VITHEAkidsLogo.png' -print0 | xargs -0 rm --
rm animatedcharacter/*
rm reinforcement/*
rm stimuli/*

# copy assets to server folder
cd "$ROOT"
cp -r client/dist/vithea-kids/assets/* server/public/

# copy the remaining resources
cp -r client/dist/* server/public/

# build with sbt
cd server
sbt dist

# extract sbt output
cd target/universal/
rm -rf vithea-kids-2.0
unzip vithea-kids-2.0.zip -d .

# rename database
cd "$ROOT"
cd server/conf
sed -i '.bkp' "s/vithea_KIDS?useSSL/vithea_KIDS_2?useSSL/g" application.conf
rm -f application.conf.bkp

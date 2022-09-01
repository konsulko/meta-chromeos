#!/bin/bash
#rm -rf *.inc
find . -name '*.inc' ! -name 'chromiumos-platform-cr50.inc' -type f -exec rm -f {} +
rm -rf /tmp/chromiumos-platform-*
BRANCH="main"
BASE_URL="https://chromium.googlesource.com/"
MANIFEST_PATH="$1"
lines=$(cat ${MANIFEST_PATH} | grep -Po '(?<=")chromiumos/platform/.*(?=")')
for line in $lines; do
	base="$(basename $line)"
	basename="$(echo $line | tr '/' '-')"
	filename="${basename}.inc"
	URL="${BASE_URL}$line.git"
	tmpdir="/tmp/$basename)"
	git clone -b ${BRANCH} --single-branch ${URL} --depth=3 ${tmpdir}
	cwd=$PWD
	cd $tmpdir
	SHA=$(git rev-parse HEAD)
	cd $cwd
	cat > $filename << EOF
# Generated file DO NOT EDIT
# Sources for $line

SRC_URI += "git://chromium.googlesource.com/$line;protocol=https;branch=${BRANCH};destsuffix=src/platform/$base;name=$basename"
SRCREV_$basename = "${SHA}"
EOF
done
includes="$(ls *.inc)"
cat >> chromiumos-platform.inc << EOF
# Generated file DO NOT EDIT
# Sources for chromiumos/platform/*

EOF
for include in $includes; do
	cat >> chromiumos-platform.inc << EOF
require recipes-chromeos/chromiumos-platform/${include}
EOF
done
rm -rf /tmp/chromiumos-platform-*

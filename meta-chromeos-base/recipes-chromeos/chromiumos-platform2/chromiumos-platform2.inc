# Sources for chromiumos/platform2

FILESEXTRAPATHS:prepend := "${CHROMEOS_BASE}/meta-chromeos-base/recipes-chromeos/chromiumos-platform2/files:"

SRC_URI += "git://chromium.googlesource.com/chromiumos/platform2;protocol=https;branch=main;destsuffix=src/platform2;name=chromiumos-platform2"
#SRC_URI += "file://0001-chaps-fix-path-for-nss-pkcs11.h.patch"

SRCREV_chromiumos-platform2 = "f1ddc0a40695445f5c3fb186beccc06851116398"

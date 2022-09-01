SUMMARY = "Chrome OS Firmware (Template - change to board name)"
DESCRIPTION = "Chrome OS Firmware (Template - change to board name)"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/firmware/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-firmware.inc

S = "/src/platform/firmware"
B = "${WORKDIR}/build"
PR = "r174"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


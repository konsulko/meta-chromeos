SUMMARY = "HPS firmware tools for development and testing"
DESCRIPTION = "HPS firmware tools for development and testing"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/hps-firmware"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-hps-firmware.inc

S = "/src/platform/hps-firmware"
B = "${WORKDIR}/build"
PR = "r166"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


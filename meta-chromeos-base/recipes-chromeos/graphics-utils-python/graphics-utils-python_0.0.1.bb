SUMMARY = "Graphics utilities written in python"
DESCRIPTION = "Graphics utilities written in python"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/graphics/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-graphics.inc

S = "/src/platform/graphics"
B = "${WORKDIR}/build"
PR = "r6"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


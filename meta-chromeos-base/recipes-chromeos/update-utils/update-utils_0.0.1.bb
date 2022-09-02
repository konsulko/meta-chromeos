SUMMARY = "A set of utilities for updating Chrome OS."
DESCRIPTION = "A set of utilities for updating Chrome OS."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/dev-util/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-dev-util.inc

S = "/src/platform/dev-util"
B = "${WORKDIR}/build"
PR = "r63"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


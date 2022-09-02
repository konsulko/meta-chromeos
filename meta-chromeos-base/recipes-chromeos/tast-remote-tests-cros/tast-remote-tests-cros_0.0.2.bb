SUMMARY = "Bundle of remote integration tests for Chrome OS"
DESCRIPTION = "Bundle of remote integration tests for Chrome OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/tast-tests/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-tast-tests.inc

S = "/src/platform/tast-tests"
B = "${WORKDIR}/build"
PR = "r11836"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


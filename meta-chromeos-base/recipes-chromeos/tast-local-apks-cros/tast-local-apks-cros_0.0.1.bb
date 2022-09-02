SUMMARY = "Compiled apks used by local Tast tests in the cros bundle"
DESCRIPTION = "Compiled apks used by local Tast tests in the cros bundle"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/tast-tests/+/master/android"
LICENSE = "BSD-3-Clause & GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-tast-tests.inc

S = "/src/platform/tast-tests"
B = "${WORKDIR}/build"
PR = "r176"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


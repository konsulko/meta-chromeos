SUMMARY = "Test tool that recovers bricked Chromium OS test devices"
DESCRIPTION = "Test tool that recovers bricked Chromium OS test devices"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/crostestutils/+/master/recover_duts/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-crostestutils.inc

S = "/src/platform/crostestutils"
B = "${WORKDIR}/build"
PR = "r422"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "vboot tests"
DESCRIPTION = "vboot tests"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/vboot_reference/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-vboot_reference.inc

S = "/src/platform/vboot_reference"
B = "${WORKDIR}/build"
PR = "r2161"

GN_ARGS += 'platform_subdir="../platform/${CHROMEOS_PN}"'


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


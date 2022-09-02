SUMMARY = "This ebuild defines the rust dependencies used in factory_installer dir."
DESCRIPTION = "This ebuild defines the rust dependencies used in factory_installer dir."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/factory_installer/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-factory_installer.inc

S = "/src/platform/factory_installer"
B = "${WORKDIR}/build"
PR = "r4"

GN_ARGS += 'platform_subdir="../platform/${CHROMEOS_PN}"'


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


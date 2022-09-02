SUMMARY = "Universal Stylus Initiative (USI) Certification Tool"
DESCRIPTION = "Universal Stylus Initiative (USI) Certification Tool"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/usi-test/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-${BPN}.inc

S = "${WORKDIR}/src/platform/${BPN}"
B = "${WORKDIR}/build"
PR = "r5"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


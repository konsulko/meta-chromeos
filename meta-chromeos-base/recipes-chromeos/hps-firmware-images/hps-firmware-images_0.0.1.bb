SUMMARY = "Chrome OS HPS firmware"
DESCRIPTION = "Chrome OS HPS firmware"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/hps-firmware-images/+/HEAD"
LICENSE = "BSD-Google BSD-2 Apache-2.0 MIT 0BSD BSD ISC"
LICENSE = "BSD-3-Clause & BSD-2-Clause & Apache-2.0 & MIT & 0BSD & ISC"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"


inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-${BPN}.inc

S = "${WORKDIR}/src/platform/${BPN}"
B = "${WORKDIR}/build"
PR = "r40"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


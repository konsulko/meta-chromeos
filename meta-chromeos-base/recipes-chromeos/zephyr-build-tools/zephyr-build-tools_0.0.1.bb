SUMMARY = "Tools used for building Zephyr OS"
DESCRIPTION = "Tools used for building Zephyr OS"
HOMEPAGE = "http://src.chromium.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r208"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


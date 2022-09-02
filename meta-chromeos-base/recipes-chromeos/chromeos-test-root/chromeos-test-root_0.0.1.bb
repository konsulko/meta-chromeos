SUMMARY = "Install packages that must live in the rootfs in test images"
DESCRIPTION = "Install packages that must live in the rootfs in test images"
HOMEPAGE = "http://www.chromium.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r12"


do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


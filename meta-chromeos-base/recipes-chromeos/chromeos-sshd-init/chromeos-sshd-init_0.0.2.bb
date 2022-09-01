SUMMARY = "Install Chromium OS sshd Upstart file to a shared location."
DESCRIPTION = "Install Chromium OS sshd Upstart file to a shared location."
HOMEPAGE = "http://www.chromium.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r3"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


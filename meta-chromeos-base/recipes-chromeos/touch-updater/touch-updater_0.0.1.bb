SUMMARY = "Touch firmware and config updater"
DESCRIPTION = "Touch firmware and config updater"
HOMEPAGE = "https://www.chromium.org/chromium-os"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r248"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


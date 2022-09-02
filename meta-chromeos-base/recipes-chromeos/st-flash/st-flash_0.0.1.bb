SUMMARY = "STM32 IAP firmware updater for Chrome OS touchpads"
DESCRIPTION = "STM32 IAP firmware updater for Chrome OS touchpads"
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"
PR = "r1366"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


SUMMARY = "Wrapper for etphidiap touch firmware updater."
DESCRIPTION = "Wrapper for etphidiap touch firmware updater."
HOMEPAGE = "https://www.chromium.org/chromium-os"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r17"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


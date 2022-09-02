SUMMARY = "Server to cache Chromium OS build artifacts from Google Storage."
DESCRIPTION = "Server to cache Chromium OS build artifacts from Google Storage."
HOMEPAGE = "http://dev.chromium.org/chromium-os/how-tos-and-troubleshooting/using-the-dev-server"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r1415"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


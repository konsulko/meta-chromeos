SUMMARY = "Chromium OS Developer Packages installer"
DESCRIPTION = "Chromium OS Developer Packages installer"
HOMEPAGE = "http://dev.chromium.org/chromium-os/how-tos-and-troubleshooting/install-software-on-base-images"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r1338"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


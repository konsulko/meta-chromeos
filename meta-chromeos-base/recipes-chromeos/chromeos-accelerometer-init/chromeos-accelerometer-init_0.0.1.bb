SUMMARY = "Chrome OS trigger allowing chrome to access cros-ec-accel device"
DESCRIPTION = "Chrome OS trigger allowing chrome to access cros-ec-accel device"
HOMEPAGE = "https://dev.chromium.org/chromium-os"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r29"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


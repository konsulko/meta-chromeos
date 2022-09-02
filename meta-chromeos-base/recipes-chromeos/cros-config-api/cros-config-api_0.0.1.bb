SUMMARY = "Provides python and go bindings to the config API"
DESCRIPTION = "Provides python and go bindings to the config API"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/config/+/master/python/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r417"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


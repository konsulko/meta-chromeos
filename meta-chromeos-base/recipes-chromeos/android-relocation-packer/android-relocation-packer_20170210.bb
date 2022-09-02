SUMMARY = "Tool for packed relocations in Android."
DESCRIPTION = "Tool for packed relocations in Android."
HOMEPAGE = "https://android.googlesource.com/platform/bionic/"
LICENSE = "Apache-2.0 & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


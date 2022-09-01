SUMMARY = "Chrome OS support utils for Android Neural Network API"
DESCRIPTION = "Chrome OS support utils for Android Neural Network API"
HOMEPAGE = "https://developer.android.com/ndk/guides/neuralnetworks"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r7"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


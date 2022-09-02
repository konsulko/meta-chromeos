SUMMARY = "HAL / Driver Vendor and Compatability Test Tools for NNAPI"
DESCRIPTION = "HAL / Driver Vendor and Compatability Test Tools for NNAPI"
HOMEPAGE = "https://developer.android.com/ndk/guides/neuralnetworks"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r174"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


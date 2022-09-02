SUMMARY = "Compilation and runtime tests for toolchain"
DESCRIPTION = "Compilation and runtime tests for toolchain"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/autotest/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r6777"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


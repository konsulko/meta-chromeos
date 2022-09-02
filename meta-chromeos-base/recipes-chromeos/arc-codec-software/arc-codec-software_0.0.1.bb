SUMMARY = "Install software codec configurations on ARC++"
DESCRIPTION = "Install software codec configurations on ARC++"
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"
PR = "r3"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


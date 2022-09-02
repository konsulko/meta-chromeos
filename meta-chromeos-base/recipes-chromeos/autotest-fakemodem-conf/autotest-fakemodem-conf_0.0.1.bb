SUMMARY = "Runtime configuration file for fakemodem (autotest dep)"
DESCRIPTION = "Runtime configuration file for fakemodem (autotest dep)"
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"
PR = "r1"

GN_ARGS += 'platform_subdir="${BPN}"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


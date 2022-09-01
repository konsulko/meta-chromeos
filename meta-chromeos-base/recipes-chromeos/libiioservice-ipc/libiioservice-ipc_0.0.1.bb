SUMMARY = "Chrome OS sensor HAL IPC util."
DESCRIPTION = "Chrome OS sensor HAL IPC util."
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"
PR = "r318"

GN_ARGS += 'platform_subdir="../platform/${CHROMEOS_PN}"'


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


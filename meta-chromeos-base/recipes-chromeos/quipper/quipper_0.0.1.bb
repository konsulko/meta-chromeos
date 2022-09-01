SUMMARY = "quipper: chromiumos wide profiling"
DESCRIPTION = "quipper: chromiumos wide profiling"
HOMEPAGE = "http://www.chromium.org/chromium-os/profiling-in-chromeos"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r2788"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


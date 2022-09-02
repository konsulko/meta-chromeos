SUMMARY = "Runner for local integration tests"
DESCRIPTION = "Runner for local integration tests"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/tast/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-tast.inc

S = "/src/platform/tast"
B = "${WORKDIR}/build"
PR = "r1580"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


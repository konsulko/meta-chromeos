SUMMARY = "TPM Light Command Library testsuite"
DESCRIPTION = "TPM Light Command Library testsuite"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/tpm_lite/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "tpm_lite"
require recipes-chromeos/chromiumos-platform/chromiumos-platform-${CHROMEOS_PN}.inc

S = "${WORKDIR}/src/platform/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r16"

GN_ARGS += 'platform_subdir="../platform/${CHROMEOS_PN}"'


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


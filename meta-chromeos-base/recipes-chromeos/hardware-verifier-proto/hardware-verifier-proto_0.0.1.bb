SUMMARY = "Hardware Verifier go proto for Chrome OS"
DESCRIPTION = "Hardware Verifier go proto for Chrome OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/hardware_verifier/proto"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "hardware_verifier_proto"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r379"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


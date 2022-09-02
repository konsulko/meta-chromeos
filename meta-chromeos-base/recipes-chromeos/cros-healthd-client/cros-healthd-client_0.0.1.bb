SUMMARY = "Chrome OS Mojo client for cros_healthd"
DESCRIPTION = "Chrome OS Mojo client for cros_healthd"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/diagnostics/mojo"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "cros_healthd-client"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r152"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


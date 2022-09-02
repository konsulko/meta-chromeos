SUMMARY = "Exported headers from the embedded controller codebase."
DESCRIPTION = "Exported headers from the embedded controller codebase."
HOMEPAGE = "https://www.chromium.org/chromium-os/ec-development"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

require recipes-chromeos/chromiumos-platform/chromiumos-platform-ec.inc
require recipes-chromeos/chromiumos-platform/chromiumos-platform-cr50.inc

PR = "r9114"

do_install() {
    dir_ec="${WORKDIR}/src/platform/ec"
    dir_cr50="${WORKDIR}/src/platform/cr50"

    CR50_INSTALL_DIR="${D}${includedir}/trunks/cr50_headers"
    install -d ${CR50_INSTALL_DIR}
    install -m 0644 ${dir_cr50}/include/pinweaver_types.h ${CR50_INSTALL_DIR}/
    install -m 0644 ${dir_cr50}/include/u2f.h ${CR50_INSTALL_DIR}/
    install -m 0644 ${dir_cr50}/board/cr50/tpm2/virtual_nvmem.h ${CR50_INSTALL_DIR}/

    EC_INSTALL_DIR="${D}${includedir}/chromeos/ec"
    install -d ${EC_INSTALL_DIR}
    install -m 0644 ${dir_ec}/include/ec_commands.h ${EC_INSTALL_DIR}/
    install -m 0644 ${dir_ec}/util/cros_ec_dev.h ${EC_INSTALL_DIR}/
}


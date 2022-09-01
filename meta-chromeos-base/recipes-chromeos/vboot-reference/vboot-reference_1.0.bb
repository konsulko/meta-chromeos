SUMMARY = "Chrome OS verified boot tools"
DESCRIPTION = "Chrome OS verified boot tools"
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
CHROMEOS_PN = "vboot_reference"
require recipes-chromeos/chromiumos-platform/chromiumos-platform-${CHROMEOS_PN}.inc

# OpenSSL 3.0 patches for reference/future work
# file://0001-vboot_reference-futility-cmd_show-port-to-openssl-3.patch
# file://0002-vboot_reference-futility-cmd_create-port-to-openssl-3.patch
# file://0003-vboot_reference-futility-vb2_helper-port-to-openssl-3.patch
# file://0004-vboot_reference-futility-vb1_helper-port-to-openssl-3.patch
# file://0005-vboot_reference-host-lib-util_misc-port-to-openssl-3.patch
# file://0006-vboot_reference-host-lib21-port-to-openssl-3.patch
# file://0007-vboot_reference-futility-cmd_gscvd-port-to-openssl-3.patch
# file://0008-vboot_reference-host-lib-signature_digest-port-to-openssl-3.patch
# file://0009-vboot_reference-host-lib-host_key2-port-to-openssl-3.patch
# file://0010-vboot_reference-host-lib-host_signature2-port-to-openssl-3.patch
# file://0011-vboot_reference-utility-port-to-openssl-3.patch

S = "${WORKDIR}/src/platform/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r2303"

DEPENDS += "bash crosid libarchive libzip openssl coreboot-utils flashrom util-linux"

RDEPENDS:${PN}:append = " bash"

PACKAGECONFIG ??= ""

# Description of all the possible PACKAGECONFIG fields (comma delimited):
# 1. Extra arguments that should be added to the configure script argument list (EXTRA_OECONF or PACKAGECONFIG_CONFARGS) if the feature is enabled.
# 2. Extra arguments that should be added to EXTRA_OECONF or PACKAGECONFIG_CONFARGS if the feature is disabled.
# 3. Additional build dependencies (DEPENDS) that should be added if the feature is enabled.
# 4. Additional runtime dependencies (RDEPENDS) that should be added if the feature is enabled.
# 5. Additional runtime recommendations (RRECOMMENDS) that should be added if the feature is enabled.
# 6. Any conflicting (that is, mutually exclusive) PACKAGECONFIG settings for this feature.

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches,
# command-line switches and dependencies.
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[dev_debug_force] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[pd_sync] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpmtests] = ""
PACKAGECONFIG[tpm] = ""
PACKAGECONFIG[tpm_dynamic] = ""
PACKAGECONFIG[tpm2] = ""
PACKAGECONFIG[tpm2_simulator] = ""
PACKAGECONFIG[vtpm_proxy] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        dev_debug_force=${@bb.utils.contains('PACKAGECONFIG', 'dev_debug_force', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        pd_sync=${@bb.utils.contains('PACKAGECONFIG', 'pd_sync', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpmtests=${@bb.utils.contains('PACKAGECONFIG', 'tpmtests', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm_dynamic=${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
        tpm2_simulator=${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator', 'true', 'false', d)} \
        vtpm_proxy=${@bb.utils.contains('PACKAGECONFIG', 'vtpm_proxy', 'true', 'false', d)} \
    } \
'

do_configure() {
    :
}

do_compile() {
    cd ${S}
    
    oe_runmake BUILD=${B} all
}

export LDLIBS

do_install() {
    cd ${S}
    oe_runmake BUILD=${B} DESTDIR=${D} install install_dev

    install -d ${D}${libdir}/pkgconfig
    sed -e 's,@LIBDIR@,${baselib},g' \
        -e "s,@LDLIBS@,${LDLIBS},g" \
        ${S}/vboot_host.pc.in > ${B}/vboot_host.pc
    install -m 0644 ${B}/vboot_host.pc ${D}${libdir}/pkgconfig/
}

FILES:${PN} += "${datadir}/vboot/*"

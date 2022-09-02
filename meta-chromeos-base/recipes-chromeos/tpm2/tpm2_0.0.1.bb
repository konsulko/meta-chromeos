SUMMARY = "TPM2.0 library"
DESCRIPTION = "TPM2.0 library"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/tpm2/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

SRC_URI = "git://chromium.googlesource.com/chromiumos/third_party/tpm2;branch=main;protocol=https"
SRCREV = "2de0a64491451f72c7ffe5eb92301f4da509d0ad"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
PR = "r146"

DEPENDS += "openssl"

PACKAGECONFIG ??= "tpm2_simulator"

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
PACKAGECONFIG[generic_tpm2] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpm2_simulator] = ""
PACKAGECONFIG[tpm2_simulator_manufacturer] = ""

GN_ARGS += ' \
    use={ \
        generic_tpm2=${@bb.utils.contains('PACKAGECONFIG', 'generic_tpm2', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm2_simulator=${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator', 'true', 'false', d)} \
        tpm2_simulator_manufacturer=${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator_manufacturer', 'true', 'false', d)} \
    } \
'

EXTRA_OEMAKE = "'CC=${CC}' 'RANLIB=${RANLIB}' 'AR=${AR}' 'obj=${B}'"
do_compile() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator_manufacturer', 'true', 'false', d)}; then
        export TPM2_SIMULATOR_MANUFACTURER=1
    fi
    if ${@bb.utils.contains('PACKAGECONFIG', 'generic_tpm2', 'true', 'false', d)}; then
        export TCG_EK_CERT_INDICES=1
    fi
    oe_runmake -C ${S}
}

do_install() {
    install -d ${D}${libdir}
    install -m 0644 ${B}/libtpm2.a ${D}${libdir}

    install -d ${D}${libdir}/pkgconfig
    sed -e "s/@BSLOT@/${PV}/g" ${S}/libtpm2.pc.in > ${B}/libtpm2.pc
    install -m 0644 ${B}/libtpm2.pc ${D}${libdir}/pkgconfig/

    install -d ${D}${includedir}/tpm2
    install -m 0644 ${S}/BaseTypes.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/Capabilities.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/ExecCommand_fp.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/GetCommandCodeString_fp.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/Implementation.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/Manufacture_fp.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/Platform.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/TPMB.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/TPM_Types.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/Tpm.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/TpmBuildSwitches.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/TpmError.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/_TPM_Init_fp.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/bool.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/swap.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/tpm_generated.h ${D}${includedir}/tpm2/
    install -m 0644 ${S}/tpm_types.h ${D}${includedir}/tpm2/

    if ${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} || \
       ${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator', 'true', 'false', d)} ; then
        install -m 0644 ${S}/tpm_manufacture.h ${D}${includedir}/tpm2/
	install -m 0644 ${S}/tpm_simulator.hpp ${D}${includedir}/tpm2/
    fi
}


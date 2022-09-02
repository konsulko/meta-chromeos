SUMMARY = "Library for sealing data to device identity and state"
DESCRIPTION = "Library for sealing data to device identity and state"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/sealed_storage"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "sealed_storage"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r581"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'

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
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpm2] = ""

GN_ARGS += ' \
    use={ \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


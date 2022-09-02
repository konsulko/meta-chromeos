SUMMARY = "Infineon TPM firmware updater"
DESCRIPTION = "Infineon TPM firmware updater"
HOMEPAGE = ""
LICENSE = "BSD-Infineon & LICENSE.infineon-firmware-updater-TCG"

PR = "r34"

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
PACKAGECONFIG[tpm_slb9655_v4_31] = ""

GN_ARGS += ' \
    use={ \
        tpm_slb9655_v4_31=${@bb.utils.contains('PACKAGECONFIG', 'tpm_slb9655_v4_31', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


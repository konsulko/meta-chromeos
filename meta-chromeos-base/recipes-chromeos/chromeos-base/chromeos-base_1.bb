SUMMARY = "ChromeOS specific system setup"
DESCRIPTION = "ChromeOS specific system setup"
HOMEPAGE = "https://dev.chromium.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r13"

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
PACKAGECONFIG[ac_only] = ""
PACKAGECONFIG[chromeless_tty] = ""
PACKAGECONFIG[cros_embedded] = ""
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[pam] = ""
PACKAGECONFIG[vtconsole] = ""
PACKAGECONFIG[kernel4_4] = ""
PACKAGECONFIG[kernel3_18] = ""

GN_ARGS += ' \
    use={ \
        ac_only=${@bb.utils.contains('PACKAGECONFIG', 'ac_only', 'true', 'false', d)} \
        chromeless_tty=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tty', 'true', 'false', d)} \
        cros_embedded=${@bb.utils.contains('PACKAGECONFIG', 'cros_embedded', 'true', 'false', d)} \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        pam=${@bb.utils.contains('PACKAGECONFIG', 'pam', 'true', 'false', d)} \
        vtconsole=${@bb.utils.contains('PACKAGECONFIG', 'vtconsole', 'true', 'false', d)} \
        kernel4_4=${@bb.utils.contains('PACKAGECONFIG', 'kernel4_4', 'true', 'false', d)} \
        kernel3_18=${@bb.utils.contains('PACKAGECONFIG', 'kernel3_18', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "Meta ebuild for all packages providing tests"
DESCRIPTION = "Meta ebuild for all packages providing tests"
HOMEPAGE = "http://www.chromium.org"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r57"

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
PACKAGECONFIG[cheets] = ""
PACKAGECONFIG[chromeless_tests] = ""
PACKAGECONFIG[bluetooth] = ""
PACKAGECONFIG[cellular] = ""
PACKAGECONFIG[cras] = ""
PACKAGECONFIG[debugd] = ""
PACKAGECONFIG[chromeless_tty] = ""
PACKAGECONFIG[kvm_host] = ""
PACKAGECONFIG[power_management] = ""
PACKAGECONFIG[shill] = ""
PACKAGECONFIG[tpm] = ""
PACKAGECONFIG[tpm2] = ""

GN_ARGS += ' \
    use={ \
        cheets=${@bb.utils.contains('PACKAGECONFIG', 'cheets', 'true', 'false', d)} \
        chromeless_tests=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tests', 'true', 'false', d)} \
        bluetooth=${@bb.utils.contains('PACKAGECONFIG', 'bluetooth', 'true', 'false', d)} \
        cellular=${@bb.utils.contains('PACKAGECONFIG', 'cellular', 'true', 'false', d)} \
        cras=${@bb.utils.contains('PACKAGECONFIG', 'cras', 'true', 'false', d)} \
        debugd=${@bb.utils.contains('PACKAGECONFIG', 'debugd', 'true', 'false', d)} \
        chromeless_tty=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tty', 'true', 'false', d)} \
        kvm_host=${@bb.utils.contains('PACKAGECONFIG', 'kvm_host', 'true', 'false', d)} \
        power_management=${@bb.utils.contains('PACKAGECONFIG', 'power_management', 'true', 'false', d)} \
        shill=${@bb.utils.contains('PACKAGECONFIG', 'shill', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


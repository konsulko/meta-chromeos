SUMMARY = "Autotest tests"
DESCRIPTION = "Autotest tests"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/autotest/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r8986"

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
PACKAGECONFIG[arccamera3] = ""
PACKAGECONFIG[biod] = ""
PACKAGECONFIG[chromeless_tests] = ""
PACKAGECONFIG[chromeless_tty] = ""
PACKAGECONFIG[crash_reporting] = ""
PACKAGECONFIG[cups] = ""
PACKAGECONFIG[dlc_test] = ""
PACKAGECONFIG[encrypted_stateful] = ""
PACKAGECONFIG[network_time] = ""
PACKAGECONFIG[passive_metrics] = ""
PACKAGECONFIG[profile] = ""
PACKAGECONFIG[vaapi] = ""

GN_ARGS += ' \
    use={ \
        arccamera3=${@bb.utils.contains('PACKAGECONFIG', 'arccamera3', 'true', 'false', d)} \
        biod=${@bb.utils.contains('PACKAGECONFIG', 'biod', 'true', 'false', d)} \
        chromeless_tests=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tests', 'true', 'false', d)} \
        chromeless_tty=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tty', 'true', 'false', d)} \
        crash_reporting=${@bb.utils.contains('PACKAGECONFIG', 'crash_reporting', 'true', 'false', d)} \
        cups=${@bb.utils.contains('PACKAGECONFIG', 'cups', 'true', 'false', d)} \
        dlc_test=${@bb.utils.contains('PACKAGECONFIG', 'dlc_test', 'true', 'false', d)} \
        encrypted_stateful=${@bb.utils.contains('PACKAGECONFIG', 'encrypted_stateful', 'true', 'false', d)} \
        network_time=${@bb.utils.contains('PACKAGECONFIG', 'network_time', 'true', 'false', d)} \
        passive_metrics=${@bb.utils.contains('PACKAGECONFIG', 'passive_metrics', 'true', 'false', d)} \
        profile=${@bb.utils.contains('PACKAGECONFIG', 'profile', 'true', 'false', d)} \
        vaapi=${@bb.utils.contains('PACKAGECONFIG', 'vaapi', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


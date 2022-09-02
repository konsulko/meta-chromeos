SUMMARY = "Security autotests"
DESCRIPTION = "Security autotests"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/autotest/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r3319"

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
PACKAGECONFIG[autotest] = ""
PACKAGECONFIG[chromeless_tests] = ""
PACKAGECONFIG[chromeless_tty] = ""
PACKAGECONFIG[containers] = ""
PACKAGECONFIG[seccomp] = ""
PACKAGECONFIG[selinux] = ""

GN_ARGS += ' \
    use={ \
        autotest=${@bb.utils.contains('PACKAGECONFIG', 'autotest', 'true', 'false', d)} \
        chromeless_tests=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tests', 'true', 'false', d)} \
        chromeless_tty=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tty', 'true', 'false', d)} \
        containers=${@bb.utils.contains('PACKAGECONFIG', 'containers', 'true', 'false', d)} \
        seccomp=${@bb.utils.contains('PACKAGECONFIG', 'seccomp', 'true', 'false', d)} \
        selinux=${@bb.utils.contains('PACKAGECONFIG', 'selinux', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


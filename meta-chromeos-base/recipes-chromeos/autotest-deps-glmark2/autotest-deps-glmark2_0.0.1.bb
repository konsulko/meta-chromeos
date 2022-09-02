SUMMARY = "Autotest glmark2 dependency"
DESCRIPTION = "Autotest glmark2 dependency"
HOMEPAGE = "https://launchpad.net/glmark2"
LICENSE = "GPL-3"

inherit chromeos_gn
PR = "r11102"

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
PACKAGECONFIG[asan] = ""
PACKAGECONFIG[autotest] = ""

GN_ARGS += ' \
    use={ \
        asan=${@bb.utils.contains('PACKAGECONFIG', 'asan', 'true', 'false', d)} \
        autotest=${@bb.utils.contains('PACKAGECONFIG', 'autotest', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


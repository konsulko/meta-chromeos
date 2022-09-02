SUMMARY = "Autotest server tests for running Tast-based tests"
DESCRIPTION = "Autotest server tests for running Tast-based tests"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/tast/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-tast.inc

S = "/src/platform/tast"
B = "${WORKDIR}/build"
PR = "r405"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'

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

GN_ARGS += ' \
    use={ \
        autotest=${@bb.utils.contains('PACKAGECONFIG', 'autotest', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


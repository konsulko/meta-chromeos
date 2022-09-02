SUMMARY = "Board specific gestures library configuration file."
DESCRIPTION = "Board specific gestures library configuration file."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/xorg-conf/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-xorg-conf.inc

S = "/src/platform/xorg-conf"
B = "${WORKDIR}/build"
PR = "r154"

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
PACKAGECONFIG[elan] = ""

GN_ARGS += ' \
    use={ \
        elan=${@bb.utils.contains('PACKAGECONFIG', 'elan', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}

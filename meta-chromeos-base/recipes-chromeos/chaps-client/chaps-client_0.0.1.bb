SUMMARY = "chaps D-Bus client library for Chromium OS"
DESCRIPTION = "chaps D-Bus client library for Chromium OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/chaps/client/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "chaps"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}/client"
B = "${WORKDIR}/build"
PR = "r139"

DEPENDS += "chromeos-dbus-bindings-native"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}/client"'

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
PACKAGECONFIG[cros_host] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

do_install() {
    install -d ${D}${includedir}/chaps-client/chaps
    install -m 0644 ${B}/gen/include/chaps/dbus-proxies.h \
		    ${D}${includedir}/chaps-client/chaps/
    install -d ${D}${includedir}/chaps-client-test/chaps
    install -m 0644 ${B}/gen/include/chaps/dbus-proxy-mocks.h \
		    ${D}${includedir}/chaps-client-test/chaps/
}


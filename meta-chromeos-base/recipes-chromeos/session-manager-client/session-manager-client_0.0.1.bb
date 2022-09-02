SUMMARY = "Session manager (chromeos-login) DBus client library for Chromium OS"
DESCRIPTION = "Session manager (chromeos-login) DBus client library for Chromium OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/login_manager/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn pkgconfig

CHROMEOS_PN = "session_manager-client"

S = "${WORKDIR}/src/platform2/login_manager/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r2352"

DEPENDS:append = " chromeos-dbus-bindings-native"

GN_ARGS += 'platform_subdir="login_manager/${CHROMEOS_PN}"'

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

PACKAGES += "libsession-manager-client"

do_compile() {
    ninja -C ${B}
}

do_install() {
    # Install DBus client library.
    install -d ${D}${includedir}/session_manager-client/session_manager
    install -m 0644 ${B}/gen/include/session_manager/dbus-proxies.h \
                    ${D}${includedir}/session_manager-client/session_manager/
    install -d ${D}${includedir}/session_manager-client-test/session_manager
    install -m 0644 ${B}/gen/include/session_manager/dbus-proxy-mocks.h \
                    ${D}${includedir}/session_manager-client-test/session_manager/

    install -d ${D}${libdir}/pkgconfig
    sed \
      -e "s|@INCLUDE_DIR@|${includedir}|g" \
         "${S}/libsession_manager-client.pc.in" > "${D}${libdir}/pkgconfig/libsession_manager-client.pc"

     sed \
       -e "s|@INCLUDE_DIR@|${includedir}|g" \
          "${S}/libsession_manager-client-test.pc.in" > "${D}${libdir}/pkgconfig/libsession_manager-client-test.pc"
}

PROVIDES += "libsession-manager-client"

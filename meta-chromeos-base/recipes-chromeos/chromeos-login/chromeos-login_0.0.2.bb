SUMMARY = "Login manager for Chromium OS."
DESCRIPTION = "Login manager for Chromium OS."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/login_manager/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "login_manager"

DEPENDS:append = " \
    bootstat \
    chromeos-config-tools \
    minijail \
    cryptohome \
    libchromeos-ui \
    libcontainer \
    libpasswordprovider \
    metrics \
    nss \
    protobuf \
    util-linux \
    protofiles \
    system-api \
    vboot-reference \
"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r5075"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'

PACKAGECONFIG ??= "systemd"

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
PACKAGECONFIG[arc_adb_sideloading] = ""
PACKAGECONFIG[cheets] = ""
PACKAGECONFIG[flex_id] = ",,flex-id,,,"
PACKAGECONFIG[fuzzer] = ",,libprotobuf-mutator,,,"
PACKAGECONFIG[systemd] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[user_session_isolation] = ""

GN_ARGS += ' \
    use={ \
        arc_adb_sideloading=${@bb.utils.contains('PACKAGECONFIG', 'arc_adb_sideloading', 'true', 'false', d)} \
        cheets=${@bb.utils.contains('PACKAGECONFIG', 'cheets', 'true', 'false', d)} \
        flex_id=${@bb.utils.contains('PACKAGECONFIG', 'flex_id', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        systemd=${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        user_session_isolation=${@bb.utils.contains('PACKAGECONFIG', 'user_session_isolation', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

do_install() {
    install -d ${D}${base_sbindir}
    install -m 0700 ${B}/keygen ${D}${base_sbindir}
    install -m 0700 ${B}/session_manager ${D}${base_sbindir}

    # Install DBus configuration.
    install -d ${D}${datadir}/dbus-1/interfaces
    install -m 0644 ${S}/dbus_bindings/org.chromium.SessionManagerInterface.xml ${D}${datadir}/dbus-1/interfaces/

    install -d ${D}${sysconfigdir}/dbus-1/system.d
    install -m 0644 ${S}/SessiongManager.conf ${D}${sysconfigdir}/dbus-1/system.d/

    # Adding init scripts
    if [ ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} ]; then
        install -m 0644 ${S}/init/systemd/* ${D}${systemd_unitdir}/
    else
        install -d ${D}${sysconfdir}/init
        install -m 0644 ${S}/init/upstart/*.conf ${D}${sysconfdir}/init/
    fi
    install -d ${D}${datadir}/cros/init
    install -m 0755 ${S}/init/scripts/* ${D}${datadir}/cros/init/

    install -d ${D}${tmpfilesdir}
    install -m 0644 ${S}/tmpfiles.d/chromeos-login.conf ${D}${tmpfilesdir}

    # For user session processes.
    install -d ${D}${sysconfdir}/skel/log

    # For user NSS database
    install -d -m 0700 ${D}${sysconfdir}/skel/.pki
    install -d -m 0700 ${D}${sysconfdir}/skel/.pki/nssdb
    # Yes, the created (empty) DB does work on ARM, x86 and x86_64.
    # certutil -N -d "sql:${D}/etc/skel/.pki/nssdb" -f <(echo '') || die

    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/chrome_dev.conf ${D}${sysconfdir}/

    install -d ${D}${datadir}/power_manager
    install -m 0644 ${S}/powerd_prefs/suspend_freezer_deps_* ${D}${datadir}/power_manager/

    # Create daemon store directories.
    install -d -m 0700 ${D}${sysconfdir}/daemon-store/session_manager
}

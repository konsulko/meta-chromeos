SUMMARY = "Chrome OS activate date mechanism"
DESCRIPTION = "Chrome OS activate date mechanism"
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromiumos_overlay ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}

PR = "r9"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}"

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
PACKAGECONFIG[systemd] = ",,systemd,,,"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/files/activate_date ${D}${bindir}/

    if ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${S}/files/activate_date.service ${D}${systemd_system_unitdir}/
    else
        install -d ${D}${sysconfdir}/init
        install -m 0644 ${S}/files/activate_date.conf ${D}${sysconfdir}/init/
    fi
  
    install -d ${D}${datadir}/cros/init
    install -m 0755 ${S}/files/activate_date.sh ${D}${datadir}/cros/init/
}

FILES:${PN} = "${bindir} ${datadir} ${@bb.utils.contains('PACKAGECONFIG', 'systemd', '${systemd_system_unitdir}', '${sysconfdir}', d)}"

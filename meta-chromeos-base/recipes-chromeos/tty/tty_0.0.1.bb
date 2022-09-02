SUMMARY = "Init script to run agetty on selected terminals."
DESCRIPTION = "Init script to run agetty on selected terminals."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/overlays/chromiumos-overlay/+/master/chromeos-base/tty"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromiumos_overlay ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}

TTY_FILESDIR="${WORKDIR}/src/third_party/chromiumos-overlay/chromeos-base/tty/files"
USE_PREFIX="tty_console_"

B = "${WORKDIR}/build"
PR = "r13"

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
PACKAGECONFIG[systemd] = ""

generate_init_script() {
    # Creates an init script per activated console by copying the base script and
    # changing the port number.
    sed -e "s|%PORT%|${port}|g" \
        ${TTY_FILESDIR}/tty-base.conf > ${B}/console-${port}.conf || \
    bbfatal "failed to generate ${port}"
}

do_compile() {
    # Generate a file for each activated tty console.
    if ! ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} ; then
        for item in ${IUSE_PORTS}; do
            generate_init_script ${item}
        done
    fi
}

do_install() {
	if [[ -n ${TTY_CONSOLE} ]]; then
                if ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} ; then
                        systemd_dounit "${TTY_FILESDIR}/chromeos-tty@.service"
                        for item in ${IUSE_PORTS}; do
                                if use ${item}; then
                                        port="${item#${USE_PREFIX}}"
                                        unit_dir=${systemd_unit_dir}
                                        dosym  "../chromeos-tty@.service" \
                                                "${unit_dir}/boot-services.target.wants/chromeos-tty@${port}.service"
                                fi
                        done
                else
                        install -d ${D}${sysconfdir}/init
                        install -m 0644 ${B}/console-*.conf ${D}${sysconfdir}/init/
                fi
        fi
}

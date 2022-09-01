SUMMARY = "Power Manager for Chromium OS"
DESCRIPTION = "Power Manager for Chromium OS"
HOMEPAGE = "http://dev.chromium.org/chromium-os/packages/power_manager"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "power_manager"

PR = "r4312"

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
PACKAGECONFIG[als] = ""
PACKAGECONFIG[cellular] = ""
PACKAGECONFIG[cras] = ""
PACKAGECONFIG[cros_embedded] = ""
PACKAGECONFIG[display_backlight] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[has_keyboard_backlight] = ""
PACKAGECONFIG[iioservice] = ""
PACKAGECONFIG[keyboard_includes_side_buttons] = ""
PACKAGECONFIG[keyboard_convertible_no_side_buttons] = ""
PACKAGECONFIG[legacy_power_button] = ""
PACKAGECONFIG[powerd_manual_eventlog_add] = ""
PACKAGECONFIG[powerknobs] = ""
PACKAGECONFIG[systemd] = ""
PACKAGECONFIG[touchpad_wakeup] = ""
PACKAGECONFIG[touchscreen_wakeup] = ""
PACKAGECONFIG[unibuild] = ""
PACKAGECONFIG[wilco] = ""
PACKAGECONFIG[qrtr] = ""

GN_ARGS += ' \
    use={ \
        als=${@bb.utils.contains('PACKAGECONFIG', 'als', 'true', 'false', d)} \
        cellular=${@bb.utils.contains('PACKAGECONFIG', 'cellular', 'true', 'false', d)} \
        cras=${@bb.utils.contains('PACKAGECONFIG', 'cras', 'true', 'false', d)} \
        cros_embedded=${@bb.utils.contains('PACKAGECONFIG', 'cros_embedded', 'true', 'false', d)} \
        display_backlight=${@bb.utils.contains('PACKAGECONFIG', 'display_backlight', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        has_keyboard_backlight=${@bb.utils.contains('PACKAGECONFIG', 'has_keyboard_backlight', 'true', 'false', d)} \
        iioservice=${@bb.utils.contains('PACKAGECONFIG', 'iioservice', 'true', 'false', d)} \
        keyboard_includes_side_buttons=${@bb.utils.contains('PACKAGECONFIG', 'keyboard_includes_side_buttons', 'true', 'false', d)} \
        keyboard_convertible_no_side_buttons=${@bb.utils.contains('PACKAGECONFIG', 'keyboard_convertible_no_side_buttons', 'true', 'false', d)} \
        legacy_power_button=${@bb.utils.contains('PACKAGECONFIG', 'legacy_power_button', 'true', 'false', d)} \
        powerd_manual_eventlog_add=${@bb.utils.contains('PACKAGECONFIG', 'powerd_manual_eventlog_add', 'true', 'false', d)} \
        powerknobs=${@bb.utils.contains('PACKAGECONFIG', 'powerknobs', 'true', 'false', d)} \
        systemd=${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} \
        touchpad_wakeup=${@bb.utils.contains('PACKAGECONFIG', 'touchpad_wakeup', 'true', 'false', d)} \
        touchscreen_wakeup=${@bb.utils.contains('PACKAGECONFIG', 'touchscreen_wakeup', 'true', 'false', d)} \
        unibuild=${@bb.utils.contains('PACKAGECONFIG', 'unibuild', 'true', 'false', d)} \
        wilco=${@bb.utils.contains('PACKAGECONFIG', 'wilco', 'true', 'false', d)} \
        qrtr=${@bb.utils.contains('PACKAGECONFIG', 'qrtr', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


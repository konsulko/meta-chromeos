SUMMARY = "Crash reporting service that uploads crash reports with debug information"
DESCRIPTION = "Crash reporting service that uploads crash reports with debug information"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/crash-reporter/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r3837"

GN_ARGS += 'platform_subdir="${BPN}"'

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
PACKAGECONFIG[arcpp] = ""
PACKAGECONFIG[arcvm] = ""
PACKAGECONFIG[chromeless_tty] = ""
PACKAGECONFIG[cros_ec] = ""
PACKAGECONFIG[cros_embedded] = ""
PACKAGECONFIG[direncryption] = ""
PACKAGECONFIG[kvm_guest] = ""
PACKAGECONFIG[systemd] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[vmcontainers] = ""
PACKAGECONFIG[force_breakpad] = ""

GN_ARGS += ' \
    use={ \
        arcpp=${@bb.utils.contains('PACKAGECONFIG', 'arcpp', 'true', 'false', d)} \
        arcvm=${@bb.utils.contains('PACKAGECONFIG', 'arcvm', 'true', 'false', d)} \
        chromeless_tty=${@bb.utils.contains('PACKAGECONFIG', 'chromeless_tty', 'true', 'false', d)} \
        cros_ec=${@bb.utils.contains('PACKAGECONFIG', 'cros_ec', 'true', 'false', d)} \
        cros_embedded=${@bb.utils.contains('PACKAGECONFIG', 'cros_embedded', 'true', 'false', d)} \
        direncryption=${@bb.utils.contains('PACKAGECONFIG', 'direncryption', 'true', 'false', d)} \
        kvm_guest=${@bb.utils.contains('PACKAGECONFIG', 'kvm_guest', 'true', 'false', d)} \
        systemd=${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        vmcontainers=${@bb.utils.contains('PACKAGECONFIG', 'vmcontainers', 'true', 'false', d)} \
        force_breakpad=${@bb.utils.contains('PACKAGECONFIG', 'force_breakpad', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


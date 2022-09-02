SUMMARY = "Daemon to manage TPM ownership."
DESCRIPTION = "Daemon to manage TPM ownership."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/tpm_manager/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "tpm_manager"

DEPENDS:append = " libbrillo libchrome libtpmcrypto openssl protobuf protobuf-native"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r2604"

PACKAGECONFIG ??= "${@bb.utils.filter('MACHINE_FEATURES', 'tpm tpm2', d)}"

# Description of all the possible PACKAGECONFIG fields:
# 1. Extra arguments that should be added to the configure script argument list (EXTRA_OECONF or PACKAGECONFIG_CONFARGS) if the feature is enabled.
# 2. Extra arguments that should be added to EXTRA_OECONF or PACKAGECONFIG_CONFARGS if the feature is disabled.
# 3. Additional build dependencies (DEPENDS) that should be added if the feature is enabled.
# 4. Additional runtime dependencies (RDEPENDS) that should be added if the feature is enabled.
# 5. Additional runtime recommendations (RRECOMMENDS) that should be added if the feature is enabled.
# 6. Any conflicting (that is, mutually exclusive) PACKAGECONFIG settings for this feature.

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches
# and command-line switches (extra dependencies should not
# be necessary but are OK to add).
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[double_extend_pcr_issue] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[os_install_service] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpm] = ""
PACKAGECONFIG[tpm2] = ""
PACKAGECONFIG[tpm_dynamic] = ""
PACKAGECONFIG[tpm_insecure_fallback] = ""


GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'
GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        double_extend_pcr_issue=${@bb.utils.contains('PACKAGECONFIG', 'double_extend_pcr_issue', 'true', 'false', d)} \
        os_install_service=${@bb.utils.contains('PACKAGECONFIG', 'os_install_service', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
        tpm_dynamic=${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} \
        tpm_insecure_fallback=${@bb.utils.contains('PACKAGECONFIG', 'tpm_insecure_fallback', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


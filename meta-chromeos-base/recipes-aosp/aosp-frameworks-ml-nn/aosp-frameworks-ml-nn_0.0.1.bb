SUMMARY = "Chrome OS port of the Android Neural Network API"
DESCRIPTION = "Chrome OS port of the Android Neural Network API"
HOMEPAGE = "https://developer.android.com/ndk/guides/neuralnetworks"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r354"

DEPENDS += "libeigen libtextclassifier nnapi openssl tensorflow"

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
PACKAGECONFIG[cpu_flags_x86_avx2] = ""
PACKAGECONFIG[vendornnhal] = ""
PACKAGECONFIG[minimaldriver] = ""
PACKAGECONFIG[xnnpack] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[strace_ipc_driver] = ""

GN_ARGS += ' \
    use={ \
        cpu_flags_x86_avx2=${@bb.utils.contains('PACKAGECONFIG', 'cpu_flags_x86_avx2', 'true', 'false', d)} \
        vendornnhal=${@bb.utils.contains('PACKAGECONFIG', 'vendornnhal', 'true', 'false', d)} \
        minimaldriver=${@bb.utils.contains('PACKAGECONFIG', 'minimaldriver', 'true', 'false', d)} \
        xnnpack=${@bb.utils.contains('PACKAGECONFIG', 'xnnpack', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        strace_ipc_driver=${@bb.utils.contains('PACKAGECONFIG', 'strace_ipc_driver', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


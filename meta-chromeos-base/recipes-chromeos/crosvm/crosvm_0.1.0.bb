SUMMARY = "Utility for running VMs on Chrome OS"
DESCRIPTION = "Utility for running VMs on Chrome OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/crosvm/"
LICENSE = "BSD-3-Clause & Apache-2.0 & BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-${BPN}.inc

S = "${WORKDIR}/src/platform/${BPN}"
B = "${WORKDIR}/build"
PR = "r1748"

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
PACKAGECONFIG[test] = ""
PACKAGECONFIG[crosdebug] = ""
PACKAGECONFIG[crosvmgpu] = ""
PACKAGECONFIG[crosvmdirect] = ""
PACKAGECONFIG[crosvmplugin] = ""
PACKAGECONFIG[crosvmpowermonitorpowerd] = ""
PACKAGECONFIG[crosvmvideodecoder] = ""
PACKAGECONFIG[crosvmvideoencoder] = ""
PACKAGECONFIG[crosvmvideoffmpeg] = ""
PACKAGECONFIG[crosvmvideolibvda] = ""
PACKAGECONFIG[crosvmwldmabuf] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[tpm2] = ""
PACKAGECONFIG[androidvmmaster] = ""
PACKAGECONFIG[androidvmtm] = ""
PACKAGECONFIG[arcvm_gce_l1] = ""

GN_ARGS += ' \
    use={ \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        crosdebug=${@bb.utils.contains('PACKAGECONFIG', 'crosdebug', 'true', 'false', d)} \
        crosvmgpu=${@bb.utils.contains('PACKAGECONFIG', 'crosvmgpu', 'true', 'false', d)} \
        crosvmdirect=${@bb.utils.contains('PACKAGECONFIG', 'crosvmdirect', 'true', 'false', d)} \
        crosvmplugin=${@bb.utils.contains('PACKAGECONFIG', 'crosvmplugin', 'true', 'false', d)} \
        crosvmpowermonitorpowerd=${@bb.utils.contains('PACKAGECONFIG', 'crosvmpowermonitorpowerd', 'true', 'false', d)} \
        crosvmvideodecoder=${@bb.utils.contains('PACKAGECONFIG', 'crosvmvideodecoder', 'true', 'false', d)} \
        crosvmvideoencoder=${@bb.utils.contains('PACKAGECONFIG', 'crosvmvideoencoder', 'true', 'false', d)} \
        crosvmvideoffmpeg=${@bb.utils.contains('PACKAGECONFIG', 'crosvmvideoffmpeg', 'true', 'false', d)} \
        crosvmvideolibvda=${@bb.utils.contains('PACKAGECONFIG', 'crosvmvideolibvda', 'true', 'false', d)} \
        crosvmwldmabuf=${@bb.utils.contains('PACKAGECONFIG', 'crosvmwldmabuf', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
        androidvmmaster=${@bb.utils.contains('PACKAGECONFIG', 'androidvmmaster', 'true', 'false', d)} \
        androidvmtm=${@bb.utils.contains('PACKAGECONFIG', 'androidvmtm', 'true', 'false', d)} \
        arcvm_gce_l1=${@bb.utils.contains('PACKAGECONFIG', 'arcvm_gce_l1', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


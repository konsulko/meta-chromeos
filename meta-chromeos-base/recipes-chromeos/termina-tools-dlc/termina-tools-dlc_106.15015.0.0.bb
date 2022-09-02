SUMMARY = "DLC package for vm tools for termina."
DESCRIPTION = "DLC package for vm tools for termina."
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"
PR = "r1"

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
PACKAGECONFIG[kvm_host] = ""
PACKAGECONFIG[dlc] = ""
PACKAGECONFIG[amd64] = ""
PACKAGECONFIG[arm] = ""

GN_ARGS += ' \
    use={ \
        kvm_host=${@bb.utils.contains('PACKAGECONFIG', 'kvm_host', 'true', 'false', d)} \
        dlc=${@bb.utils.contains('PACKAGECONFIG', 'dlc', 'true', 'false', d)} \
        amd64=${@bb.utils.contains('PACKAGECONFIG', 'amd64', 'true', 'false', d)} \
        arm=${@bb.utils.contains('PACKAGECONFIG', 'arm', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


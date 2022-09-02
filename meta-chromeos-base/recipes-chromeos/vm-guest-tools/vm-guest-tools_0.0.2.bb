SUMMARY = "VM guest tools for Chrome OS"
DESCRIPTION = "VM guest tools for Chrome OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/vm_tools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "vm_guest_tools"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r1369"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'

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
PACKAGECONFIG[kvm_guest] = ""
PACKAGECONFIG[vmcontainers] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[vm_borealis] = ""

GN_ARGS += ' \
    use={ \
        kvm_guest=${@bb.utils.contains('PACKAGECONFIG', 'kvm_guest', 'true', 'false', d)} \
        vmcontainers=${@bb.utils.contains('PACKAGECONFIG', 'vmcontainers', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        vm_borealis=${@bb.utils.contains('PACKAGECONFIG', 'vm_borealis', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}

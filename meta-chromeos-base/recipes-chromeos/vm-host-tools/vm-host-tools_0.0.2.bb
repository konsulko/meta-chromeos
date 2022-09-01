SUMMARY = "VM host tools for Chrome OS"
DESCRIPTION = "VM host tools for Chrome OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/vm_tools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "vm_host_tools"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r1811"

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
PACKAGECONFIG[kvm_host] = ""
PACKAGECONFIG[seccomp] = ""
PACKAGECONFIG[crosvmwldmabuf] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[wilco] = ""
PACKAGECONFIG[crosvmvirtiovideo] = ""
PACKAGECONFIG[vulkan] = ""
PACKAGECONFIG[libglvnd] = ""
PACKAGECONFIG[crosvm_siblings] = ""
PACKAGECONFIG[virtgpu_native_context] = ""
PACKAGECONFIG[iioservice] = ""

GN_ARGS += ' \
    use={ \
        kvm_host=${@bb.utils.contains('PACKAGECONFIG', 'kvm_host', 'true', 'false', d)} \
        seccomp=${@bb.utils.contains('PACKAGECONFIG', 'seccomp', 'true', 'false', d)} \
        crosvmwldmabuf=${@bb.utils.contains('PACKAGECONFIG', 'crosvmwldmabuf', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        wilco=${@bb.utils.contains('PACKAGECONFIG', 'wilco', 'true', 'false', d)} \
        crosvmvirtiovideo=${@bb.utils.contains('PACKAGECONFIG', 'crosvmvirtiovideo', 'true', 'false', d)} \
        vulkan=${@bb.utils.contains('PACKAGECONFIG', 'vulkan', 'true', 'false', d)} \
        libglvnd=${@bb.utils.contains('PACKAGECONFIG', 'libglvnd', 'true', 'false', d)} \
        crosvm_siblings=${@bb.utils.contains('PACKAGECONFIG', 'crosvm_siblings', 'true', 'false', d)} \
        virtgpu_native_context=${@bb.utils.contains('PACKAGECONFIG', 'virtgpu_native_context', 'true', 'false', d)} \
        iioservice=${@bb.utils.contains('PACKAGECONFIG', 'iioservice', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


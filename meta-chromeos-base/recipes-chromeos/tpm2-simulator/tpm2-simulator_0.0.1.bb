SUMMARY = "TPM 2.0 Simulator"
DESCRIPTION = "TPM 2.0 Simulator"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/tpm2-simulator/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn useradd

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r1948"

SRC_URI += "file://tpm2-simulator-constexpr-return.patch"

DEPENDS += "libbrillo libchrome libselinux minijail openssl pinweaver vboot-reference"

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
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[selinux] = ",,,selinux-policy,,"
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpm] = ""
PACKAGECONFIG[tpm2] = ",,tpm2,tpm2,,"
PACKAGECONFIG[tpm2_simulator] = ""
PACKAGECONFIG[tpm2_simulator_manufacturer] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        selinux=${@bb.utils.contains('PACKAGECONFIG', 'selinux', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
        tpm2_simulator=${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator', 'true', 'false', d)} \
        tpm2_simulator_manufacturer=${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator_manufacturer', 'true', 'false', d)} \
    } \
'

USERADD_PACKAGES = "${PN}"

# tpm2-simulator:!:20169:20169:Chromium OS tpm2-simulator daemon runs as this user:/dev/null:/bin/false
GROUPADD_PARAM:${PN} = "--gid 20169 --system tpm2-simulator"
USERADD_PARAM:${PN} = "--uid 20169 --gid 20169 --comment 'Chromium OS tpm2-simulator daemon runs as this user' --home-dir /dev/null --system --shell /bin/false tpm2-simulator"

do_compile() {
    # Hack to avoid constexpr no return warning/error
    if [-z ${USE_TPM1} ]; then
        export USE_TPM2
    fi
    ninja -C ${B}
}

do_install() {
    :
}


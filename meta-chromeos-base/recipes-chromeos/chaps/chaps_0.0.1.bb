SUMMARY = "PKCS #11 layer over TrouSerS"
DESCRIPTION = "PKCS #11 layer over TrouSerS"
HOMEPAGE = "http://www.chromium.org/developers/design-documents/chaps-technical-design"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn useradd

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r3756"

SRC_URI += "file://0001-chaps-nss-include-path.patch"

CXX:append = " -I${STAGING_INCDIR}"

DEPENDS += " \
    chaps-client \
    leveldb \
    libbrillo \
    libchrome \
    libhwsec \
    libmetrics \
    minijail \
    nss \
    nspr \
    openssl \
    protobuf \
    protobuf-native \
    system-api \
"

GN_ARGS += 'platform_subdir="${BPN}"'

PACKAGECONFIG ??= "\
    ${@bb.utils.filter("DISTRO_FEATURES", "systemd", d)} \
    ${@bb.utils.filter("MACHINE_FEATURES", "tpm tpm2", d)} \
    tpm_insecure_fallback"

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
PACKAGECONFIG[systemd] = ",,systemd,,,"
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ",,gzip tar,,,"
PACKAGECONFIG[tpm] = ",,,trousers,,tpm2"
PACKAGECONFIG[tpm_dynamic] = ""
PACKAGECONFIG[tpm_insecure_fallback] = ""
PACKAGECONFIG[tpm2] = ",,trunks,trunks,,tpm"

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        systemd=${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm_dynamic=${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} \
        tpm_insecure_fallback=${@bb.utils.contains('PACKAGECONFIG', 'tpm_insecure_fallback', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
    } \
'

USERADD_PACKAGES = "${PN}"


# system users/groups; must exist before user can be added to them
GROUPADD_PARAM:${PN} = "--gid 207 --system tss"
GROUPADD_PARAM:${PN}:append = "; --gid 208 --system pkcs11"
GROUPADD_PARAM:${PN}:append = "; --gid 400 --system daemon-store"
# chaps:!:223:223:chaps PKCS11 daemon:/dev/null:/bin/false
USERADD_PARAM:${PN} = "-u 223 -U -c 'chaps PKCS11 daemon' -d /dev/null -r -s /bin/false -G tss,pkcs11,daemon-store chaps"
#USERADD_PARAM:${PN} = "-u 223 -U -c 'chaps PKCS11 daemon' -d /dev/null -r -s /bin/false chaps"

do_compile() {
    ninja -C ${B}
}

do_install() {
    install -d ${D}${includedir}/chaps/
    install -m 0644 ${S}/pkcs11/*.h ${D}${includedir}/chaps/
}


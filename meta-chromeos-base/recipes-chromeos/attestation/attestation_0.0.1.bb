SUMMARY = "Attestation service for Chromium OS"
DESCRIPTION = "Attestation service for Chromium OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/attestation/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn useradd

DEPENDS:append = " \
    abseil-cpp \
    attestation-client \
    chromeos-dbus-bindings-native \
    github.com-golang-protobuf-native \
    libbrillo \
    libchrome \
    libmetrics \
    libminijail \
    libtpm-manager-client \
    nss \
    openssl \
    protobuf \
    protobuf-native \
    system-api \
    vboot-reference \
"

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r3611"

RDEPENDS:${PN} += " \
    chaps \
    libhwsec \
    libhwsec-foundation \
    system-api \
    metrics \
    minijail \
    tpm-manager \
    attestation-client \
    "

GN_ARGS += 'platform_subdir="${BPN}"'

PACKAGECONFIG ??= "\
    ${@bb.utils.filter("MACHINE_FEATURES", "tpm tpm2", d)} \
"

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
PACKAGECONFIG[generic_tpm2] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[ti50_onboard] = ""
PACKAGECONFIG[tpm] = ",,,trousers,,"
PACKAGECONFIG[tpm_dynamic] = ""
PACKAGECONFIG[tpm2] = ",,trunks chromeos-ec-headers,trunks,,"

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        generic_tpm2=${@bb.utils.contains('PACKAGECONFIG', 'generic_tpm2', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        ti50_onboard=${@bb.utils.contains('PACKAGECONFIG', 'ti50_onboard', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm_dynamic=${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
    } \
'

USERADD_PACKAGES = "${PN}"

# attestation:!:247:247:Chromium OS attestation daemon runs as this user:/dev/null:/bin/false
USERADD_PARAM:${PN} = "-u 247 -U -c 'Chromium OS attestation daemon runs as this user' -d /dev/null -r -s /bin/false attestation"
# Create group for /mnt/stateful_partition/unencrypted/preserve.
# preserve:!:253:root,attestation,tpm_manager
GROUPADD_PARAM:${PN} = "-g 253 preserve"

CXX:append = " -I${STAGING_INCDIR}"

do_configure:append() {
    sed -i -e "s,nss/pkcs11.h,nss3/pkcs11.h,g" ${WORKDIR}/src/platform2/chaps/pkcs11/cryptoki.h
}

do_compile() {
    ninja -C ${B}
}

do_install() {
    install -d ${D}${includedir}/attestation/common
    install -m 0644 ${S}/commmon/attestation_interface.h ${D}${includedir}/attestation/common/
    install -m 0644 ${B}/gen/attestation/common/print_attestation_ca_proto.h ${D}${includedir}/attestation/common/
    install -m 0644 ${B}/gen/attestation/common/print_interface_proto.h ${D}${includedir}/attestation/common/
    install -m 0644 ${B}/gen/attestation/common/print_keystore_proto.h ${D}${includedir}/attestation/common/

    # Install the generated dbus-binding for fake pca agent.
    # It does no harm to install the header even for non-test image build.
    install -d ${D}${includedir}/attestation/pca-agent/dbus_adaptors
    install -m 0644 ${B}/gen/include/attestation/pca-agent/dbus_adaptors/org.chromium.PcaAgent.h \
                    ${D}${includedir}/attestation/pca-agent/dbus_adaptors
}

FILES:${PN}-dev += "${includedir}/attestation/common/*.h"

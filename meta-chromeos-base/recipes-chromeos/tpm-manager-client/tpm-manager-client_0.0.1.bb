SUMMARY = "TPM Manager D-Bus client library for Chromium OS"
DESCRIPTION = "TPM Manager D-Bus client library for Chromium OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/tpm_manager/client/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "tpm_manager"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}/client"
B = "${WORKDIR}/build"
PR = "r447"

# protobuf RPROVIDES protobuf-lite
DEPENDS += " chromeos-dbus-bindings-native libbrillo libchrome protobuf system-api"

PACKAGES += "libtpm-manager-client"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}/client"'

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
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpm] = ""
PACKAGECONFIG[tpm2] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

export SO_VERSION="1"

do_install() {
    # Install D-Bus client library.
    install -d ${D}${includedir}/tpm_manager-client/tpm_manager
    install -m 0644 ${B}/gen/include/tpm_manager/dbus-proxies.h \
                    ${D}${includedir}/tpm_manager-client/tpm_manager/
    install -d ${D}${includedir}/tpm_manager-client-test/tpm_manager
    install -m 0644 ${B}/gen/include/tpm_manager/dbus-proxy-mocks.h \
                    ${D}${includedir}/tpm_manager-client-test/tpm_manager/

    install -d ${D}${libdir}/pkgconfig
    sed \
      -e "s|@INCLUDE_DIR@|${includedir}|g" \
      -e "s|@PV@|${PV}|g" \
         "${S}/libtpm_manager-client.pc.in" > "${D}${libdir}/pkgconfig/libtpm_manager-client.pc"

     sed \
       -e "s|@INCLUDE_DIR@|${includedir}|g" \
       -e "s|@PV@|${PV}|g" \
          "${S}/libtpm_manager-client-test.pc.in" > "${D}${libdir}/pkgconfig/libtpm_manager-client-test.pc"

    install -d ${D}${bindir}
    install -m 0755 ${B}/tpm_manager_client ${D}${bindir}/

    install -d ${D}${libdir}
    find ${B}${base_libdir} -type f -name lib*.so | while read f; do
        fn=$(basename ${f})
        echo ${fn}
        install -m 0755 ${f} ${D}${libdir}/${fn}.${SO_VERSION}
        ln -sf ${fn}.${SO_VERSION} ${D}${libdir}/${fn}
    done

    # Install header files.
    install -d ${D}${includedir}/tpm_manager/client
    install -m 0644 ${S}/*.h ${D}${includedir}/tpm_manager/client/
    install -d ${D}${includedir}/tpm_manager/common
    install -m 0644 ${S}/../common/*.h ${D}${includedir}/tpm_manager/common/
}

PROVIDES += "libtpm-manager-client"

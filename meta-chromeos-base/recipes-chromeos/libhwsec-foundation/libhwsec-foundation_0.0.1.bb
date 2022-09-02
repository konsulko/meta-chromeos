SUMMARY = "Crypto and utility functions used in TPM related daemons."
DESCRIPTION = "Crypto and utility functions used in TPM related daemons."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/libhwsec-foundation/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r305"

DEPENDS:append = " libbrillo libchrome libmetrics libtpm-manager-client openssl protobuf re2 system-api"

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
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpm] = ""
PACKAGECONFIG[tpm_dynamic] = ""
PACKAGECONFIG[tpm2] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm_dynamic=${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

export SO_VERSION="1"

do_install() {
    # Install header files.
    install -d ${D}${includedir}/libhwsec-foundation
    install -m 0644 ${S}/*.h ${D}${includedir}/libhwsec-foundation/
    for dir in status status/impl syscaller tpm_error utility error tpm profiling; do
        install -d ${D}${includedir}/libhwsec-foundation/${dir}
        install -m 0644 ${S}/${dir}/*.h ${D}${includedir}/libhwsec-foundation/${dir}/
    done

    install -d ${D}${libdir}
    find ${B}${base_libdir} -type f -name lib*.so | while read f; do
        fn=$(basename ${f})
        echo ${fn}
        install -m 0755 ${f} ${D}${libdir}/${fn}.${SO_VERSION}
        ln -sf ${fn}.${SO_VERSION} ${D}${libdir}/${fn}
    done
    install -m 0644 ${B}/libhwsec-profiling.a ${D}${libdir}

    install -d ${D}${sbindir}
    install -m 0744 ${B}/tpm_version_client ${D}${sbindir}

    if ${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} ; then
        install -m 0744 ${S}/tool/tpm_version ${D}${sbindir}

        install -d ${D}${sysconfdir}/init
        install -m 0644 ${S}/init/no-tpm-checker.conf ${D}${sysconfdir}/init/
    fi
}


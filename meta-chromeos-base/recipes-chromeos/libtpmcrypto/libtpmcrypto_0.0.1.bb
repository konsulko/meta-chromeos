SUMMARY = "Encrypts/Decrypts data to a serialized proto with TPM sealed key."
DESCRIPTION = "Encrypts/Decrypts data to a serialized proto with TPM sealed key."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/libtpmcrypto/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

DEPENDS:append = "libbrillo libchrome libhwsec-foundation openssl protobuf protobuf-native"

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r557"

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
   install -d ${D}${libdir}
   find ${B}${base_libdir} -type f -name lib*.so | while read f; do
       fn=$(basename ${f})
       echo ${fn}
       install -m 0755 ${f} ${D}${libdir}/${fn}.${SO_VERSION}
       ln -sf ${fn}.${SO_VERSION} ${D}${libdir}/${fn}
   done

   install -d ${D}${libdir}/pkgconfig
   sed \
       -e "s/@BSLOT@/${PV}/g" \
       -e "s:@INCLUDE_DIR@:${includedir}:g" \
       ${S}/libtpmcrypto.pc.in > ${B}/libtpmcrypto.pc
   install -m 0644 ${B}/libtpmcrypto.pc ${D}${libdir}/pkgconfig/    

   install -d ${D}${includedir}/libtpmcrypto
   install -m 0644 ${S}/*.h ${D}${includedir}/libtpmcrypto/
}


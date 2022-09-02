SUMMARY = "Library used to start Chromium-based UIs"
DESCRIPTION = "Library used to start Chromium-based UIs"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/libchromeos-ui/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-libchrome.inc

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r1924"

DEPENDS += "libbrillo libchrome"

RDEPENDS:${PN} += "bootstat"

PACKAGECONFIG ??= ""

# Description of all the possible PACKAGECONFIG fields:
# 1. Extra arguments that should be added to the configure script argument list (EXTRA_OECONF or PACKAGECONFIG_CONFARGS) if the feature is enabled.
# 2. Extra arguments that should be added to EXTRA_OECONF or PACKAGECONFIG_CONFARGS if the feature is disabled.
# 3. Additional build dependencies (DEPENDS) that should be added if the feature is enabled.
# 4. Additional runtime dependencies (RDEPENDS) that should be added if the feature is enabled.
# 5. Additional runtime recommendations (RRECOMMENDS) that should be added if the feature is enabled.
# 6. Any conflicting (that is, mutually exclusive) PACKAGECONFIG settings for this feature.

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches
# and command-line switches (extra dependencies should not
# be necessary but are OK to add).
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tcmalloc] = ""

GN_ARGS += 'platform_subdir="${BPN}"'

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B}
}

export SO_VERSION = "1"

do_install() {
    install -d ${D}${libdir}
    find ${B} -type f -name lib*.so | while read f; do
        fn=$(basename ${f})
        echo ${fn}
        install -m 0755 ${f} ${D}${libdir}/${fn}.${SO_VERSION}
        ln -sf ${fn}.${SO_VERSION} ${D}${libdir}/${fn}
    done

    LIBCHROME_VER=$(cat ${WORKDIR}/src/platform/libchrome/BASE_VER)

    install -d ${D}${libdir}/pkgconfig
    sed -e "s/@BSLOT@/${LIBCHROME_VER}/g" ${S}/libchromeos-ui.pc.in > ${B}/lib/libchromeos-ui.pc
    install -m 0644 ${B}/lib/libchromeos-ui.pc ${D}${libdir}/pkgconfig/

    install -d ${D}${includedir}/chromeos/ui
    install -m 0644 ${S}/chromeos/ui/*.h ${D}${includedir}/chromeos/ui/
}


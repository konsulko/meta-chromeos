SUMMARY = "Chrome OS configuration tools"
DESCRIPTION = "Chrome OS configuration tools"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/chromeos-config"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "chromeos-config"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r2134"

DEPENDS:append = " libbrillo libchrome"

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
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[unibuild] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        unibuild=${@bb.utils.contains('PACKAGECONFIG', 'unibuild', 'true', 'false', d)} \
    } \
'

RDEPENDS:${PN} += "bash"

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

    install -d ${D}${includedir}/chromeos/chromeos-config/libcros_config
    install -m 0644 ${S}/libcros_config/*.h ${D}${includedir}/chromeos/chromeos-config/libcros_config/

    install -d ${D}${libdir}/pkgconfig
    PKGCONFIGS=$(find ${B} -name *.pc)
    for pkgconfig in $PKGCONFIGS; do
        install -m 0644 $pkgconfig ${D}${libdir}/pkgconfig/
    done

    install -d ${D}${includedir}/cros_config
    install -m 0644 ${S}/libcros_config/cros_config_interface.h ${D}${includedir}/cros_config/
    install -m 0644 ${S}/libcros_config/cros_config.h ${D}${includedir}/cros_config/
    install -m 0644 ${S}/libcros_config/fake_cros_config.h ${D}${includedir}/cros_config/

    install -d ${D}${bindir}
    install -m 0755 ${B}/cros_config ${D}${bindir}/
    install -m 0755 ${S}/cros_config_mock.sh ${D}${bindir}/cros_config_mock

    install -d ${D}${sbindir}
    if [ ${@bb.utils.contains('PACKAGECONFIG', 'unibuild', 'true', 'false', d)} ]; then
        install -m 0755 ${S}/scripts/cros_config_setup.sh ${D}${sbindir}/cros_config_setup
    else
	install -m 0755 ${S}/scripts/cros_config_setup_legacy.sh ${D}${sbindir}/cros_config_setup
    fi

    install -d ${D}${sysconfdir}/init
    install -m 0644 ${S}/init/*.conf ${D}${sysconfdir}/init/
}


SUMMARY = "Chrome OS Model Configuration tools"
DESCRIPTION = "Chrome OS Model Configuration tools"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/chromeos-config"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn clang meson

SRC_URI += " \
    file://0001-cross-compile-tests.patch \
"

S = "${WORKDIR}/src/platform2/chromeos-config/crosid"
B = "${WORKDIR}/build"
PR = "r76"

DEPENDS:append = " libbrillo libchrome compiler-rt-sanitizers"

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

GN_ARGS += 'platform_subdir="chromeos-config"'

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
    } \
'

BUILD_CPPFLAGS:remove:runtime-llvm = "-stdlib=libc++"
export PYTHONPATH="${S}"
EXTRA_OEMESON:append = " -Ddefault_library=both"
do_configure() {
    meson_do_configure
}

export SO_VERSION="1"

do_install:append () {
    install -d ${D}${bindir}
    install -m 0755 ${B}/crosid ${D}${bindir}

    install -d ${D}${libdir}
    install -m 0644 ${B}/libcrosid.a ${D}${libdir}
    find ${B} -type f -name lib*.so | while read f; do
        fn=$(basename ${f})
        echo ${fn}
        install -m 0755 ${f} ${D}${libdir}/${fn}.${SO_VERSION}
        ln -sf ${fn}.${SO_VERSION} ${D}${libdir}/${fn}
    done

    install -d ${D}${includedir}
    install -m 0644 ${S}/crosid.h ${D}${includedir}
}

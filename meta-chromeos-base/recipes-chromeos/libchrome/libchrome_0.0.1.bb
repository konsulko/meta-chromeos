SUMMARY = "Base library for Chromium OS"
DESCRIPTION="Chrome base/ and dbus/ libraries extracted for use on Chrome OS"
HOMEPAGE="http://dev.chromium.org/chromium-os/packages/libchrome"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromiumos-platform/chromiumos-platform-${BPN}.inc

DEPENDS:append = " python3-native abseil-cpp glib-2.0 libevent gtest modp-b64 double-conversion re2"

S = "${WORKDIR}/src/platform/libchrome"
B = "${WORKDIR}/build"
PR = "r410"

CC:append = " -I${STAGING_INCDIR}"
CPP:append = " -I${STAGING_INCDIR}"
CXX:append = " -I${STAGING_INCDIR}"

# Unused patches to port to OpenSSL 3.0
# file://0013-libchrome-crypto-secure_hash-port-to-EVP.patch
# file://0014-libchrome-crypto-scoped_openssl_types-drop-deprecated.patch
SRC_URI += " \
    gitsm://chromium.googlesource.com/linux-syscall-support;protocol=https;branch=main;destsuffix=src/platform/libchrome/third_party/lss;name=lss \
    file://0001-libchrome-base-hash-md5.h-include-string.h.patch \
    file://0002-libchrome-base-metrics-histogram-double_t.patch \
    file://0003-libchrome-base-metrics-histogram-static_cast.patch \
    file://0004-libchrome-base-metrics-sample_vector-static_cast.patch \
    file://0005-libchrome-base-metrics-histogram-more-static_cast.patch \
    file://0006-libchrome-base-process-process_metrics-static_cast.patch \
    file://0007-libchrome-base-numerics-clamped_math_impl-cast.patch \
    file://0008-libchrome-base-time-time_delta_from_string-cast.patch \
    file://0009-libchrome-base-time-time-static_cast-double.patch \
    file://0010-libchrome-crypto-nss_util-use-nss3-nss.h-path.patch \
    file://0011-libchrome-crypto-p224_spoke-include-string.h.patch \
    file://0012-libchrome-crypto-scoped_nss_types-fix-nss.h-path.patch \
    "

SRCREV_lss = "92a65a8f5d705d1928874420c8d0d15bde8c89e5"

# The order of patches is set in src/platform/libchrome/libchrome_tools/patches/patches
#
# libchrome/libchrome_tools contains a series of patches maintained by quilt(?).
# So manually apply them before applying other local patches. Also remove all
# temp files before leaving, because do_patch() will pop up all previously
# applied patches in the start

do_patch[depends] += "quilt-native:do_populate_sysroot"

# see meta-openembedded/meta-networking/recipes-support/netcat/netcat-openbsd_1.195.bb
# or meta-openemedded/meta-oe/recipes-multimedia/id3lib/id3lib_3.8.3.bb
libchrome_do_patch() {
    cd ${S}
    quilt pop -a || true
    if [ -d ${S}/.pc-libchrome ]; then
            rm -rf ${S}/.pc
            mv ${S}/.pc-libchrome ${S}/.pc
            QUILT_PATCHES=${S}/libchrome_tools/patches quilt pop -a
            rm -rf ${S}/.pc
    fi
    # The patches are not numbered, so we need to use the order in the
    # patches file
    for f in $(grep "\.patch$" ${S}/libchrome_tools/patches/patches); do
        QUILT_PATCHES=${S}/libchrome_tools/patches quilt import ${S}/libchrome_tools/patches/${f}
        QUILT_PATCHES=${S}/libchrome_tools/patches quilt push ${f}
    done
    mv ${S}/.pc ${S}/.pc-libchrome
}

do_unpack[cleandirs] += "${S}"

# We invoke base do_patch at end, to incorporate any local patches
python do_patch() {
    bb.build.exec_func('libchrome_do_patch', d)
    bb.build.exec_func('patch_do_patch', d)
}

PACKAGECONFIG ??= "crypto dbus mojo"

# Description of all the possible PACKAGECONFIG fields:
# 1. Extra arguments that should be added to the configure script argument list (EXTRA_OECONF or PACKAGECONFIG_CONFARGS) if the feature is enabled.
# 2. Extra arguments that should be added to EXTRA_OECONF or PACKAGECONFIG_CONFARGS if the feature is disabled.
# 3. Additional build dependencies (DEPENDS) that should be added if the feature is enabled.
# 4. Additional runtime dependencies (RDEPENDS) that should be added if the feature is enabled.
# 5. Additional runtime recommendations (RRECOMMENDS) that should be added if the feature is enabled.
# 6. Any conflicting (that is, mutually exclusive) PACKAGECONFIG settings for this feature.

PACKAGECONFIG[crypto] = ",,nss openssl,,,"
PACKAGECONFIG[dbus] = ",,dbus protobuf,,,"

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches
# and command-line switches (extra dependencies should not
# be necessary but are OK to add).
PACKAGECONFIG[asan] = ""
PACKAGECONFIG[board_use_mistral] = ""
PACKAGECONFIG[coverage] = ""
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[media_perception] = ""
PACKAGECONFIG[mojo] = ""
PACKAGECONFIG[msan] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[ubsan] = ""


GN_ARGS += 'platform_subdir="../platform/${BPN}"'

GN_ARGS += ' \
    use={ \
        asan=${@bb.utils.contains('PACKAGECONFIG', 'asan', 'true', 'false', d)} \
        board_use_mistral=${@bb.utils.contains('PACKAGECONFIG', 'board_use_mistral', 'true', 'false', d)} \
        coverage=${@bb.utils.contains('PACKAGECONFIG', 'coverage', 'true', 'false', d)} \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        crypto=${@bb.utils.contains('PACKAGECONFIG', 'crypto', 'true', 'false', d)} \
        dbus=${@bb.utils.contains('PACKAGECONFIG', 'dbus', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        media_perception=${@bb.utils.contains('PACKAGECONFIG', 'media_perception', 'true', 'false', d)} \
        mojo=${@bb.utils.contains('PACKAGECONFIG', 'mojo', 'true', 'false', d)} \
        msan=${@bb.utils.contains('PACKAGECONFIG', 'msan', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        ubsan=${@bb.utils.contains('PACKAGECONFIG', 'ubsan', 'true', 'false', d)} \
    } \
'

do_compile() {
    # FIXME: this should really be without ${BPN} but fails currently
    # ninja: error: '../src/platform/libchrome/mojo/public/cpp/base/file.typemap',
    # needed by 'gen/ABS_PATH/.../src/platform/libchrome/mojom_type_mappings_typemapping',
    # missing and no known rule to make it
    ninja -C ${B} ${BPN}
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
    PKGCONFIGS=$(find ${B} -name *.pc)
    for pkgconfig in $PKGCONFIGS; do
	install -m 0644 $pkgconfig ${D}${libdir}/pkgconfig/
    done

    INCLUDE_DIRS="base build components crypto dbus ipc mojo testing third_party"
    install -d ${D}${includedir}/libchrome
    for dir in ${INCLUDE_DIRS}; do
        find ${S}/${dir} -name *.h | sed -e "s,${S}/,,g" | while read f; do
            install -D -m 0644 ${S}/${f} ${D}${includedir}/libchrome/${f}
        done
    done
}

FILES:${PN} = "${libdir}/*"

FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"

FILES:${PN}-dev = "${includedir}/*"

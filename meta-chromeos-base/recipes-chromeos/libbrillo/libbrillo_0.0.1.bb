SUMMARY = "Base library for Chromium OS"
DESCRIPTON = "Common platform utility library"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/libbrillo/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r2022"

# Unused patches to port to OpenSSL 3.0.y
# file://0001-libbrillo-cryptohome-port-to-evp.patch
# file://0002-libbrillo-streams-deprecated-ERR.patch
SRC_URI += " \
    file://0003-libbrillo-blkdev_utils-static_cast.patch \
"

require recipes-chromeos/files/include/common-mk-update-mm.inc

DEPENDS += "curl grpc libchrome libminijail modp-b64 openssl protobuf protofiles python3-native rootdev"

RDEPENDS:${PN} += " libchrome libminijail"

GN_ARGS += 'platform_subdir="${BPN}"'

PACKAGECONFIG ??= "dbus device_mapper udev usb"

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
#common-mk/external_dependencies/BUILD.gn builds dbus_adaptors for modemmanager
PACKAGECONFIG[dbus] = ",,dbus dbus-glib libdbus-c++-native modemmanager system-api,,,"
PACKAGECONFIG[device_mapper] = ",,github.com-golang-protobuf-native protobuf-native lvm2 libdevmapper,,,"
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[lvm_stateful_partition] = ",,lvm2,,,"
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[udev] = ",,udev,,,"
PACKAGECONFIG[usb] = ",,libusb,,,"

GN_ARGS += "clang_cc=true clang_cxx=true"
GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        dbus=${@bb.utils.contains('PACKAGECONFIG', 'dbus', 'true', 'false', d)} \
        device_mapper=${@bb.utils.contains('PACKAGECONFIG', 'device_mapper', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        lvm_stateful_partition=${@bb.utils.contains('PACKAGECONFIG', 'lvm_stateful_partition', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        udev=${@bb.utils.contains('PACKAGECONFIG', 'udev', 'true', 'false', d)} \
        usb=${@bb.utils.contains('PACKAGECONFIG', 'usb', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -v -C ${B}
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

    install -d ${D}${includedir}
    # Install all the header files from libbrillo/brillo/*.h into
    # /usr/include/brillo (recursively, with sub-directories).
    oldpwd=${PWD}
    cd ${S}
    DIRS=$(find brillo -type d -print)
    for dir in $DIRS; do
        install -d ${D}${includedir}/${dir}
        install -m 0644 ${dir}/*.h ${D}${includedir}/${dir}
    done
    cd ${oldpwd}

    install -d ${D}${includedir}/policy/
    install -m 0644 ${S}/policy/*.h ${D}${includedir}/policy/

    install -d ${D}${includedir}/install_attributes/
    install -m 0644 ${S}/install_attributes/libinstallattributes.h ${D}${includedir}/install_attributes/
}

SUMMARY="An open-source project for performance instrumentation and tracing."
DESCRIPTION="An open-source project for performance instrumentation and tracing."
HOMEPAGE = "https://perfetto.dev/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb7e2e0af1d4971360553aedadee8d86"

require recipes-chromeos/chromiumos-platform2/chromiumos-platform2.inc

PR = "r2"

SRC_URI += "git://android.googlesource.com/platform/external/${BPN};protocol=https;branch=master;destsuffix=src/aosp/platform/external/${BPN}"
SRCREV = "bdb7121b116bf653116400b69687debe6b1b2b41"

inherit pkgconfig

DEPENDS = "gn-native ninja-native protobuf-native"

S = "${WORKDIR}/src/aosp/platform/external/${BPN}"
B = "${WORKDIR}/build"

do_configure() {
    cat > ${B}/args.gn << EOF
pkg_config="${STAGING_BINDIR_NATIVE}/pkg-config"
libdir="${STAGING_LIBDIR}"
platform_subdir="../aosp/platform/external/${BPN}"
platform2_root="${WORKDIR}/src/platform2"
cxx="${CXX}"
cc="${CC}"
ar="${AR}"
use={cros_host=false fuzzer=false profiling=false tcmalloc=false}
EOF
    cd ${S}
    gn gen ${B} --root=${WORKDIR}/src/platform2
}

do_configure[cleandirs] += "${B}"

do_compile() {
    ninja -C ${B} ${AOSP_PN}
}

do_install () {
    install -d ${D}${includedir}/${AOSP_PN}
    install -m 0644 ${S}/${AOSP_PN}/modp_b64.h ${D}${includedir}/${AOSP_PN}
    install -d ${D}${libdir}
    install -m 0644 ${B}/libmodp_b64.a ${D}${libdir}
}

FILES:${PN}-dev = "${includedir}/${AOSP_PN}/*"
FILES:${PN}-staticdev = "${libdir}/libmodp_b64.a"

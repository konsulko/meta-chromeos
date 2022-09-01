SUMMARY = "Base64 encoder/decoder library."
DESCRIPTION = "Base64 encoder/decoder library."
HOMEPAGE = "https://github.com/client9/stringencoders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb7e2e0af1d4971360553aedadee8d86"

inherit chromeos_gn

PR = "r240"

AOSP_PN = "modp_b64"

SRC_URI += "git://chromium.googlesource.com/aosp/platform/external/modp_b64;protocol=https;branch=master;destsuffix=src/third_party/${AOSP_PN}"
SRCREV = "269b6fb8401617b85e2dff7ae8a7b0f97613e2cd"

S = "${WORKDIR}/src/third_party/${AOSP_PN}"
B = "${WORKDIR}/build"

GN_ARGS += 'platform_subdir="../third_party/${AOSP_PN}"'
GN_ARGS += ' use={ \
    cros_host=false \
    fuzzer=false \
    profiling=false \
    tcmalloc=false \
    } \
'

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

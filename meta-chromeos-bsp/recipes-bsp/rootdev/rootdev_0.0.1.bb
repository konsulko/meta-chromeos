SUMMARY = "Chrome OS root block device tool/library"
DESCRIPTION = "Chrome OS root block device tool/library"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/rootdev/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=562c740877935f40b262db8af30bca36"

SRC_URI += "git://chromium.googlesource.com/chromiumos/third_party/rootdev;protocol=https;branch=main"
SRCREV = "b2f37be7c25bc83b76f1b7063a4ef38b824dc4ef"

S = "${WORKDIR}/git"
PR = "r38"

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
PACKAGECONFIG[asan] = ""

do_configure() {
    :
}

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/rootdev ${D}${bindir}/

    install -d ${D}${libdir}
    install -m 0755 ${B}/librootdev.so.1.0 ${D}${libdir}/
    ln -sf librootdev.so.1.0  ${D}${libdir}/librootdev.so.1
    ln -sf librootdev.so.1.0  ${D}${libdir}/librootdev.so

    install -d ${D}${includedir}/rootdev
    install -m 0644 ${S}/rootdev.h ${D}${includedir}/rootdev/
}


FILES:${PN}-dev += "${includedir}/rootdev/rootdev.h"

SUMMARY = "helper binary and library for sandboxing & restricting privs of services"
DESCRIPTION = "helper binary and library for sandboxing & restricting privs of services"
HOMEPAGE = "https://android.googlesource.com/platform/external/minijail"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit pkgconfig

SRC_URI += " \
    git://android.googlesource.com/platform/external/minijail;protocol=https;branch=master \
"
SRCREV = "a221d24974995483d7bb54a59e10f98b513ce803"

S = "${WORKDIR}/git"
PR = "r4"

DEPENDS = "libcap linux-libc-headers"

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
PACKAGECONFIG[asan] = ""
PACKAGECONFIG[crosdebug] = ""
PACKAGECONFIG[default-ret-log] = ""
PACKAGECONFIG[seccomp] = ""
PACKAGECONFIG[test] = ",,gtest,,,"


do_compile() {
    oe_runmake LIBDIR=${libdir}
    sed -e "s/@BSLOT@/${PV}/g" -e "s,@INCLUDE_DIR@,${includedir}/chromeos,g" \
           "${S}/libminijail.pc.in" > libminijail.pc || die

}

PACKAGES += "libminijail"

export SO_VERSION="1"

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${B}/libminijailpreload.so ${D}${libdir}/libminijailpreload.so.${SO_VERSION}
    install -m 0755 ${B}/libminijail.so ${D}${libdir}/libminijail.so.${SO_VERSION}
    ln -sf libminijailpreload.so.${SO_VERSION} ${D}${libdir}/libminijailpreload.so
    ln -sf libminijail.so.${SO_VERSION} ${D}${libdir}/libminijail.so

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${B}/libminijail.pc ${D}${libdir}/pkgconfig/

    install -d ${D}${bindir}
    install -m 0755 ${B}/minijail0 ${D}${bindir}

    install -d ${D}${includedir}/chromeos
    install -m 0644 ${S}/libminijail.h ${D}${includedir}/chromeos/
    install -m 0644 ${S}/scoped_minijail.h ${D}${includedir}/chromeos/
}

FILES:${PN} = "${bindir}"
FILES:libminijail = "${libdir}/*"
PROVIDES += "libminijail"
RPROVIDES:libminijail += "libminijail.so()(64bit)"

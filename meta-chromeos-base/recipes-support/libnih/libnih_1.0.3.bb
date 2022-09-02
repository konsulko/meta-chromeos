# Portions borrowed from https://github.com/openwebos/meta-webos/blob/master/recipes-upstreamable/libnih/libnih_1.0.3.bb
# Copyright (c) 2013  LG Electronics, Inc.

SUMMARY = "Light-weight 'standard library' of C functions"
DESCRIPTION = "libnih is a small library for C application development \
    containing functions that, despite its name, are not implemented \
    elsewhere in the standard library set. \
    \
    libnih is roughly equivalent to other C libraries such as glib, \
    except that its focus is on a small size and intended for \
    applications that sit very low in the software stack, especially \
    outside of /usr. \
    \
    It expressly does not reimplement functions that already exist in \
    libraries ordinarily shipped in /lib such libc6, and does not do \
    foolish things like invent arbitrary typedefs for perfectly good C types."
HOMEPAGE = "https://launchpad.net/libnih"
SECTION = "libs"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"


inherit autotools gettext pkgconfig

DEPENDS = "virtual/libintl libtool-native libnih-native"
DEPENDS:class-native = "dbus-native"

SRC_URI = "https://launchpad.net/${BPN}/1.0/${PV}/+download/${BP}.tar.gz;name=tarball"
SRC_URI[tarball.sha256sum] = "897572df7565c0a90a81532671e23c63f99b4efde2eecbbf11e7857fbc61f405"

require recipes-chromeos/chromiumos-overlay/chromiumos-overlay.inc

LIBNIH_FILESDIR="${WORKDIR}/src/third_party/chromiumos-overlay/sys-libs/libnih/files"

# chromiumos-overlay/sys-libs/libnih contains a series of patches maintained by quilt(?).
# So manually apply them before applying other local patches. Also remove all
# temp files before leaving, because do_patch() will pop up all previously
# applied patches in the start

do_patch[depends] += "quilt-native:do_populate_sysroot"

# see meta-openembedded/meta-networking/recipes-support/netcat/netcat-openbsd_1.195.bb
# or meta-openemedded/meta-oe/recipes-multimedia/id3lib/id3lib_3.8.3.bb
libnih_do_patch() {
    cd ${S}
    quilt pop -a || true
    if [ -d ${S}/.pc-libnih ]; then
	    rm -rf ${S}/.pc
	    mv ${S}/.pc-libnih ${S}/.pc
	    quilt pop -a
	    rm -rf ${S}/.pc
    fi
    # The patches are not numbered, so we need to use the order in the
    # libnih-*.ebuild file
    #for patch in optional-dbus pkg-config signal-race fno-common expat-2.2.5 glibc-2.24 fix-assert-not-reach-logic fix-test-to-reflect-changes-in-outputs test_main-Disable-textdomain-test; do
    for patch in optional-dbus signal-race fno-common expat-2.2.5 glibc-2.24 fix-assert-not-reach-logic fix-test-to-reflect-changes-in-outputs test_main-Disable-textdomain-test; do
        quilt import -f ${LIBNIH_FILESDIR}/${BP}-${patch}.patch
        quilt push ${BP}-${patch}.patch
    done
    mv ${S}/.pc ${S}/.pc-libnih
}

do_unpack[cleandirs] += "${S}"

# We invoke base do_patch at end, to incorporate any local patches
python do_patch() {
    bb.build.exec_func('libnih_do_patch', d)
    bb.build.exec_func('patch_do_patch', d)
}

do_configure:append() {
   sed -i "s/@INTL_LIBTOOL_SUFFIX_PREFIX@/l/g" ${S}/intl/Makefile.in
}

#EXTRA_OEMAKE += "INSTALLPREFIX=${prefix}"

CFLAGS:append = " -DINSTALLPREFIX=\\"${prefix}\\""
TARGET_CC_ARCH:append = " -fPIC"

PACKAGECONFIG ??= "dbus nls threads"

PACKAGECONFIG[dbus] = "--with-dbus,--without-dbus,expat dbus,expat dbus,,"
PACKAGECONFIG[nls] = "--enable-nls,--disable-nls,,,,"
PACKAGECONFIG[static-libs] = "--enable-static,--disable-static,,,,"
PACKAGECONFIG[threads] = "--enable-threading,--disable-threading,,,,"

# target libnih requires native nih-dbus-tool
BBCLASSEXTEND = "native"

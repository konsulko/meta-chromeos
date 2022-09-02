SUMMARY = "An event-based replacement for the init daemon"
DESCRIPTION = "An event-based replacement for the init daemon"
HOMEPAGE = "https://upstart.ubuntu.com/"
SECTION = "base"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

inherit autotools pkgconfig update-alternatives
inherit gettext

DEPENDS = "dbus expat intltool json-c libnih libnih-native udev"

S = "${WORKDIR}/git"
PR = "r65"

SRC_URI = "git://chromium.googlesource.com/chromiumos/third_party/upstart;branch=chromeos-1.2;protocol=https;name=upstart"
SRCREV = "2a2330a6114bef0a5c5bff7afe0d32a911b3f774"

SRC_URI += "\
    file://0001-upstart-1.2-gettext-updates.patch \
"

require recipes-chromeos/chromiumos-overlay/chromiumos-overlay.inc

UPSTART_FILESDIR = "${WORKDIR}/src/third_party/chromiumos-overlay/sys-apps/upstart/files"

PACKAGECONFIG ??= "\
    ${@bb.utils.filter("DISTRO_FEATURES", "selinux", d)} \
    nls \
"

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
PACKAGECONFIG[debug] = ""
PACKAGECONFIG[direncryption] = "--with-dircrytpo-keyring,,e2fsprogs,keyutils,,"
PACKAGECONFIG[examples] = ""
PACKAGECONFIG[nls] = "--enable-nls,--disable-nls,gettext,,,"
# FIXME:  --with-seccomp-constants="${SYSROOT}/build/share/constants.json"
PACKAGECONFIG[global_seccomp] = "--with-seccomp-constants='',,,minijail,,"
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,,libselinux libsepol selinux-policy,,"
PACKAGECONFIG[udev_bridge] = ",,virtual/libudev,virtual/libudev,,"


# libupstart can be used for upstart event generation from other programs.
# However it is not used by upstart itself, so package it separately.
PACKAGES =+ "libupstart libupstart-dev libupstart-staticdev ${PN}-systemd ${PN}-udev-bridge"

# dynamic libupstart libs for the target device
FILES:libupstart += "${libdir}/libupstart.so.*"

# add these two to libupstart above to build for libupstart
FILES:libupstart-dev += "${libdir}/libupstart.la ${libdir}/libupstart.so ${includedir}/upstart/ ${libdir}/pkgconfig/*"
FILES:libupstart-staticdev += "${libdir}/libupstart.a"

# this moves upstart-*-bridge.conf and binaries from /init, /sbin and /usr/share/upstart/* to -udev-bridge, and system.d files to -systemd ipks
FILES:${PN}-systemd = "${sysconfdir}/dbus-1/*"
FILES:${PN}-udev-bridge = "${sysconfdir}/init/* ${base_sbindir}/*bridge ${datadir}/upstart/*"

# this moves these files to the -dev ipk since they may be of use to developers
FILES:${PN}-dev += "${base_bindir}/init-checkconf ${base_bindir}/initctl2dot ${base_bindir}/upstart-monitor"
FILES:${PN}-dev += "${webos_install_datadir}/applications/upstart-monitor"

# see meta-openembedded/meta-networking/recipes-support/netcat/netcat-openbsd_1.195.bb
# or meta-openemedded/meta-oe/recipes-multimedia/id3lib/id3lib_3.8.3.bb
upstart_do_patch() {
    cd ${S}
    quilt pop -a || true
    if [ -d ${S}/.pc-upstart ]; then
            rm -rf ${S}/.pc
            mv ${S}/.pc-upstart ${S}/.pc
            quilt pop -a
            rm -rf ${S}/.pc
    fi
    # The patches are not numbered, so we need to use the order in the
    # upstart-*.ebuild file
    for patch in log-verbosity; do
        quilt import -f ${UPSTART_FILESDIR}/${BP}-${patch}.patch
        quilt push ${BP}-${patch}.patch
    done
    mv ${S}/.pc ${S}/.pc-upstart
}

do_unpack[cleandirs] += "${S}"

# We invoke base do_patch at end, to incorporate any local patches
python do_patch() {
    if bb.utils.contains('PACKAGECONFIG', 'debug', True, False, d):
        bb.build.exec_func('upstart_do_patch', d)
    bb.build.exec_func('patch_do_patch', d)
}

EXTRA_OEMAKE += "'bindir=${base_bindir}' \
                 'sbindir=${base_sbindir}' \
                 'usrbindir=${bindir}' \
                 'usrsbindir=${sbindir}' \
                 'includedir=${includedir}' \
                 'mandir=${mandir}'"

# append-lfs-flags from portage-stable/eclass/flag-o-matic.eclass 
# http://www.gnu.org/s/libc/manual/html_node/Feature-Test-Macros.html
# _LARGEFILE_SOURCE: enable support for new LFS funcs (ftello/etc...)
# _LARGEFILE64_SOURCE: enable support for 64bit variants (off64_t/fseeko64/etc...)
# _FILE_OFFSET_BITS: default to 64bit variants (off_t is defined as off64_t)
CPPFLAGS += "-D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE -D_LARGEFILE64_SOURCE"

do_install () {
    oe_runmake 'DESTDIR=${D}' install
    if ! ${@bb.utils.contains('PACKAGECONFIG', 'examples', 'true', 'false', d)} ; then
        rm ${D}${sysconfdir}/init/*.conf
    fi
    install -d ${D}${sysconfdir}/init
    # Always use our own upstart-socket-bridge.conf
    install -m 0644 ${UPSTART_FILESDIR}/init/upstart-socket-bridge.conf ${D}${sysconfdir}/init/
    # Restore udev bridge if requested.
    if ${@bb.utils.contains('PACKAGECONFIG', 'udev_bridge', 'true', 'false', d)} ; then
	install -m 0644 ${S}/extra/conf/upstart-udev-bridge.conf ${D}${sysconfdir}/init/
    fi
}
ALTERNATIVE:${PN}  = "init reboot halt poweroff shutdown telinit"

ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"
ALTERNATIVE_LINK_NAME[reboot] = "${base_sbindir}/reboot"
ALTERNATIVE_LINK_NAME[halt] = "${base_sbindir}/halt"
ALTERNATIVE_LINK_NAME[poweroff] = "${base_sbindir}/poweroff"
ALTERNATIVE_LINK_NAME[shutdown] = "${base_sbindir}/shutdown"
ALTERNATIVE_LINK_NAME[telinit] = "${base_sbindir}/telinit"

ALTERNATIVE_PRIORITY[init] = "60"
ALTERNATIVE_PRIORITY[reboot] = "210"
ALTERNATIVE_PRIORITY[halt] = "210"
ALTERNATIVE_PRIORITY[poweroff] = "210"
ALTERNATIVE_PRIORITY[shutdown] = "210"
ALTERNATIVE_PRIORITY[telinit] = "210"

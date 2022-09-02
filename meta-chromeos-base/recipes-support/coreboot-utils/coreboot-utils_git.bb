DESCRIPTION = "Utilities for modifying coreboot firmware images"
SECTION = "coreboot"
HOMEPAGE = "https://coreboot.org"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = " \
    gitsm://github.com/coreboot/coreboot.git;protocol=https;branch=${COREBOOT_BRANCH} \
"
# file://0001-utils-do-not-override-compiler-variables.patch

COREBOOT_VERSION = "4.17"
COREBOOT_BRANCH = "master"
SRCREV = "8da4bfe5b573f395057fbfb5a9d99b376e25c2a4"

PV = "${COREBOOT_VERSION}+${SRCREV}"

# this indicates the folder to run do_compile from.
S="${WORKDIR}/git"

PACKAGES =+ "${PN}-cbfstool ${PN}-ifdtool"

FILES:${PN}-cbfstool = "${bindir}/cbfstool"
FILES:${PN}-ifdtool = "${bindir}/ifdtool"

# INSANE_SKIP_cbfstool = "ldflags"
# INSANE_SKIP_ifdtool = "ldflags"

# this command will be run to compile your source code. it assumes you are in the
# directory indicated by S. i'm just going to use make and rely on my Makefile
do_compile () {
    oe_runmake -C util/cbfstool TOOLLDFLAGS="${LDFLAGS}"
    oe_runmake -C util/ifdtool TOOLLDFLAGS="${LDFLAGS}"
}
 
# this will copy the compiled file and place it in ${bindir}, which is /usr/bin
do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/util/cbfstool/cbfstool ${D}${bindir}
    install -m 0755 ${S}/util/ifdtool/ifdtool ${D}${bindir}
}


BBCLASSEXTEND = "native nativesdk"

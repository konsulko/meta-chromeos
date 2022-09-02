DESCRIPTION = "Handwriting Library used by Language Pack for Chromium OS"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

SRC_URI = "https://gsdview.appspot.com/chromeos-localmirror/distfiles/libhandwriting-${PV}.tar.gz;subdir=libhandwriting-${PV}"
SRC_URI[sha256sum] = "d9acc34334e04682d9fc5fb5b4bdf23d6938a5212a9f9da300051b46efa9a2b1"

inherit dlc

# DLC variables.
# Allocate 4KB * 7500 = 30MB
DLC_PREALLOC_BLOCKS = "7500"

S="${WORKDIR}/libhandwriting-${PV}"

PACKAGECONFIG ??= "dlc ondevice_handwriting"

PACKAGECONFIG[dlc] = ",,,,,"
PACKAGECONFIG[ondevice_handwriting] = ",,,,,"

# Only amd64 and arm versions are available? no arm64
LIBHANDWRITING_ARCH = "`echo ${TARGET_ARCH} | sed s,i.86,amd64, | sed s,aarch64.*,arm, | sed s,armeb,arm,`"
do_install() {
    install -d `dlc_add_path /`
    # FIXME: dlc_do_install still needs work
    #dlc_do_install
    install -m 0755 ${S}/libhandwriting-${LIBHANDWRITING_ARCH}.so ${D}/dlc/
}

INSANE_SKIP:${PN} += "already-stripped"
FILES:${PN} += "/dlc/*"

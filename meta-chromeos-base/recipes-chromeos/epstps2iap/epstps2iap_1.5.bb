SUMMARY = "ELAN Standalone Trackpoint Firmware Update"
DESCRIPTION = "ELAN Standalone Trackpoint Firmware Update"
HOMEPAGE = "https://github.com/jinglewu/epstps2iap/"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


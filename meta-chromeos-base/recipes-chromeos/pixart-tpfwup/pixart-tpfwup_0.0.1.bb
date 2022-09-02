SUMMARY = "Pixart Touchpad utility tool for Firmware Update"
DESCRIPTION = "Pixart Touchpad utility tool for Firmware Update"
HOMEPAGE = "https://github.com/pixarteswd/pix_tpfwup"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r3"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


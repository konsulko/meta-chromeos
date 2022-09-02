SUMMARY = "Wacom EMR/AES flash for Firmware Update"
DESCRIPTION = "Wacom EMR/AES flash for Firmware Update"
HOMEPAGE = "https://github.com/flying-elephant/wacom_source/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


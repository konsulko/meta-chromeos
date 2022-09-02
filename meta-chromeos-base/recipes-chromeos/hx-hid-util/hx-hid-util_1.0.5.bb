SUMMARY = "Himax I2C-HID Utility for Firmware Update"
DESCRIPTION = "Himax I2C-HID Utility for Firmware Update"
HOMEPAGE = "https://github.com/HimaxSoftware/hx_hid_util"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


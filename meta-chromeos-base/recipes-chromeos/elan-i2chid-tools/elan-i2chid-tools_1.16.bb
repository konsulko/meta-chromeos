SUMMARY = "Elan Touchscreen I2C-HID Tools for Firmware Update"
DESCRIPTION = "Elan Touchscreen I2C-HID Tools for Firmware Update"
HOMEPAGE = "https://github.com/PaulLiang01043/elan_i2chid_tools"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r8"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


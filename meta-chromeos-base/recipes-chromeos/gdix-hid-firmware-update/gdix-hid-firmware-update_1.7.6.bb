SUMMARY = "Goodix HIDRAW Firmware Update Tool"
DESCRIPTION = "Goodix HIDRAW Firmware Update Tool"
HOMEPAGE = "https://github.com/goodix/gdix_hid_firmware_update"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


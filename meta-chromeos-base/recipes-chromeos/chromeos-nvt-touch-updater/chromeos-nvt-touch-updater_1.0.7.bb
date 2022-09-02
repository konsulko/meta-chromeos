SUMMARY = "Novatek Touch Firmware Update"
DESCRIPTION = "Novatek Touch Firmware Update"
HOMEPAGE = "https://github.com/Novatek-MSP/chromeos-nvt-touch-updater"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "Synaptics RMI4 Utilities for Firmware Update"
DESCRIPTION = "Synaptics RMI4 Utilities for Firmware Update"
HOMEPAGE = "https://github.com/aduggan/rmi4utils"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


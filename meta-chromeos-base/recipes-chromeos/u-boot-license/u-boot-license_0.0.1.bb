SUMMARY = "Das U-Boot -- the Universal Boot Loader"
DESCRIPTION = "Das U-Boot -- the Universal Boot Loader"
HOMEPAGE = "https://www.denx.de/wiki/U-Boot"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r2"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


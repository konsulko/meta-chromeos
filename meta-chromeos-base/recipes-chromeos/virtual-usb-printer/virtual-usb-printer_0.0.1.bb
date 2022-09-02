SUMMARY = "Used with USBIP to provide a virtual USB printer for testing."
DESCRIPTION = "Used with USBIP to provide a virtual USB printer for testing."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/virtual-usb-printer/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r420"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "Autotest scripts and tools"
DESCRIPTION = "Autotest scripts and tools"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/autotest/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r16178"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


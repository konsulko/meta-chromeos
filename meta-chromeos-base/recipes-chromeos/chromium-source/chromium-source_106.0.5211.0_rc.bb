SUMMARY = "Source code for the open-source version of Google Chrome web browser"
DESCRIPTION = "Source code for the open-source version of Google Chrome web browser"
HOMEPAGE = "http://www.chromium.org/"
LICENSE = ""

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


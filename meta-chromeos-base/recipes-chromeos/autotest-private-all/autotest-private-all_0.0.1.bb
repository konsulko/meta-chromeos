SUMMARY = "All private autotest tests"
DESCRIPTION = "All private autotest tests"
HOMEPAGE = "https://dev.chromium.org/chromium-os"
LICENSE = "metapackage"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


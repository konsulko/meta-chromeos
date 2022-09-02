SUMMARY = "Android SDK"
DESCRIPTION = "Android SDK"
HOMEPAGE = "http://developer.android.com"
LICENSE = ""

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


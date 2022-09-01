SUMMARY = "ChromeOS implementation of the Lexmark fax-pnh-filter"
DESCRIPTION = "ChromeOS implementation of the Lexmark fax-pnh-filter"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/lexmark-fax-pnh/"
LICENSE = "MPL-2.0"

inherit chromeos_gn
PR = "r178"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "Chrome OS Install Shim (meta package)"
DESCRIPTION = "Chrome OS Install Shim (meta package)"
HOMEPAGE = "https://dev.chromium.org/chromium-os"
LICENSE = "metapackage"

inherit chromeos_gn
PR = "r21"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


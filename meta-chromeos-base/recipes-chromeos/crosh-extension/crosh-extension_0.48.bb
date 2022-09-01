SUMMARY = "The Chromium OS Shell extension (the HTML/JS rendering part)"
DESCRIPTION = "The Chromium OS Shell extension (the HTML/JS rendering part)"
HOMEPAGE = "https://chromium.googlesource.com/apps/libapps/+/master/nassh/doc/chromeos-crosh.md"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r1393"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


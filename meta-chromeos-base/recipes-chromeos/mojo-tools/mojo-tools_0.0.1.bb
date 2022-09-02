SUMMARY = "Mojo python tools for binding generating."
DESCRIPTION = "Mojo python tools for binding generating."
HOMEPAGE = "https://chromium.googlesource.com/chromium/src/+/HEAD/mojo/public/tools/mojom/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r9"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


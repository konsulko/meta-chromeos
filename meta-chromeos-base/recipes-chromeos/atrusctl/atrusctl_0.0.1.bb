SUMMARY = "CrOS daemon for the Atrus speakerphone"
DESCRIPTION = "CrOS daemon for the Atrus speakerphone"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/atrusctl/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r1085"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "Web Page Replay (for testing)"
DESCRIPTION = "Web Page Replay (for testing)"
HOMEPAGE = "https://github.com/catapult-project/catapult/tree/master/web_page_replay_go"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
PR = "r2"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


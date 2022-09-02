SUMMARY = "ChromeOS fingerprint MCU unittest binaries"
DESCRIPTION = "ChromeOS fingerprint MCU unittest binaries"
HOMEPAGE = ""
LICENSE = ""
PR = "r5539"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


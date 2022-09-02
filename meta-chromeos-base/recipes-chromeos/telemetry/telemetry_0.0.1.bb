SUMMARY = "Chromium telemetry dep"
DESCRIPTION = "Chromium telemetry dep"
HOMEPAGE = "http://www.chromium.org/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r7"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


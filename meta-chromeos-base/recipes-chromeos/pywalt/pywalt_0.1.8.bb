SUMMARY = "device for measuring latency of physical sensors and outputs on phones and computers"
DESCRIPTION = "device for measuring latency of physical sensors and outputs on phones and computers"
HOMEPAGE = "https://github.com/google/walt"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r2"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


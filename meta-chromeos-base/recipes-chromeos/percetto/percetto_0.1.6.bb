SUMMARY = "Percetto is a C wrapper for Perfetto SDK."
DESCRIPTION = "Percetto is a C wrapper for Perfetto SDK."
HOMEPAGE = "https://github.com/olvaffe/percetto"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


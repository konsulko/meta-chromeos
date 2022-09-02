SUMMARY = "Tools to obtain gerrit credentials from GCE."
DESCRIPTION = "Tools to obtain gerrit credentials from GCE."
HOMEPAGE = "https://gerrit.googlesource.com/gcompute-tools"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "SELinux policy compiler"
DESCRIPTION = "SELinux policy compiler"
HOMEPAGE = "http://userspace.selinuxproject.org"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


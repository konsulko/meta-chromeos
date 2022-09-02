SUMMARY = "Embedded Controller firmware code"
DESCRIPTION = "Embedded Controller firmware code"
HOMEPAGE = ""
LICENSE = ""
PR = "r13681"

GN_ARGS += 'platform_subdir="${BPN}"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    :
}


SUMMARY = "Board specific autotests"
DESCRIPTION = "Board specific autotests"
HOMEPAGE = ""
LICENSE = "metapackage"
PR = "r2"

GN_ARGS += 'platform_subdir="../platform/${BPN}"'


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


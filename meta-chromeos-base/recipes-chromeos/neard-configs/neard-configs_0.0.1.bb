SUMMARY = "ChromiumOS-specific configuration files for net-wireless/neard"
DESCRIPTION = "ChromiumOS-specific configuration files for net-wireless/neard"
HOMEPAGE = "http://www.chromium.org"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r2"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


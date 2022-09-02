SUMMARY = "Install the upstart jobs that configure the firewall."
DESCRIPTION = "Install the upstart jobs that configure the firewall."
HOMEPAGE = "http://www.chromium.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromiumos_overlay

PR = "r7"

RDEPENDS:${PN} = "chromeos-init iptables"

do_install() {
    install -d ${D}${sysconfdir}/init
    install -m 0644 ${S}/files/*.conf ${D}${sysconfdir}/init
}


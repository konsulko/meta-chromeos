SUMMARY = "Defines the boot-complete boot marker for ChromeOS"
DESCRIPTION = "Defines the boot-complete boot marker for ChromeOS \
(login-prompt-visible signal emitted)"
HOMEPAGE = "http://www.chromium.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromiumos_overlay

PR = "r3"

RDEPENDS:${PN} = "chromeos-login"

do_install() {
    install -d ${D}${sysconfdir}/init
    install -m 0644 ${S}/files/init/*.conf ${D}${sysconfdir}/init/
}

RPROVIDES:${PN} += "virtual/chromeos-bootcomplete"

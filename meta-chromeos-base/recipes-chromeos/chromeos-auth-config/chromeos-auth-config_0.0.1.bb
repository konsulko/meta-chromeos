SUMMARY = "ChromiumOS-specific configuration files for pambase"
DESCRIPTION = "ChromiumOS-specific configuration files for pambase"
HOMEPAGE = "http://www.chromium.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromiumos_overlay features_check

PR = "r11"

REQUIRED_DISTRO_FEATURES = "pam"

RDEPENDS:${PN} = "vboot-reference"

do_install() {
    # Chrome OS: sudo and vt2 are important for system debugging both in
    # developer mode and during development.  These two stanzas allow sudo and
    # login auth as user chronos under the following conditions:
    #
    # 1. password-less access:
    # - system in developer mode
    # - there is no passwd.devmode file
    # - there is no system-wide password set above.
    # 2. System-wide (/etc/shadow) password access:
    # - image has a baked in password above
    # 3. Developer mode password access
    # - user creates a passwd.devmode file with "chronos:CRYPTED_PASSWORD"
    # 4. System-wide (/etc/shadow) password access set by modifying /etc/shadow:
    # - Cases #1 and #2 will apply but failure will fall through to the
    #   inserted password.
    install -d ${D}${sysconfdir}/pam.d
    install -m 0644 ${S}/files/chromeos-auth ${D}${sysconfdir}/pam.d/

    install -m 0644 ${S}/files/include-chromeos-auth ${D}${sysconfdir}/pam.d/sudo
    echo "auth	include	system-auth" >> ${D}${sysconfdir}/pam.d/sudo
    echo "account	include	system-auth" >> ${D}${sysconfdir}/pam.d/sudo
    echo "session	include	system-auth" >> ${D}${sysconfdir}/pam.d/sudo

    install -m 0644 ${S}/files/include-chromeos-auth ${D}${sysconfdir}/pam.d/sudo-i
    echo "auth	include	system-auth" >> ${D}${sysconfdir}/pam.d/sudo-i
    echo "account	include	system-auth" >> ${D}${sysconfdir}/pam.d/sudo-i
    echo "session	include	system-auth" >> ${D}${sysconfdir}/pam.d/sudo-i

    install -m 0644 ${S}/files/include-chromeos-auth ${D}${sysconfdir}/pam.d/login
    echo "auth	include	system-local-login" >> ${D}${sysconfdir}/pam.d/login
    echo "account	include	system-local-login" >> ${D}${sysconfdir}/pam.d/login
    echo "password	include	system-local-login" >> ${D}${sysconfdir}/pam.d/login
    echo "session	include	system-local-login" >> ${D}${sysconfdir}/pam.d/login

    install -d ${D}${bindir}
    install -m 0755 ${S}/files/is_developer_end_user ${D}${bindir}
}


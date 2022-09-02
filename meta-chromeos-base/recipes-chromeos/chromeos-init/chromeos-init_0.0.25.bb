SUMMARY = "Upstart init scripts for Chromium OS"
DESCRIPTON = "CrOS common startup init scripts and boot time helpers"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/init/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn useradd

SRC_URI += "file://0001-chromeos-init-clobber_ui-static-cast.patch"

CHROMEOS_PN = "init"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r4572"


# openssl PROVIDES libcrypto
# metrics PROVIDES libmetrics
DEPENDS += "bootstat libbrillo libchrome metrics openssl re2 rootdev secure-erase-file vboot-reference"

RDEPENDS:${PN} += "\
    tar \
    jq \
    chromeos-common-script \
    tty \
    upstart \
    lsof \
    virtual/chromeos-bootcomplete \
"

PACKAGECONFIG ??= "\
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
    ${@bb.utils.filter('MACHINE_FEATURES', 'tpm2', d)} \
    vtconsole"

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches
# and command-line switches (extra dependencies should not
# be necessary but are OK to add).
PACKAGECONFIG[arcpp] = ""
PACKAGECONFIG[arcvm] = ""
PACKAGECONFIG[cros_embedded] = ""
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[direncryption] = ""
PACKAGECONFIG[encrypted_reboot_vault] = ""
PACKAGECONFIG[encrypted_stateful] = ""
PACKAGECONFIG[frecon] = ",,,frecon,,"
PACKAGECONFIG[fsverity] = ""
PACKAGECONFIG[lvm_stateful_partition] = ""
PACKAGECONFIG[oobe_config] = ",,,oobe_config,,"
PACKAGECONFIG[prjquota] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[s3halt] = ""
PACKAGECONFIG[syslog] = ""
PACKAGECONFIG[systemd] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""
PACKAGECONFIG[tpm2] = ""
PACKAGECONFIG[udev] = ""
PACKAGECONFIG[vivid] = ""
PACKAGECONFIG[vtconsole] = ""

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'
GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        direncryption=${@bb.utils.contains('PACKAGECONFIG', 'direncryption', 'true', 'false', d)} \
        encrypted_reboot_vault=${@bb.utils.contains('PACKAGECONFIG', 'encrypted_reboot_vault', 'true', 'false', d)} \
        encrypted_stateful=${@bb.utils.contains('PACKAGECONFIG', 'encrypted_stateful', 'true', 'false', d)} \
        fsverity=${@bb.utils.contains('PACKAGECONFIG', 'fsverity', 'true', 'false', d)} \
        lvm_stateful_partition=${@bb.utils.contains('PACKAGECONFIG', 'lvm_stateful_partition', 'true', 'false', d)} \
        prjquota=${@bb.utils.contains('PACKAGECONFIG', 'prjquota', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
    } \
'

USERADD_PACKAGES = "${PN}"

# Add the syslog user
# syslog:!:202:202:rsyslog:/dev/null:/bin/false
GROUPADD_PARAM:${PN} = "--system --gid 202 syslog"
USERADD_PARAM:${PN} = "--uid 202 --gid syslog --comment 'rsyslog' --home-dir /dev/null --system --shell /bin/false syslog"

# Create debugfs-access user and group, which is needed by the
# chromeos_startup script to mount /sys/kernel/debug.  This is needed
# by bootstat and ureadahead.
# debugfs-access:!:605:605:access to debugfs:/dev/null:/bin/false
GROUPADD_PARAM:${PN}:append = "; --gid 605 debugfs-access"
USERADD_PARAM:${PN}:append = "; --uid 605 --gid debugfs-access --comment 'access to debugfs' --home-dir /dev/null --system --shell /bin/false debugfs-access"

# Create pstore-access group.
# pstore-access:!:422:debugd,oobe_config_restore
GROUPADD_PARAM:${PN}:append = "; --gid 422 pstore-access"

do_compile() {
    ninja -C ${B}
}

src_install_upstart() {
        install -d ${D}${sysconfdir}/init
        install -d ${D}${sysconfdir}/tmpfiles.d

	if ${@bb.utils.contains('PACKAGECONFIG', 'cros_embedded', 'true', 'false', d)} ; then
		install -m 0644 ${S}/upstart/startup.conf ${D}${sysconfdir}/init/
                install -m 0644 ${S}/tmpfiles.d/chromeos.conf ${D}${sysconfdir}/tmpfiles.d/
		install -m 0644 ${S}/upstart/embedded-init/boot-services.conf ${D}${sysconfdir}/init/

		install -m 0644 ${S}/upstart/report-boot-complete.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/failsafe-delay.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/failsafe.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/pre-shutdown.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/pre-startup.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/pstore.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/reboot.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/system-services.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/uinput.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/upstart/sysrq-init.conf ${D}${sysconfdir}/init/

		if ${@bb.utils.contains('PACKAGECONFIG', 'syslog', 'true', 'false', d)} ; then
                        install -m 0644 ${S}/upstart/collect-early-logs.conf ${D}${sysconfdir}/init/
			install -m 0644 ${S}/upstart/log-rotate.conf ${D}${sysconfdir}/init/
			install -m 0644 ${S}/upstart/syslog.conf ${D}${sysconfdir}/init/
			install -m 0644 ${S}/tmpfiles.d/syslog.conf ${D}${sysconfdir}/tmpfiles.d/
                fi
		if ! ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} ; then
			install -m 0644 ${S}/upstart/cgroups.conf ${D}${sysconfdir}/init/
			install -m 0644 ${S}/upstart/dbus.conf ${D}${sysconfdir}/init/
			install -m 0644 ${S}/tmpfiles.d/dbus.conf ${D}${sysconfdir}/tmpfiles.d/
			if ${@bb.utils.contains('PACKAGECONFIG', 'udev', 'true', 'false', d)} ; then
				install -m 0644 ${S}/upstart/udev*.conf ${D}${sysconfdir}/init/
                        fi
                fi
		if ${@bb.utils.contains('PACKAGECONFIG', 'frecon', 'true', 'false', d)} ; then
			install -m 0644 ${S}/upstart/boot-splash.conf ${D}${sysconfdir}/init/
                fi
        else
		install -m 0644 ${S}/upstart/*.conf ${D}${sysconfdir}/init/
		install -m 0644 ${S}/tmpfiles.d/*.conf ${D}${sysconfdir}/tmpfiles.d/

		if [ ! ${@bb.utils.contains('PACKAGECONFIG', 'arcpp', 'true', 'false', d)} && \
                   ${@bb.utils.contains('PACKAGECONFIG', 'arcvm', 'true', 'false', d)} ]; then
                        sed -i '/^env IS_ARCVM=/s:=0:=1:' \
                                "${D}${sysconfdir}/init/rt-limits.conf" || \
                                bbfatal "Failed to replace is_arcvm in rt-limits.conf"
                fi

		install -d ${D}${sbindir}
		install -m 0744 ${S}/chromeos-disk-metrics ${D}${sbindir}/
		install -m 0744 ${S}/chromeos-send-kernel-errors ${D}${sbindir}/
		install -m 0744 ${S}/display_low_battery_alert ${D}${sbindir}/
        fi

	if ${@bb.utils.contains('PACKAGECONFIG', 's3halt', 'true', 'false', d)} ; then
		install -m 0644 ${S}/upstart/halt/s3halt.conf ${D}${sysconfdir}/init/halt.conf
        else
		install -m 0644 ${S}/upstart/halt/halt.conf ${D}${sysconfdir}/init/
        fi

	if ${@bb.utils.contains('PACKAGECONFIG', 'vivid', 'true', 'false', d)} ; then
		install -m 0644 ${S}/upstart/vivid/vivid.conf ${D}${sysconfdir}/init/
        fi

        use vtconsole && doins upstart/vtconsole/*.conf
	if ${@bb.utils.contains('PACKAGECONFIG', 'vtconsole', 'true', 'false', d)} ; then
		install -m 0644 ${S}/upstart/vtconsole/*.conf ${D}${sysconfdir}/init/
	fi
}

do_install() {
    install -d ${D}${bindir}

    # Install helper to run periodic tasks.
    install -m 0755 ${B}/periodic_scheduler ${D}${bindir}/
    install -m 0755 ${B}/process_killer ${D}${bindir}/

    if ${@bb.utils.contains('PACKAGECONFIG', 'syslog', 'true', 'false', d)} ; then
        # Install log cleaning script and run it daily.
        install -m 0755 ${S}/chromeos-cleanup-logs ${D}${bindir}/

        install -d ${D}${sysconfdir}
        install -m 0644 ${S}/rsyslog.chromeos ${D}${sysconfdir}/
    fi

    install -d ${D}${datadir}/cros
    install -m 0755 ${S}/*_utils.sh ${D}${datadir}/cros/

    install -d ${D}${datadir}/cros/init
    install -m 0755 ${S}/is_feature_enabled.sh ${D}${datadir}/cros/init/

    # We want /sbin, not /usr/sbin, etc.
    install -d ${D}${base_sbindir}

    # Install various utility files.
    install -m 0744 ${S}/killers ${D}${base_sbindir}/

    # Install various helper programs.
    install -m 0744 ${B}/cros_sysrq_init ${D}${base_sbindir}/
    install -m 0744 ${B}/static_node_tool ${D}${base_sbindir}/
    install -m 0744 ${B}/net_poll_tool ${D}${base_sbindir}/
    install -m 0744 ${B}/file_attrs_cleaner_tool ${D}${base_sbindir}/
    install -m 0744 ${B}/usermode-helper ${D}${base_sbindir}/

    # Install startup/shutdown scripts.
    install -m 0744 ${B}/chromeos_startup ${D}${base_sbindir}/
    install -m 0744 ${S}/chromeos_startup.sh ${D}${base_sbindir}/
    install -m 0744 ${S}/chromeos_shutdown ${D}${base_sbindir}/

    # Disable encrypted reboot vault if it is not used.
    if ! ${@bb.utils.contains('PACKAGECONFIG', 'encrypted_reboot_vault', 'true', 'false', d)} ; then
        sed -i '/USE_ENCRYPTED_REBOOT_VAULT=/s:=1:=0:' \
                "${D}${base_sbindir}/chromeos_startup.sh" ||
                bbfatal "Failed to replace USE_ENCRYPTED_REBOOT_VAULT in chromeos_startup"
    fi

    # Enable lvm stateful partition.
    if ${@bb.utils.contains('PACKAGECONFIG', 'lvm_stateful_partition', 'true', 'false', d)} ; then
        sed -i '/USE_LVM_STATEFUL_PARTITION=/s:=0:=1:' \
                "${D}${base_sbindir}/chromeos_startup.sh" ||
                bbfatal "Failed to replace USE_LVM_STATEFUL_PARTITION in chromeos_startup"
    fi

    install -m 0744 ${B}/clobber-state ${D}${base_sbindir}/

    install -m 0744 ${S}/clobber-log ${D}${base_sbindir}/
    install -m 0744 ${S}/chromeos-boot-alert ${D}${base_sbindir}/

    # Install Upstart scripts.
    src_install_upstart

    install -m 0644 ${S}/${@bb.utils.contains('PACKAGECONFIG', 'encrypted_stateful', 'encrypted_stateful', 'unencrypted_stateful', d)}/startup_utils.sh ${D}${base_sbindir}

    # Install LVM conf files.
    install -d ${D}${sysconfdir}/lvm
    install -m 0644 ${S}/lvm.conf ${D}${sysconfdir}/lvm
}

FILES:${PN} += "${datadir}/cros/*"

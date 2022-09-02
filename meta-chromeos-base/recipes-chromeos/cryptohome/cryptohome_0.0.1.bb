SUMMARY = "Encrypted home directories for Chromium OS"
DESCRIPTION = "Encrypted home directories for Chromium OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/cryptohome/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn useradd

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r4448"

DEPENDS:append = " \
    attestation \
    attestation-client \
    biod-proxy \
    bootlockbox-client \
    cbor \
    chaps \
    chromeos-config-tools \
    cryptohome-client \
    e2fsprogs \
    ecryptfs-utils \
    flashmap \
    flatbuffers \
    keyutils \
    libhwsec \
    lvm2 \
    metrics \
    openssl \
    power-manager-client \
    protobuf \
    protofiles \
    rootdev \
    secure-erase-file \
    shill-client \
    system-api \
    tpm-manager \
    tpm-manager-client \
    vboot-reference \
"

GN_ARGS += 'platform_subdir="${BPN}"'

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} device_mapper"

# Description of all the possible PACKAGECONFIG fields (comma delimited):
# 1. Extra arguments that should be added to the configure script argument list (EXTRA_OECONF or PACKAGECONFIG_CONFARGS) if the feature is enabled.
# 2. Extra arguments that should be added to EXTRA_OECONF or PACKAGECONFIG_CONFARGS if the feature is disabled.
# 3. Additional build dependencies (DEPENDS) that should be added if the feature is enabled.
# 4. Additional runtime dependencies (RDEPENDS) that should be added if the feature is enabled.
# 5. Additional runtime recommendations (RRECOMMENDS) that should be added if the feature is enabled.
# 6. Any conflicting (that is, mutually exclusive) PACKAGECONFIG settings for this feature.

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches,
# command-line switches and dependencies.
PACKAGECONFIG[cert_provision] = ""
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[device_mapper] = ",,lvm2,,,"
PACKAGECONFIG[direncription_allow_v2] = ""
PACKAGECONFIG[direncryption] = ""
PACKAGECONFIG[double_extend_pcr_issue] = ""
PACKAGECONFIG[downloads_bind_mount] = ""
PACKAGECONFIG[fuzzer] = ",,trousers,,,"
PACKAGECONFIG[generic_tpm2] = ""
PACKAGECONFIG[kernel-5_15] = ""
PACKAGECONFIG[kernel-5_10] = ""
PACKAGECONFIG[kernel-5_4] = ""
PACKAGECONFIG[kernel-upstream] = ""
PACKAGECONFIG[lvm_stateful_partition] = ",,lvm2,,,"
PACKAGECONFIG[mount_oop] = ""
PACKAGECONFIG[pinweaver] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[selinux] = ""
PACKAGECONFIG[slow_mount] = ""
PACKAGECONFIG[systemd] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ",,dash chromeos-base,,,"
PACKAGECONFIG[tpm] = ",,trousers,,,"
PACKAGECONFIG[tpm_dynamic] = ""
PACKAGECONFIG[tpm_insecure_fallback] = ""
PACKAGECONFIG[tpm2] = ",,trunks,,,"
PACKAGECONFIG[tpm2_simulator] = ""
PACKAGECONFIG[uprev-4-to-5] = ""
PACKAGECONFIG[user_session_isolation] = ""
PACKAGECONFIG[uss_migration] = ""
PACKAGECONFIG[vault_legacy_mount] = ""
PACKAGECONFIG[vtpm_proxy] = ""

GN_ARGS += "clang_cc=true clang_cxx=true"
GN_ARGS += ' \
    use={ \
        cert_provision=${@bb.utils.contains('PACKAGECONFIG', 'cert_provision', 'true', 'false', d)} \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        device_mapper=${@bb.utils.contains('PACKAGECONFIG', 'device_mapper', 'true', 'false', d)} \
        direncription_allow_v2=${@bb.utils.contains('PACKAGECONFIG', 'direncription_allow_v2', 'true', 'false', d)} \
        direncryption=${@bb.utils.contains('PACKAGECONFIG', 'direncryption', 'true', 'false', d)} \
        double_extend_pcr_issue=${@bb.utils.contains('PACKAGECONFIG', 'double_extend_pcr_issue', 'true', 'false', d)} \
        downloads_bind_mount=${@bb.utils.contains('PACKAGECONFIG', 'downloads_bind_mount', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        generic_tpm2=${@bb.utils.contains('PACKAGECONFIG', 'generic_tpm2', 'true', 'false', d)} \
        kernel-5_15=${@bb.utils.contains('PACKAGECONFIG', 'kernel-5_15', 'true', 'false', d)} \
        kernel-5_10=${@bb.utils.contains('PACKAGECONFIG', 'kernel-5_10', 'true', 'false', d)} \
        kernel-5_4=${@bb.utils.contains('PACKAGECONFIG', 'kernel-5_4', 'true', 'false', d)} \
        kernel-upstream=${@bb.utils.contains('PACKAGECONFIG', 'kernel-upstream', 'true', 'false', d)} \
        lvm_stateful_partition=${@bb.utils.contains('PACKAGECONFIG', 'lvm_stateful_partition', 'true', 'false', d)} \
        mount_oop=${@bb.utils.contains('PACKAGECONFIG', 'mount_oop', 'true', 'false', d)} \
        pinweaver=${@bb.utils.contains('PACKAGECONFIG', 'pinweaver', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        selinux=${@bb.utils.contains('PACKAGECONFIG', 'selinux', 'true', 'false', d)} \
        slow_mount=${@bb.utils.contains('PACKAGECONFIG', 'slow_mount', 'true', 'false', d)} \
        systemd=${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        tpm=${@bb.utils.contains('PACKAGECONFIG', 'tpm', 'true', 'false', d)} \
        tpm_dynamic=${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} \
        tpm_insecure_fallback=${@bb.utils.contains('PACKAGECONFIG', 'tpm_insecure_fallback', 'true', 'false', d)} \
        tpm2=${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} \
        tpm2_simulator=${@bb.utils.contains('PACKAGECONFIG', 'tpm2_simulator', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
        uprev-4-to-5=${@bb.utils.contains('PACKAGECONFIG', 'uprev-4-to-5', 'true', 'false', d)} \
        user_session_isolation=${@bb.utils.contains('PACKAGECONFIG', 'user_session_isolation', 'true', 'false', d)} \
        uss_migration=${@bb.utils.contains('PACKAGECONFIG', 'uss_migration', 'true', 'false', d)} \
        vault_legacy_mount=${@bb.utils.contains('PACKAGECONFIG', 'vault_legacy_mount', 'true', 'false', d)} \
        vtpm_proxy=${@bb.utils.contains('PACKAGECONFIG', 'vtpm_proxy', 'true', 'false', d)} \
    } \
'

USERADD_PACKAGES = "${PN}"

# bootlockboxd:!:20107:20107:bootlockbox daemon:/dev/null:/bin/false
USERADD_PARAM:${PN} = "-u 20107 -U -c 'bootlockbox daemon' -d /dev/null -r -s /bin/false bootlockboxd"
# cryptohome:!:292:292:cryptohome service and client:/dev/null:/bin/false
USERADD_PARAM:${PN} = "-u 292 -U -c 'cryptohome service and client' -d /dev/null -r -s /bin/false cryptohome"

do_compile() {
    ninja -C ${B}
}

python check_direncryption_allow_v2() {
    if bb.utils.contains('PACKAGECONFIG', 'direncryption_allow_v2', 'true', 'false', d) and \
        ( not bb.utils.contains_any('PACKAGECONFIG', 'kernel-5_4 kernel-5_10 kernel-5_15 kernel-upstream', 'true', 'false', d) or \
          bb.utils.contains('PACKAGECONFIG', 'uprev-4-to-5', 'true', 'false', d) ):
        bb.fatal("direncription_allow_v2 is enabled where it shouldn't be. Do you need to change the MACHINE?"
                 " Note, uprev boards should have it disabled!")
    elif not bb.utils.contains('PACKAGECONFIG', 'direncryption_allow_v2', 'true', 'false', d) and \
        ( bb.utils.contains_any('PACKAGECONFIG', 'kernel-5_4 kernel-5_10 kernel-5_15 kernel-upstream', 'true', 'false', d) or \
          not bb.utils.contains('PACKAGECONFIG', 'uprev-4-to-5', 'true', 'false', d) ):
        bb.fatal("direncription_allow_v2 is not enabled where it should be. Do you need to change the MACHINE?"
                 " Note, uprev boards should have it disabled!")
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/cryptohomed ${D}${bindir}/
    install -m 0755 ${B}/cryptohome ${D}${bindir}/
    install -m 0755 ${B}/cryptohome-path ${D}${bindir}/
    install -m 0755 ${B}/homedirs_initializer ${D}${bindir}/
    install -m 0755 ${B}/lockbox-cache ${D}${bindir}/
    install -m 0755 ${B}/tpm-manager ${D}${bindir}/
    install -m 0755 ${B}/stateful-recovery ${D}${bindir}/
    install -m 0755 ${B}/cryptohome-namespace-mounter ${D}${bindir}/
    install -m 0755 ${B}/mount-encrypted ${D}${bindir}/
    install -m 0755 ${B}/encrypted-reboot-vault ${D}${bindir}/
    install -m 0755 ${B}/bootlockboxd ${D}${bindir}/
    install -m 0755 ${B}/bootlockboxtool ${D}${bindir}/
    if [ ${@bb.utils.contains('PACKAGECONFIG', 'cert_provision', 'true', 'false', d)} ]; then
        install -m 0755 ${B}/cert_provision_client ${D}${bindir}/
    fi

    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${S}/etc/org.chromium.UserDataAuth.conf ${D}${sysconfdir}/dbus-1/system.d/
    install -m 0644 ${S}/etc/BootLockbox.conf ${D}${sysconfdir}/dbus-1/system.d/

    check_direncryption_allow_v2

    # Install init scripts
    if [ ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} ]; then
        install -d ${D}${systemd_system_unit_dir}
        if [ ${@bb.utils.contains('PACKAGECONFIG', 'tpm2', 'true', 'false', d)} ]; then
            sed 's/tcsd.service/attestationd.service/' \
                ${S}/init/cryptohomed.service > ${B}/cryptohomed.service
            install -m 0644 ${B}/cryptohomed.service ${D}${systemd_system_unit_dir}/
        else
            install -m 0644 ${S}/init/cryptohomed.service ${D}${systemd_system_unit_dir}/
        fi
        install -m 0644 ${S}/init/mount-encrypted.service ${D}${systemd_system_unit_dir}/
        install -m 0644 ${S}/init/lockbox-cache.service ${D}${systemd_system_unit_dir}/

        install -d ${D}${systemd_system_unit_dir}/boot-services.target.wants
        ln -sf ../cryptohomed.service ${D}${systemd_system_unit_dir}/boot-services.target.wants/cryptohomed.service

        install -d ${D}${systemd_system_unit_dir}/system-services.target.wants
        ln -sf ../mount-encrypted.service ${D}${systemd_system_unit_dir}/system-services.target.wants/mount-encrypted.service

        install -d ${D}${systemd_system_unit_dir}/ui.target.wants
        ln -sf ../lockbox-cache.service ${D}${systemd_system_unit_dir}/ui.target.wants/lockbox-cache.service
    else
        install -d ${D}${sysconfdir}/init
        install -m 0644 ${S}/bootlockbox/bootlockboxd.conf ${D}${sysconfdir}/init/
        install -m 0644 ${S}/init/cryptohomed-client.conf ${D}${sysconfdir}/init/
        install -m 0644 ${S}/init/cryptohomed.conf ${D}${sysconfdir}/init/
        install -m 0644 ${S}/init/init-homedirs.conf ${D}${sysconfdir}/init/
        install -m 0644 ${S}/init/send-mount-encrypted-metrics.conf ${D}${sysconfdir}/init/

        if [ ${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} ]; then
            install -m 0644 ${S}/init/lockbox-cache.conf.tpm_dynamic ${D}${sysconfdir}/init/lockbox-cache.conf
        else
            install -m 0644 ${S}/init/lockbox-cache.conf ${D}${sysconfdir}/init/
        fi

        if [ ${@bb.utils.contains('PACKAGECONFIG', 'direncryption', 'true', 'false', d)} ]; then
            sed -i '/env DIRENCRYPTION_FLAG=/s:=.*:="--direncryption":' \
            ${D}${sysconfdir}/init/cryptohomed.conf ||
            bbfatal "Can't replace direncyption flag in cryptohomed.conf"
        fi

        if [ !${@bb.utils.contains('PACKAGECONFIG', 'vault_legacy_mount', 'true', 'false', d)} ]; then
            sed -i '/env NO_LEGACY_MOUNT_FLAG=/s:=.*:="--nolegacymount":' \
            ${D}${sysconfdir}/init/cryptohomed.conf ||
            bbfatal "Can't replace nolegacymount flag in cryptohomed.conf"
        fi

        if [ !${@bb.utils.contains('PACKAGECONFIG', 'downloads_bind_mount', 'true', 'false', d)} ]; then
            sed -i '/env NO_DOWNLOAD_BINDMOUNT_FLAG=/s:=.*:="--no_downloads_bind_mount":' \
            ${D}${sysconfdir}/init/cryptohomed.conf ||
            bbfatal "Can't replace no_downloads_bind_mount flag in cryptohomed.conf"
        fi

        if [ ${@bb.utils.contains('PACKAGECONFIG', 'direncription_allow_v2', 'true', 'false', d)} ]; then
            sed -i '/env FSCRYPT_V2_FLAG=/s:=.*:="--fscrypt_v2":' \
            ${D}${sysconfdir}/init/cryptohomed.conf ||
            bbfatal "Can't replace fscrypt_v2 flag in cryptohomed.conf"
        fi
    fi

    install -d ${D}${datadir}/cros/init
    if [ ${@bb.utils.contains('PACKAGECONFIG', 'tpm_dynamic', 'true', 'false', d)} ]; then
        install -m 0755 ${S}/init/lockbox-cache.sh.tpm_dynamic ${D}${datadir}/cros/init/lockbox-cache.sh
    else
        install -m 0755 ${S}/init/lockbox-cache.sh ${D}${datadir}/cros/init/
    fi

    if [ ${@bb.utils.contains('PACKAGECONFIG', 'cert_provision', 'true', 'false', d)} ]; then
        install -d ${D}${includedir}/cryptohome
        install -m 0644 ${S}/init/cert_provision.h ${D}${includedir}/cryptohome/
    fi

    # Install udev rules for cryptohome
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${S}/udev/50-dm-cryptohome.rules ${D}${sysconfdir}/udev/rules.d/

    # Install seccomp policy for bootlockboxd
    install -d ${D}${datadir}/policy
    install -m 0644 ${S}/bootlockbox/seccomp/bootlockboxd-seccomp-${ARCH}.policy \
                    ${D}${datadir}/policy/bootlockboxd-seccomp.policy

    install -d ${D}${sysconfdir}/tmpfiles.d
    install -m 0644 ${S}/tmpfiles.d/cryptohome.conf ${D}${sysconfdir}/tmpfiles.d

    # TODO platform_fuzzer_install
}

SYSTEMD_SERVICE:${PN} = " \
    ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'cryptohomed.service mount-encrypted.service lockbox-cache.service', '', d)} \
"


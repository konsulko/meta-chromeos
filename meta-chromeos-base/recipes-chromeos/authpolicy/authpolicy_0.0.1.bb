SUMMARY = "Provides authentication to LDAP and fetching device/user policies"
DESCRIPTION = "Provides authentication to LDAP and fetching device/user policies"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/authpolicy/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn useradd

SRC_URI += "\
    file://0001-authpolicy-tgt_manager-static-cast.patch \
"

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r1865"

require recipes-chromeos/files/include/common-mk-update-mm.inc

GN_ARGS += 'platform_subdir="${BPN}"'

# dbusxx-xml2cpp is provided by libdbus-c++-native
# protoc is provided by protbuf-native
# protoc-gen-go is provided by github.com-golang-protobuf-native
# go-generate-chromeos-dbus-bindings is provided by chromeos-dbus-bindings-native
DEPENDS += "\
    chromeos-dbus-bindings-native \
    cryptohome-client \
    dbus \
    github.com-golang-protobuf-native \
    libbrillo \
    libcap \
    libchrome \
    libdbus-c++-native \
    libpcre \
    metrics \
    minijail \
    protobuf \
    protobuf-native \
    protofiles \
    session-manager-client \
    system-api \
"

RDEPENDS:${PN} += "libminijail"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'samba', '', d)}"

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
PACKAGECONFIG[asan] = ""
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[samba] = ",,samba,samba,,"
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""

GN_ARGS += ' \
    use={ \
        asan=${@bb.utils.contains('PACKAGECONFIG', 'asan', 'true', 'false', d)} \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        samba=${@bb.utils.contains('PACKAGECONFIG', 'samba', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
    } \
'

CXX:append = " -I${STAGING_INCDIR}/session_manager-client"

USERADD_PACKAGES = "${PN}"

# authpolicyd:!:254:254:authpolicy daemon:/dev/null:/bin/false
GROUPADD_PARAM:${PN} = "--system --gid 254 authpolicyd"
USERADD_PARAM:${PN} = "--uid 254 --gid authpolicyd --comment 'authpolicy daemon' --home-dir /dev/null --system --shell /bin/false authpolicyd"
# authpolicyd-exec:!:607:607:authpolicy process executor:/dev/null:/bin/false
GROUPADD_PARAM:${PN}:append = "; --system --gid 607 authpolicyd-exec"
USERADD_PARAM:${PN}:append = "; --uid 607 --gid authpolicyd-exec --comment 'authpolicy process executor' --home-dir /dev/null --system --shell /bin/false authpolicyd-exec"

do_compile() {
    ninja -C ${B}
}

do_install() {
    # Create daemon store folder prototype, see
    # https://chromium.googlesource.com/chromiumos/docs/+/HEAD/sandboxing.md#securely-mounting-cryptohome-daemon-store-folders
    install -d -m 0700 ${D}${sysconfdir}/daemon-store/authpolicyd
    chown authpolicyd:authpolicyd ${D}${sysconfdir}/daemon-store/authpolicyd

    install -d ${D}${bindir}
    install -m 0755 ${B}/authpolicyd ${D}${bindir}/
    install -m 0755 ${B}/authpolicy_parser ${D}${bindir}/

    install -d ${D}${includedir}
    install -d ${D}${includedir}/authpolicy/
    install -m 0644 ${B}/gen/include/authpolicy/org.chromium.AuthPolicy.h ${D}${includedir}/authpolicy/
}

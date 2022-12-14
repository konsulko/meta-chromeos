SUMMARY = "Chrome OS system API (D-Bus service names, etc.)"
DESCRIPTION = "Chrome OS system API (D-Bus service names, etc.)"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/system_api/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit cargo chromeos_gn

CHROMEOS_PN = "system_api"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
B = "${WORKDIR}/build"
PR = "r4838"

# workaround for the crate fetcher
do_compile[network] = "1"

# auto generated by cargo-bitbake
# please note if you have entries that do not begin with crate://
# you must change them to how that package can be fetched
SRC_URI += " \
    crate://crates.io/bitflags/1.3.2 \
    crate://crates.io/cfg-if/1.0.0 \
    crate://crates.io/dbus/0.9.6 \
    crate://crates.io/either/1.7.0 \
    crate://crates.io/fastrand/1.8.0 \
    crate://crates.io/instant/0.1.12 \
    crate://crates.io/lazy_static/1.4.0 \
    crate://crates.io/libc/0.2.128 \
    crate://crates.io/libdbus-sys/0.2.2 \
    crate://crates.io/log/0.4.17 \
    crate://crates.io/pkg-config/0.3.25 \
    crate://crates.io/protobuf-codegen/2.27.1 \
    crate://crates.io/protobuf/2.27.1 \
    crate://crates.io/protoc-rust/2.27.1 \
    crate://crates.io/protoc/2.27.1 \
    crate://crates.io/redox_syscall/0.2.16 \
    crate://crates.io/remove_dir_all/0.5.3 \
    crate://crates.io/tempfile/3.3.0 \
    crate://crates.io/which/4.2.5 \
    crate://crates.io/winapi-i686-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi-x86_64-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi/0.3.9 \
"

# protobuf-native RPROVIDES protobuf-compiler-native which is protoc-native
# protobuf-codegen-native RPROVIDES protoc-gen-rust-native
DEPENDS:append = " \
    chromeos-dbus-bindings \
    chromeos-dbus-bindings-rust \
    dbus \
    dbus-codegen-rust-native \
    github.com-golang-protobuf-native \
    protobuf \
    protobuf-codegen \
    protobuf-codegen-native \
    protobuf-native \
"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}"'

PACKAGECONFIG ??= ""

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
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[tcmalloc] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'cros_host', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
    } \
'

do_configure() {
    chromeos_gn_do_configure
}

do_compile() {
    cargo_do_compile
    ninja -C ${B}
}

export SO_VERSION="1"

do_install() {
    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${S}/system_api.pc "${D}$libdir/pkgconfig"
    
    INCLUDE_DIRS="constants dbus switches"
    install -d ${D}${includedir}/chromeos
    for dir in ${INCLUDE_DIRS}; do
        find ${S}/${dir} -name *.h -o -name *.proto | sed -e "s,${S}/,,g" | while read f; do
            install -D -m 0644 ${S}/${f} ${D}${includedir}/chromeos/${f}
        done
    done
    
    # Install the dbus-constants.h files in the respective daemons' client library
    # include directory. Users will need to include the corresponding client
    # library to access these files.
    INCLUDE_DIRS=" \
        arc-data-snapshotd \
        anomaly_detector \
        attestation \
        biod \
        chunneld \
        cros-disks \
        cros_healthd \
        cryptohome \
        debugd \
        discod \
        dlcservice \
        kerberos \
        login_manager \
        lorgnette \
        oobe_config \
        runtime_probe \
        pciguard \
        permission_broker \
        power_manager \
        rgbkbd \
        rmad \
        shill \
        smbprovider \
        tpm_manager \
        update_engine \
        wilco_dtc_supportd \
    "
    for dir in ${INCLUDE_DIRS}; do
        install -d ${D}${includedir}/${dir}-client/${dir}
        install -D -m 0644 ${S}/dbus/${dir}/dbus-constants.h ${D}${includedir}/${dir}-client/${dir}/
    done

    # These are files/projects installed in the common dir.
    oldpwd=${PWD}
    INCLUDE_DIRS="system_api"
    # These are project-specific files.
    cd ${S}/dbus
    INCLUDE_DIRS="${INCLUDE_DIRS} $(dirname */*.proto | sort -u | tr '\n' ' \ \n')"
    cd ${oldpwd}
    for dir in ${INCLUDE_DIRS}; do
        install -d ${D}${includedir}/${dir}/proto_bindings
        install -D -m 0644 ${B}/gen/include/${dir}/proto_bindings/*.h ${D}${includedir}/${dir}/proto_bindings/
    done

    install -d ${D}${libdir}
    install -m 0644 ${B}/libsystem_api*.a ${D}${libdir}/

    find ${B}${base_libdir} -type f -name lib*.so | while read f; do
        fn=$(basename ${f})
        echo ${fn}
        install -m 0755 ${f} ${D}${libdir}/${fn}.${SO_VERSION}
        ln -sf ${fn}.${SO_VERSION} ${D}${libdir}/${fn}
    done

    # TODO: add cros-go.bbclass and cros-go_src_install function
    cd ${B}/gen
    GOSRC_DIRS="$(find . -type f -wholename "*/*.pb.go" -exec 'dirname' {} + | tr '\n' ' ' )"
    for dir in ${GOSRC_DIRS}; do
        echo "gosrc_dir: ${dir}"
        install_dir="$(echo ${dir} | sed -e 's,./go/src,go,g')"
        install -d ${D}${prefix}/src/${install_dir}
        install -m 0644 ${B}/gen/${dir}/*.pb.go ${D}${prefix}/src/${install_dir}/
    done
    cd ${oldpwd}
}

FILES:${PN} = "${libdir}/libsystem_api.so* ${libdir}/pkgconfig/*"

FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"

FILES:${PN}-dev = "${includedir}/*"
FILES:${PN}-staticdev += "${libdir}/*.a"
FILES:${PN}-src += "${prefix}/src/go/chromiumos/system_api/*"

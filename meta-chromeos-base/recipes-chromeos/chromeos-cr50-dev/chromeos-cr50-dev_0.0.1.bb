SUMMARY = "Google Security Chip firmware code"
DESCRIPTION = "Google Security Chip firmware code"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/ec/+/refs/heads/cr50_stab"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit pkgconfig

SRC_URI += "git://chromium.googlesource.com/chromiumos/platform/ec;protocol=https;branch=cr50_stab;destsuffix=src/platform/ec;name=chromiumos-platform-ec"
SRCREV_chromiumos-platform-ec = "9f0ed15c133c1ee33de032da69ba045fdbae9516"

A_IMAGE = "cr50.prod.ro.A.0.0.11"
B_IMAGE = "cr50.prod.ro.B.0.0.11"
SRC_URI += "\
    https://gsdview.appspot.com/chromeos-localmirror/distfiles/${A_IMAGE};name=a \
    https://gsdview.appspot.com/chromeos-localmirror/distfiles/${B_IMAGE};name=b \
"
SRC_URI[a.sha256sum] = "e8ea56f804038c492d8e40d941ad89bb083e2dc51e13889cd12fc7676127692d"
SRC_URI[b.sha256sum] = "c618947c73f2bd225c465ca0aea6c2b35c737cef7c93a89fea238681f042b268"

SRC_URI += "\
    file://0001-ec-Fix-DEBUG-variable-in-usb_updater-Makefile.patch \
"

# | gsctool.c:1986:2: error: ignoring return value of function declared with 'warn_unused_result' attribute [-Werror,-Wunused-result]
# |         getline(&password_copy, &copy_len, stdin);
# |         ^~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
SRC_URI += "\
    file://0002-ec-Ignore-Wunused-result.patch \
"

S = "${WORKDIR}/src/platform/ec"
B = "${WORKDIR}/build"
PR = "r431"

DEPENDS += "flashmap openssl libusb"

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
PACKAGECONFIG[asan] = ""
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[fuzzer] = ",,protobuf,,,"
PACKAGECONFIG[msan] = ""
PACKAGECONFIG[quiet] = ",,,,,verbose"
PACKAGECONFIG[reef] = ""
PACKAGECONFIG[ubsan] = ""
PACKAGECONFIG[verbose] = ",,,,,quiet"

set_build_env() {
    EC_OPTS=""
    EC_OPTS=${@bb.utils.contains('PACKAGECONFIG', 'quiet', '-s "V=0"', '', d)}
    EC_OPTS=${@bb.utils.contains('PACKAGECONFIG', 'verbose', '"V=1"', '', d)}
}

do_compile() {
    cd ${S}
    set_build_env
    export BOARD=cr50
    oe_runmake -C extra/usb_updater clean
    oe_runmake -C extra/usb_updater gsctool
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/extra/usb_updater/gsctool ${D}${sbindir}
    install -m 0755 ${S}/util/chargen ${D}${sbindir}

    if ! ${@bb.utils.contains('PACKAGECONFIG', 'reef', 'true', 'false', d)} ; then
        bbplain "Not installing Cr50 binaries"
        return
    fi

    build_dir="${B}/cr50"
    dest_dir="${D}/firmware/cr50"
    bbinfo "Installing cr50 from ${build_dir} into ${dest_dir}"

    install -d ${dest_dir}
    install -m 0644 ${build_dir}/ec.bin ${dest_dir}/
    install -m 0644 ${build_dir}/RW/ec.RW.elf ${dest_dir}/
    install -m 0644 ${build_dir}/RW/ec.RW_B.elf ${dest_dir}/

    # FIXME:
    # install_cr50_signer_aid
}


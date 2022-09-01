SUMMARY = "Utility for building Chrome D-Bus bindings from an XML description"
DESCRIPTION = "Utility for building Chrome D-Bus bindings from an XML description"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/chromeos-dbus-bindings"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

require recipes-chromeos/chromiumos-platform2/chromiumos-platform2.inc

inherit cargo

RDEPENDS:${PN} = "dbus-codegen-rust"

# workaround for the crate fetcher
do_compile[network] = "1"

CHROMEOS_PN = "chromeos-dbus-bindings"

# please note if you have entries that do not begin with crate://
# you must change them to how that package can be fetched
SRC_URI += " \
    crate://crates.io/dbus-codegen/0.10.0 \
    crate://crates.io/either/1.7.0 \
    crate://crates.io/lazy_static/1.4.0 \
    crate://crates.io/libc/0.2.126 \
    crate://crates.io/which/4.2.5 \
"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}"
PR = "r39"

BBCLASSEXTEND = "native"

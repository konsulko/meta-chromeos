SUMMARY = "Utility for building Chrome D-Bus bindings from an XML description"
DESCRIPTION = "Utility for building Chrome D-Bus bindings from an XML description"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/chromeos-dbus-bindings"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn go

# fetcher is failing to get 'which'
# failed to fetch `https://github.com/rust-lang/crates.io-index`
#inherit cargo

# please note if you have entries that do not begin with crate://
# you must change them to how that package can be fetched
#SRC_URI += " \
#    crate://crates.io/crates/either/1.7.0 \
#    crate://crates.io/crates/lazy_static/1.4.0 \
#    crate://crates.io/crates/libc/0.2.126 \
#    crate://crates.io/crates/which/4.2.5 \
#"

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r2771"

GOHOME="${B}/go"
PKGDIR="${GOHOME}/pkg"

# Package to build to produce the generator executable.
GENERATOR_PKG="go.chromium.org/chromiumos/dbusbindings/cmd/generator"

# Output filename for generator executable.
GENERATOR_OUT="${GOHOME}/bin/go-generate-chromeos-dbus-bindings"

# enforce GOPATH mode
export GO111MODULE="off"

do_configure() {
	ln -snf ${S}/go/src ${B}/
}
do_configure[dirs] =+ "${GOTMPDIR}"

# Builds an executable package to a destination path.
run_build() {
  local pkg="${1}"
  local dest="${2}"
  if [ "${debug}" == 0 ]; then
    ${GO} build -i -pkgdir "${PKGDIR}" -o "${dest}" "${pkg}"
  else
    ${GO} build -ldflags "${GO_BUILD_LDFLAGS} -w -s" -i -pkgdir "${PKGDIR}" -o "${dest}" "${pkg}"
    #${GO} build -gcflags="all=-N -l" -i -pkgdir "${PKGDIR}" -o "${dest}" "${pkg}"
  fi
}

do_compile() {
    run_build "${GENERATOR_PKG}" "${GENERATOR_OUT}"
    #cargo_do_compile
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${GENERATOR_OUT} ${D}${bindir}/
}

INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP:${PN} += "ldflags already-stripped"

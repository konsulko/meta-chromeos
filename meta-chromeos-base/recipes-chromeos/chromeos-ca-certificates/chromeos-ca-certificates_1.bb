SUMMARY = "Chrome OS restricted set of certificates"
DESCRIPTION = "Chrome OS restricted set of certificates"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/docs/+/HEAD/ca_certs.md"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromiumos_overlay python3native

B = "${WORKDIR}/build"
PR = "r6"

DEPENDS = "openssl-native"

do_compile() {
   ${PYTHON} "${S}/files/split-root-certs.py" \
        --extract-to "${B}" \
        --roots-pem "${S}/files/roots.pem" \
        || bbfatal "Couldn't extract certs from roots.pem"
}

do_install() {
    CA_CERT_DIR="${D}${datadir}/chromeos-ca-certificates"
    install -d ${CA_CERT_DIR}
    install -m 0644 ${B}/*.pem ${CA_CERT_DIR}
    ${STAGING_BINDIR_NATIVE}/c_rehash "${CA_CERT_DIR}"
}


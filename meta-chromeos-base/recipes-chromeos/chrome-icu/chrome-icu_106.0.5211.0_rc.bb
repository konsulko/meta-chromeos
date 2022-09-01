SUMMARY = "The ICU library copied from chrome/third_party"
DESCRIPTION = "The ICU library copied from chrome/third_party"
HOMEPAGE = "https://cs.chromium.org/chromium/src/third_party/icu/"
LICENSE = "icu-58"

inherit chromeos_gn
PR = "r2"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "Some system locales when apps need more than C (not for apps using ICU for i18n)"
DESCRIPTION = "Some system locales when apps need more than C (not for apps using ICU for i18n)"
HOMEPAGE = "http://dev.chromium.org/"
LICENSE = "public-domain"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


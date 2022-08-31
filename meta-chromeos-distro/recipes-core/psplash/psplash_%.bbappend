FILESEXTRAPATHS:prepend:chromeos := "${THISDIR}/${PN}:"

SPLASH_IMAGES:chromeos = "file://psplash-chromeos.png;outsuffix=chromeos"

SRC_URI += " \
    file://0001-psplash-poky-chromeos-throughout.patch \
    file://0002-psplash-colors-set-background-to-dark-gray.patch \
    file://psplash-chromeos.png \
    file://psplash-bar.png \
    "

do_configure:prepend () {
    cp ${WORKDIR}/psplash-chromeos.png ${S}/base-images/
    cp ${WORKDIR}/psplash-bar.png ${S}/base-images/
}

SUMMARY = "Shill DBus client interface library"
DESCRIPTION = "Shill DBus client interface library"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/shill/dbus/client"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "shill"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}/dbus/client"
B = "${WORKDIR}/build"
PR = "r958"

DEPENDS += "shill-client shill-net"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}/dbus/client"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    # Install libshill-dbus-client library.
    install -d ${D}${libdir}/pkgconfig
    # local v="$(libchrome_ver)"
    # ./preinstall.sh "${OUT}" "${v}"
    #    dolib.so "${OUT}/lib/libshill-dbus-client.so"
    #    doins "${OUT}/lib/libshill-dbus-client.pc"

    # Install header files from libshill-dbus-client
    install -d ${D}${includedir}/shill/dbus/client
    install -m 0644 ${S}/*.h ${D}${includedir}/shill/dbus/client/
}


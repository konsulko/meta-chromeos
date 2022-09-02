SUMMARY = "Cryptohome D-Bus client library for Chromium OS"
DESCRIPTION = "Cryptohome D-Bus client library for Chromium OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/cryptohome"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn

CHROMEOS_PN = "cryptohome"

S = "${WORKDIR}/src/platform2/${CHROMEOS_PN}/client"
B = "${WORKDIR}/build"
PR = "r2044"

DEPENDS += "chromeos-dbus-bindings-native"

GN_ARGS += 'platform_subdir="${CHROMEOS_PN}/client"'


do_compile() {
    ninja -C ${B}
}

do_install() {
    install -d ${D}${includedir}/user_data_auth-client/user_data_auth
    install -m 0644 ${B}/gen/include/user_data_auth/dbus-proxies.h \
		    ${D}${includedir}/user_data_auth-client/user_data_auth/
    install -d ${D}${includedir}/user_data_auth-client-test/user_data_auth
    install -m 0644 ${B}/gen/include/user_data_auth/dbus-proxy-mocks.h \
		    ${D}${includedir}/user_data_auth-client-test/user_data_auth/

    install -d ${D}${libdir}/pkgconfig
    sed \
      -e "s|@INCLUDE_DIR@|${includedir}|g" \
      -e "s|@PV@|${PV}|g" \
         "${S}/libuser_data_auth-client.pc.in" > "${D}${libdir}/pkgconfig/libuser_data_auth-client.pc"

     sed \
       -e "s|@INCLUDE_DIR@|${includedir}|g" \
       -e "s|@PV@|${PV}|g" \
          "${S}/libuser_data_auth-client-test.pc.in" > "${D}${libdir}/pkgconfig/libuser_data_auth-client-test.pc"

}


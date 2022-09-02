platform_install_dbus_client_lib() {
    libname=${1:-${PN}}

    client_includes=${includedir}/${libname}-client
    client_test_includes=${includedir}/${libname}-client-test

    # Install DBus proxy headers.
    install -d ${D}${client_includes}/${libname}
    install -m 0644 ${B}/gen/include/${libname}/dbus-proxies.h \
                    ${D}${client_includes}/${libname}/
    install -d ${D}${client_test_includes}/${libname}
    install -m 0644 ${B}/gen/include/${libname}/dbus-proxy-mocks.h \
                    ${D}${client_test_includes}/${libname}/

    if [ -f "${S}/lib${libname}-client.pc.in" ]; then
        # Install pkg-config for client libraries
        install -d ${D}${libdir}/pkgconfig
        sed \
	   -e "s|@PV@|${PV}|g" \
           -e "s|@INCLUDE_DIR@|${includedir}|g" \
              "${S}/lib${libname}-client.pc.in" > "${D}${libdir}/pkgconfig/lib${libname}-client.pc"

        sed \
	   -e "s|@PV@|${PV}|g" \
           -e "s|@INCLUDE_DIR@|${includedir}|g" \
              "${S}/lib${libname}-client-test.pc.in" > "${D}${libdir}/pkgconfig/lib${libname}-client-test.pc"
    fi
}

EXPORT_FUNCTIONS = "install_dbus_client_lib"

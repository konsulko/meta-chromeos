do_install:append() {
    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${B}/*.pc ${D}${libdir}/pkgconfig/
}

do_install:append() {
    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${S}/re2.pc ${D}${libdir}/pkgconfig/
}

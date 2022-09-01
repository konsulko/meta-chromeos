do_install:append() {
        install -d ${D}$libdir/pkgconfig
        install -m 0644 ${B}/libcurl.pc "${D}$libdir/pkgconfig"
}

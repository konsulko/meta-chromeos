do_install:append() {
    install -d ${D}${includedir}/flatbuffers
    install -m 0644 ${S}/reflection/reflection.fbs ${D}${includedir}/flatbuffers/
}

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-Link-liblvm2cmd-with-libdevmapper-event.patch"

FILES:libdevmapper:append = " \
       ${includedir}/libdevmapper.h \
       ${libdir}/pkgconfig/devmapper.pc \
"

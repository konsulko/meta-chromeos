FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://org.freedesktop.DBus.Properties.xml \
"

do_install:append() {
    install -d ${D}${datadir}/dbus-1/interfaces
    install -m 0644 ${WORKDIR}/org.freedesktop.DBus.Properties.xml \
                    ${D}${datadir}/dbus-1/interfaces/
}

FILES:${PN} += "${datadir}/dbus-1/interfaces"

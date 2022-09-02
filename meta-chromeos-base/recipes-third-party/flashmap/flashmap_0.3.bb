SUMMARY = "Utility for manipulating firmware ROM mapping data structure"
DESCRIPTION = "Utility for manipulating firmware ROM mapping data structure"
HOMEPAGE = "http://flashmap.googlecode.com"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=40c6f4e7206b834b9ab2fda040aef84d"

inherit pkgconfig python3-dir

SRC_URI = "git://chromium.googlesource.com/chromiumos/third_party/flashmap;protocol=https;branch=master"
SRCREV = "9c71c8331ad52a11d29496ffb10cbdb1a51e2ccb"


S = "${WORKDIR}/git"
PR = "r37"

PYTHON_RDEPS = "python3-core python3-logging python3-pprint"
PACKAGECONFIG ??= "python"

PACKAGECONFIG[python] = ",,,${PYTHON_RDEPS},,"

do_install() {
    oe_runmake DESTDIR="${D}" USE_PKG_CONFIG=1 install

    if [ ${@bb.utils.contains('PACKAGECONFIG', 'python', 'true', 'false', d)} ]; then
        install -d ${D}${PYTHON_SITEPACKAGES_DIR}
        install -m 0755 ${S}/fmap.py ${D}${PYTHON_SITEPACKAGES_DIR}/
    fi
}

FILES:${PN} += "${libdir}"

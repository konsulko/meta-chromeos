FILESEXTRAPATHS:prepend := "${CHROMEOS_BASE}/meta-chromeos-base/recipes-chromeos/files:"

SRC_URI += "file://common-mk-external_dependencies-BUILD.gn"

do_configure:prepend() {
    cp ${WORKDIR}/common-mk-external_dependencies-BUILD.gn \
       ${WORKDIR}/src/platform2/common-mk/external_dependencies/BUILD.gn
}

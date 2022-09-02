SUMMARY = "Minimal chromeos-base components"
DESCRIPTION = "The minimal set of packages required to emulate chromeos-base base-image"
PR = "r1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    chromeos-init \
    libchromeos-ui \
"

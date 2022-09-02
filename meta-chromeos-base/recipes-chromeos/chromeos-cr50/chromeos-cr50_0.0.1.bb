SUMMARY = "Ebuild to support the Chrome OS CR50 device."
DESCRIPTION = "Ebuild to support the Chrome OS CR50 device."
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform/ec/+/cr50_stab"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

PROD_IMAGE = "cr50.r0.0.11.w0.5.120"
PRE_PVT_IMAGE="cr50.r0.0.11.w0.6.120_FFFF_00000000_00000010"
SRC_URI = "\
    https://gsdview.appspot.com/chromeos-localmirror/distfiles/${PROD_IMAGE}.tbz2;name=prod \
    https://gsdview.appspot.com/chromeos-localmirror/distfiles/${PRE_PVT_IMAGE}.tbz2;name=pre_pvt \
"
SRC_URI[prod.sha256sum] = "15b317a8ddf2e462f4edb6819e804733844c616d0c7141ae17cf16a74588a717"
SRC_URI[pre_pvt.sha256sum] = "a106fd1c558638f6facb17199aaed1b49e129621ca75afd69b287c76a9846cde"

PR = "r1025"

PACKAGES:remove = "${PN}-dev"

# Cannot have chromeos-cr50-dev as that collides with an automatically generated sub-package of this recipe
RDEPENDS:${PN} += "chromeos-cr50-dev"

PACKAGECONFIG ??= ""

# Description of all the possible PACKAGECONFIG fields (comma delimited):
# 1. Extra arguments that should be added to the configure script argument list (EXTRA_OECONF or PACKAGECONFIG_CONFARGS) if the feature is enabled.
# 2. Extra arguments that should be added to EXTRA_OECONF or PACKAGECONFIG_CONFARGS if the feature is disabled.
# 3. Additional build dependencies (DEPENDS) that should be added if the feature is enabled.
# 4. Additional runtime dependencies (RDEPENDS) that should be added if the feature is enabled.
# 5. Additional runtime recommendations (RRECOMMENDS) that should be added if the feature is enabled.
# 6. Any conflicting (that is, mutually exclusive) PACKAGECONFIG settings for this feature.

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches,
# command-line switches and dependencies.
PACKAGECONFIG[cros_host] = ""

do_install() {
    # Always install both pre-pvt and MP Cr50 images, let the updater at
    # run time decide which one to use, based on the H1 Board ID flags
    # value.

    install -d ${D}/opt/google/cr50/firmware

    bbnote "Will install ${PRE_PVT_IMAGE} and ${PROD_IMAGE}"

    install -m 0644 ${WORKDIR}/${PRE_PVT_IMAGE}/*.bin.prod ${D}/opt/google/cr50/firmware/cr50.bin.prepvt
    install -m 0644 ${WORKDIR}/${PROD_IMAGE}/*.bin.prod ${D}/opt/google/cr50/firmware/cr50.bin.prod
}

FILES:${PN} = "/opt/google/cr50/firmware/*"

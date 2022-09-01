SUMMARY = "Chrome OS camera common libraries."
DESCRIPTION = "Chrome OS camera common libraries."
HOMEPAGE = ""
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"
PR = "r542"

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
PACKAGECONFIG[camera_feature_auto_framing] = ""
PACKAGECONFIG[camera_feature_face_detection] = ""
PACKAGECONFIG[camera_feature_hdrnet] = ""
PACKAGECONFIG[camera_feature_portrait_mode] = ""
PACKAGECONFIG[ipu6] = ""
PACKAGECONFIG[ipu6ep] = ""
PACKAGECONFIG[ipu6se] = ""
PACKAGECONFIG[qualcomm_camx] = ""

GN_ARGS += ' \
    use={ \
        camera_feature_auto_framing=${@bb.utils.contains('PACKAGECONFIG', 'camera_feature_auto_framing', 'true', 'false', d)} \
        camera_feature_face_detection=${@bb.utils.contains('PACKAGECONFIG', 'camera_feature_face_detection', 'true', 'false', d)} \
        camera_feature_hdrnet=${@bb.utils.contains('PACKAGECONFIG', 'camera_feature_hdrnet', 'true', 'false', d)} \
        camera_feature_portrait_mode=${@bb.utils.contains('PACKAGECONFIG', 'camera_feature_portrait_mode', 'true', 'false', d)} \
        ipu6=${@bb.utils.contains('PACKAGECONFIG', 'ipu6', 'true', 'false', d)} \
        ipu6ep=${@bb.utils.contains('PACKAGECONFIG', 'ipu6ep', 'true', 'false', d)} \
        ipu6se=${@bb.utils.contains('PACKAGECONFIG', 'ipu6se', 'true', 'false', d)} \
        qualcomm_camx=${@bb.utils.contains('PACKAGECONFIG', 'qualcomm_camx', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


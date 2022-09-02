SUMMARY = "TFLite models and supporting assets used for testing ML & NNAPI."
DESCRIPTION = "TFLite models and supporting assets used for testing ML & NNAPI."
HOMEPAGE = "https://chromium.googlesource.com/aosp/platform/test/mlts/models/"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r2"

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
PACKAGECONFIG[label_image] = ""
PACKAGECONFIG[benchmark_model] = ""

GN_ARGS += ' \
    use={ \
        label_image=${@bb.utils.contains('PACKAGECONFIG', 'label_image', 'true', 'false', d)} \
        benchmark_model=${@bb.utils.contains('PACKAGECONFIG', 'benchmark_model', 'true', 'false', d)} \
    } \
'

do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


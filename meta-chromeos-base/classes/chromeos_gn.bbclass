# We always need chromiumos-platform2 for common-mk and more
require recipes-chromeos/chromiumos-platform2/chromiumos-platform2.inc
# From meta-chromium -- we might want our own local fork
require recipes-browser/chromium/gn-utils.inc

inherit clang pkgconfig

OUTPUT_DIR ?= "${B}"

DEPENDS += " \
    gn-native \
    ninja-native \
"

# Borrowed from meta-chromium

DEPEND:append:runtime-llvm = " compiler-rt-native libcxx-native"

COMPATIBLE_MACHINE ?= "invalid"
COMPATIBLE_MACHINE:amd64-generic = "amd64-generic"
COMPATIBLE_MACHINE:arm64-generic = "arm64-generic"
COMPATIBLE_MACHINE:arm-generic = "arm-generic"
COMPATIBLE_MACHINE:trogdor = "trogdor"
COMPATIBLE_MACHINE:volteer = "volteer"

# Also build the parts that are run on the host with clang.
BUILD_AR:toolchain-clang = "llvm-ar"
BUILD_CC:toolchain-clang = "clang"
BUILD_CXX:toolchain-clang = "clang++"
BUILD_LD:toolchain-clang = "clang"

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches
# and command-line switches (extra dependencies should not
# be necessary but are OK to add).
PACKAGECONFIG[component-build] = ""

# Base GN arguments, mostly related to features we want to enable or disable.
GN_ARGS = " \
    ${PACKAGECONFIG_CONFARGS} \
"
# This doesn't seem to be common (chromium browser specific)
# is_component_build=${@bb.utils.contains('PACKAGECONFIG', 'component-build', 'true', 'false', d)}

# Make sure pkg-config, when used with the host's toolchain to build the
# binaries we need to run on the host, uses the right pkg-config to avoid
# passing include directories belonging to the target.
GN_ARGS += 'pkg_config="pkg-config"'
GN_ARGS += 'libdir="${STAGING_LIBDIR}"'

# Use libcxx headers for native parts
BUILD_CPPFLAGS:append:runtime-llvm = " -isysroot=${STAGING_DIR_NATIVE} -stdlib=libc++"
# Use libgcc for native parts
BUILD_LDFLAGS:append:runtime-llvm = " -rtlib=libgcc -unwindlib=libgcc -stdlib=libc++ -lc++abi -rpath ${STAGING_LIBDIR_NATIVE}"

# The options are in src/platform2/common-mk/BUILDCONFIG.gn
# Toolchains we will use for the build. We need to point to the toolchain file
# we've created, set the right target architecture and make sure we are not
# using Chromium's toolchain (bundled clang, bundled binutils etc).
GN_ARGS += ' \
    cxx="${CXX}" \
    cc="${CC}" \
    ar="${AR}" \
    sysroot="${STAGING_DIR_TARGET}" \
    platform2_root="${WORKDIR}/src/platform2" \
    target_cpu="${@gn_target_arch_name(d)}" \
'

chromeos_gn_do_configure() {
        cd ${S}
        gn gen --root='${WORKDIR}/src/platform2' --args='${GN_ARGS}' "${OUTPUT_DIR}"
	bbdebug 1 "Valid --args:\n `gn args --root='${WORKDIR}/src/platform2' --list ${OUTPUT_DIR}`"
}
do_configure[cleandirs] += "${OUTPUT_DIR}"

EXPORT_FUNCTIONS do_configure

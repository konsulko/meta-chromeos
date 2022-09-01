FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# ChromeOS files from src/third_party/chromiumos-overlay/dev-cpp/abseil-cpp
# file://abseil-cpp-20211102.0-fix-cuda-nvcc-build.patch
# file://use-std-optional.patch
SRC_URI += "\
    file://abseil-cpp-20211102.0-fix-pkgconfig.patch \
    file://absl.pc.in \
    "

do_compile:append() {
        local libs=$(find ${D}/$libdir -name libabsl_*.so )
        local linklibs="$(echo "${libs}" | sed -E -e 's|[^ ]*/lib([^ ]*)\.so|-l\1|g' | tr '\n' ' ')"
        sed -e "s/@LIBS@/${linklibs}/g" -e "s/@PV@/${PV}/g" \
                "${WORKDIR}/absl.pc.in" > absl.pc || die
}

do_install:append() {
        install -m 0644 absl.pc "${D}$libdir/pkgconfig"
}

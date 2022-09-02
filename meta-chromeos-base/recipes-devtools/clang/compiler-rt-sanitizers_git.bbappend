do_install:append() {
    # Already shipped with compile-rt Orc support
    rm -rf ${D}${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/libclang_rt.orc-*.a

    # Install header files in /usr/include
    for dir in fuzzer profile sanitizer xray; do
        install -d ${D}${includedir}/${dir}
        install -m 0644 ${D}${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/include/${dir}/* \
                        ${D}${includedir}/${dir}/
    done
}

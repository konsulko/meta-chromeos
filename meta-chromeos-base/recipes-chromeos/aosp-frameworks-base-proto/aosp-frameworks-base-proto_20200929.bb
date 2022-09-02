SUMMARY = "AOSP frameworks/base protobuf files"
DESCRIPTION = "AOSP frameworks/base protobuf files"
HOMEPAGE = "https://android.googlesource.com/platform/frameworks/base/+/refs/heads/android11-dev/core/proto/"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r3"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


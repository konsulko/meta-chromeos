SUMMARY = "Chrome OS Update Engine Update Payload Scripts"
DESCRIPTION = "Chrome OS Update Engine Update Payload Scripts"
HOMEPAGE = "https://chromium.googlesource.com/aosp/platform/system/update_engine"
LICENSE = "Apache-2.0"

inherit chromeos_gn
PR = "r458"


do_compile() {
    ninja -C ${B} ${CHROMEOS_PN}
}

do_install() {
    :
}


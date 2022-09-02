SUMMARY = "Autotest tests that require chrome_binary_test, or telemetry deps"
DESCRIPTION = "Autotest tests that require chrome_binary_test, or telemetry deps"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/autotest/"
LICENSE = "GPL-2.0"

inherit chromeos_gn
PR = "r7992"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


SUMMARY = "Vulkan API Capture and Replay Tools"
DESCRIPTION = "Vulkan API Capture and Replay Tools"
HOMEPAGE = "https://github.com/LunarG/gfxreconstruct"
LICENSE = "MIT"

inherit chromeos_gn
PR = "r1"


do_compile() {
    ninja -C ${B} ${BPN}
}

do_install() {
    :
}


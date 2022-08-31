# ChromeOS Reference BSPs README

**NOTE:** This is a work-in-progress and currently at a pre-alpha,
proof-of-concept stage.

This file gives details about using the ChromeOS reference BSPs. The machines
supported can be seen in the conf/machine/ directory and are listed below.
Currently there are generic (amd64-generic, arm-generic and arm64-generic)
machines, based on qemu equivalents in poky, but with naming to align with
ChromeOS. There are also placeholders for two hardware platforms, the
Qualcomm QC7180 based "trogdor" and the Intel Tiger Lake based "volteer".

If you are in doubt about using Poky/OpenEmbedded/Yocto Project with your
hardware, consult the documentation for your board/device.

Support for additional devices is normally added by adding BSP layers to your 
configuration. For more information please see the Yocto Board Support Package 
(BSP) Developer's Guide - documentation source is in documentation/bspguide or 
download the PDF from:

   https://docs.yoctoproject.org/

Note that these reference BSPs use the linux-yocto kernel and in general don't
pull in binary module support for the platforms. This means some device
functionality may be limited compared to a 'full' BSP which may be available.

Future work will add the linux-chromeos kernel with 'full' BSP support.

## Hardware Reference Boards

The following boards are supported by the meta-chromeos-bsp layer:

  * Qualcomm Snapdragon 7c QC7180 (trogdor, e.g. Acer Chromebook 511)
  * Intel Tiger Lake UP3 (volteer, e.g. Acer Chromebook 515)
  * General IA platform (amd64-generic)
  * General ARM platforms (arm-generic and arm64-generic)

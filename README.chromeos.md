# meta-chromeos

**NOTE:** This is a work-in-progress and currently at a pre-alpha,
proof-of-concept stage.

To ensure you have all the dependent layers at the expected revision, use the
manifest in the `chromeos-yocto-manifest` git repository.

This umbrella repository holds the individual layers which make up
`meta-chromeos`. In Yocto Project/OpenEmbedded layers can be of different types
(`distro`, `bsp` and `software/functional`) and should be kept for the
specified purpose in order to pass the `Yocto Project Layer Compatibility` tests.

Layers in this repository:
* `meta-chromeos-distro`:  High-level "global" settings, such as branding
and invasive features (`DISTRO_FEATURES`) such as `systemd` or `wayland`.
* `meta-chromeos-bsp`:  Low-level definition of boards (`MACHINE`s), tunings,
bootloaders and Linux™ kernels.
* `meta-chromeos-base`:  Software/functional layer for the recipes needed to
build `chromeos-base`. This is also where the `chromeos-base-image` is defined.
Most of the work for this proof-of-concept is in this layer.

To set up your build environment:

```shell
$ source <path to>/meta-chromeos/cros-init-build-env [build directory]
```

Many tutorials will tell you to clone other layers as sub-directories of
`poky` and by default put your `build` directory there as well. This does not
work well when working on multiple projects or different build configurations.
It is strongly recommended to keep your other layers as siblings of `poky` and
put your `build-<purpose>` directory on a disk with lots of fast scratch storage.

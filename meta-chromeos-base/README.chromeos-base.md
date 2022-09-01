# meta-chromeos-base

**NOTE:** This is a work-in-progress and currently at a pre-alpha,
proof-of-concept stage.

This layer is for the Yocto Project/OpenEmbedded recipes to build
`chromeos-base`. At the time this was initially developed, the `kirkstone`
(4.0) LTS release of Yocto Project was used as the base.

## Structure of `chromiumos` metadata

A proper checkout of the `chromiumos` sources, following the instructions in
the [developer guide](https://chromium.googlesource.com/chromiumos/docs/+/HEAD/developer_guide.md),
will have a directory structure like:

```
chromiumos/src
├── aosp
├── build
├── chromium
├── config
├── overlays
├── platform
├── platform2
├── project_public
├── repohooks
├── scripts
└── third_party
```

The actual metadata for "recipes" (`*.ebuild`) is mostly located under
[`chromium/src/third_party/chromiumos-overlay/<section>`](https://chromium.googlesource.com/chromiumos/overlays/chromiumos-overlay/+/master/)
(following the [Gentoo naming convention](https://packages.gentoo.org/categories)
for sections, e.g. `sys-apps`). The `chromeos-base` directory is of special
interest as most of the "recipes" of interest are located here. Another
important directory is `chromeos-languagepacks`, which holds the handwriting
recognition packages.

The metadata for "classes" (`*.eclass`) can be found predominantly in
[`chromium/src/third_party/chromiumos-overlay/eclass`](https://chromium.googlesource.com/chromiumos/overlays/chromiumos-overlay/+/main/eclass/),
but also elsewhere, e.g.
[`chromium/src/third_party/eclass-overlay`](https://chromium.googlesource.com/chromiumos/overlays/eclass-overlay/+/master/eclass/)
and [`chromium/src/third_party/portage-stable/eclass`](https://chromium.googlesource.com/chromiumos/overlays/portage-stable/+/refs/heads/main/eclass/).

The "BSP" (Board Support Package) metadata is mostly found in
[`chromium/src/overlays`](https://chromium.googlesource.com/chromiumos/overlays/board-overlays/+/refs/heads/main).
For a given board such as `trogdor` there will be an `overlay-<board>` and
`baseboard-<board>`, e.g. `overlay-trogdor` and `baseboard-trogdor`. There
will also be a `chipset-<cpu>`, such as `chipset-tgl` for Intel's Tiger Lake
(e.g. `volteer` platform) or `chipset-qc7180` for Qualcomm's QC7180 family
(e.g. `trogdor` platform).

Sources for most of the `chromeos` packages are in subdirectories in a git
mono-repo under
[`chromiumos/src/platform2`](https://chromium.googlesource.com/chromiumos/platform2/+/refs/heads/main),
e.g. `libbrillo`.

Sources for legacy `chromeos` packages will be individual git repositories
under [`chromiumos/src/platform`](https://chromium.googlesource.com/chromiumos/platform/),
e.g. [`libchrome`](https://chromium.googlesource.com/chromiumos/platform/libchrome/+/refs/heads/main).

Sources which originate from the [Android Open Source Project](https://source.android.com/)
will be under `aosp`, e.g.
[`external/minijail`](https://android.googlesource.com/platform/external/minijail)
or [`frameworks/ml`](https://android.googlesource.com/platform/frameworks/ml/+/refs/heads/master).

Sources external to Gentoo are typically found under `chromiumos/src/third_party`,
e.g. [`modp_b64`](https://chromium.googlesource.com/aosp/platform/external/modp_b64/+/refs/heads/main)
or [`rootdev`](https://chromium.googlesource.com/chromiumos/third_party/rootdev/+/refs/heads/main).

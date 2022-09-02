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

## Overall design and implementation

An attempt was made to keep names and source paths similar to what is found
in Chrome OS.

In keeping with the
[Yocto Project Compatibile guidelines](https://docs.yoctoproject.org/dev-manual/common-tasks.html#making-sure-your-layer-is-compatible-with-yocto-project),
higher level "distribution" settings (such as the branding with the `psplash`
startup screen) are in `meta-chromeos-distro` and board/chip specific settings
(such as `MACHINE` definitions and kernel recipes) are in `meta-chromeos-bsp`.

### Scripts

#### Recipe generator

A very naïve recipe generator script is available in
`scripts/gen-chromeos-base-recipes.sh`. It takes the path to
`chromiumos/src/third-party/chromeos-overlay/chromeos-base` as an argument.
It will delete and overwrite the `recipes-generated` directory structure with
simple skeleton recipes. It is not very intelligent, so recipes will need
changes. Generated recipes are created with a no-op `do_install()` task, so
this must be manually set by looking at `src_install` in the corresponding
[`*.ebuild`](https://chromium.googlesource.com/chromiumos/overlays/chromiumos-overlay/+/master/chromeos-base).The script currently does not know how to handle more than one `LICENSE`.
Generated recipes with two or more licenses will throw a warning during
parsing. This is because the syntax of the
[`LICENSE` variable](https://docs.yoctoproject.org/ref-manual/variables.html#term-LICENSE)
in Yocto Project uses [SPDX licenses](https://spdx.org/licenses/) and uses
`& | ( )` to indicate "AND", "OR" and compound logic).

#### `chromiumos-platform-*.inc` generator

Since the pattern of `SRC_URI`s for the `chromiumos/src/platform` repositories
is consistent and the sheer number of repositories warranted semi-automation,
a script `recipes-chromeos/chromiumos-platform/gen-platform-inc.sh` is available
to generate the `chromiumos-platform-<name>.inc` files to be used in `platform`
recipes. It should be run in the `recipes-chromeos/chromiumos-platform`
directory and takes the path to `chromiumos/.repo/manifests/full.xml` as an
argument.

**NOTE:** `chromiumos-platform-cr50.inc` is a special case that had to be
manually created, since it is in the `ec` platform repository, but the branch
name is `cr50_stab`. There are probably other exceptions not handled by the
script.

### Recipes

Most of the recipes of interest were generated as skeletons with the recipe
generator script and then moved to the `recipes-chromeos` directory as they
became more fully functional. For other non-`chromeos-base` recipes, typical
Yocto Project directories, such as `recipes-core`, `recipes-connectivity`,
`recipes-devtools`, etc. were used in alignment with upstream layers. This is
especially true of `*.bbappend`s.

**NOTE:** Many recipes are not complete, but were commited to the git
repository in order to have a fuller picture of the scope of the project and
allow for incremental development.

### Image targets

As with [`poky`](https://docs.yoctoproject.org/ref-manual/terms.html#term-Poky),
you can build
[`core-image-base`](https://docs.yoctoproject.org/ref-manual/images.html#images)
and boot it with
[`runqemu`](https://docs.yoctoproject.org/dev-manual/qemu.html?highlight=runqemu#running-qemu)
(you may need to enter your `sudo` password to create `tap` devices).

In `meta-chromeos-distro/conf/conf-notes.txt`, you can list other common
targets (currently `chromeos-image-base`) as a hint to the user. This recipe
is in `meta-chromeos-base/recipes-core/images`. This recipe includes
`packagegroup-chromeos-base`, which is in
`meta-chromeos-base/recipes-core/packagegroups` (currently only contains
`chromeos-init`).

### Classes

In the initial proof-of-concept stage, it did not make sense to immediately
replicate all of the `*.eclass`es in Gentoo/ChromeOS. Rather, recipes were
created from a Yocto Project developer perspective and classes were
identified/developed organically along the way.

Several key classes were developed to make recipes more concise and allow
for code reuse:
* `chromeos_gn`: Helper class for recipes which use the `gn` (generate ninja)
tool.
* `chromiumos_overlay`: Helper class for recipes which only install files from
`chromiumos-overlay/*/*.ebuild/files`.
* `dlc`: work-in-progress to mimic the behavior of the `dlc.eclass` in Chrome
OS for working with dowloadable components.
* `platform`: Currently only implements the `platform_dbus_client_libs`
function

#### `chromeos_gn` class

The `chromeos_gn` class sets up a build environment ready to compile `gn`
recipes which use `common-mk`.

By default, the `OUTPUT_DIR` is defined to be the same as `${B}`, but this
can be overriden if desired.

The actual call to `gn` passes in `--root='${WORKDIR}/src/platform2'` and
`--args='${GN_ARGS}'`. In your recipes you should add `GN_ARGS +=` for
recipe-specific arguments such as `platform_subdir` and the `use` flags list.

If you call `bitbake -D <recipe>` (first level of debugging), a list of valid
`--args` will be output in `log.do_configure`.

The only task defined in the class is `chromeos_gn_do_configure`, as
`do_compile` and `do_install` likely need recipe-specific tweaks.

### Includes (`*.inc`)

Patterns emerged where sources needed to be available in multiple recipes,
to make this simpler (and keep the `SRC_URI` and `SRCREV` set in only one
place), the following include files are available.

* `chromiumos-platform2/chromiumos-platform2.inc`
* `chromiumos-platform/chromiumos-platform-*.inc`
* `chromiumos-overlay/chromiumos-overlay.inc`
* `chromium/chromium-src-components-policy.inc`
* `chromium/chromium-src-third-party-shell-encryption.inc`
* `chromium/chromium-src-third-party-private-membership.inc`

## Portage from a Yocto Project developer perspective

Portage/ebuild was the original inspiration for
[BitBake](https://docs.yoctoproject.org/bitbake/2.0/bitbake-user-manual/bitbake-user-manual-intro.html#history-and-goals),
but the two have significant differences. The usage of
[`use` flags](https://wiki.gentoo.org/wiki/Handbook:AMD64/Working/USE) sets
the configuration for a specific build. The closest allegory in BitBake recipes
is [`PACKAGECONFIG`](https://docs.yoctoproject.org/ref-manual/variables.html#term-PACKAGECONFIG)
(with some higher level values set in
[`IMAGE_FEATURES`](https://docs.yoctoproject.org/ref-manual/variables.html#term-IMAGE_FEATURES),
[`MACHINE_FEATURES`](https://docs.yoctoproject.org/ref-manual/variables.html#term-MACHINE_FEATURES)
or [`DISTRO_FEATURES`](https://docs.yoctoproject.org/ref-manual/variables.html#term-DISTRO_FEATURES)).
Often, the `use` flags are set in the `IUSE` variable. Take special note of
`use` flags prefixed with a minus (`-`) which means "disable" or plus (`+`)
which means "enable" in the local context. Also take note of `REQUIRED_USE`,
which are absolutely required `use` flags which must be enabled.

Similar to `bbclass`, an `eclass` is `inherit`ed, but the syntax is quite
different (`bash`isms, variable names do not have a space around `=` or other
operator).

There are a lot of helper scripts/functions used in ebuild, such as `insinto`,
`doins`, `dolib.so` and `dobin`. In BitBake recipes, we explicitly call out the
directory to be installed into, e.g. `install -d ${D}${bindir}` and the file to
be installed, e.g. `install -m 0755 ${B}/foo ${D}${bindir}/`.

ChromeOS does not use versioned `.so` files for the most part (especially not
in `chrome-base` components). The `PV` (package version) is also often set to
`0.0.1` with the `PR` (package revision) carrying the bump for e.g. a new
commit of `platform2`.

ChromeOS is mostly multilib (e.g. `/usr/lib64`), which is not as common in
[Yocto Project/OpenEmbedded](https://docs.yoctoproject.org/dev-manual/common-tasks.html?highlight=multilib#combining-multiple-versions-of-library-files-into-one-image).

## BitBake from a Gentoo developer perspective

Bitbake recipes (`*.bb`) have a very specific naming scheme:
`<recipe-name>_<version>.bb` where `recipe-name` can included alphanumeric
characters and dash (`-`). The underscore (`_`) character is the delimiter
between the [`PN`](https://docs.yoctoproject.org/ref-manual/variables.html#term-PN)
(package name) and the
[`PV`](https://docs.yoctoproject.org/ref-manual/variables.html#term-PV)
(package version). For this reason, recipe/package names SHALL NOT contain an
underscore. All usage of underscores in the `chromeos-base` meta-data was
replaced by dashes.

BitBake shell tasks are not `bash` but rather a more limited POSIX `sh`.

### Tasks

BitBake also allows for
[tasks](https://docs.yoctoproject.org/ref-manual/tasks.html#tasks) to be
written in
[Python](https://docs.yoctoproject.org/bitbake/2.0/bitbake-user-manual/bitbake-user-manual-metadata.html#bitbake-style-python-functions).
Some base tasks, such as `do_patch` are written in Python, so appending
(`do_patch:append()`) and prepending (`do_patch:prepend()`) must also be
written in Python. This is indicated by prefixing the task name with `python`,
e.g. `python do_unpack()`.

BitBake allows
[Python functions](https://docs.yoctoproject.org/bitbake/2.0/bitbake-user-manual/bitbake-user-manual-metadata.html#python-functions)
to be defined in classes and recipes, e.g. `def my_awesome_func():`.

BitBake also allows for
[anonymous Python functions](https://docs.yoctoproject.org/bitbake/2.0/bitbake-user-manual/bitbake-user-manual-metadata.html#anonymous-python-functions)
which are executed immediately (and should be used sparingly as they affect
parsing time).

Rather than the base tasks being named `src_compile` and `src_install`, as
they are in `*.ebuild`, they are named `do_compile` and `do_install`. BitBake
classes can export tasks, such as `do_install`, normally preceded by the name
of the class ("namespacing" or
"[flexible inheritance](https://docs.yoctoproject.org/bitbake/bitbake-user-manual/bitbake-user-manual-metadata.html#flexible-inheritance-for-class-functions)"),
e.g. `go_do_install`. Similar to usage in Portage/ebuild, this namespacing
allows for multiple classes to be used. Also similar to Portage, functions can
be exported with `EXPORT_FUNCTIONS`.

Task order can be set with the
[`addtask` syntax](https://docs.yoctoproject.org/bitbake/2.0/bitbake-user-manual/bitbake-user-manual-metadata.html#promoting-a-function-to-a-task),
e.g:
```python
python do_printdate () {
    import time
    print(time.strftime('%Y%m%d', time.gmtime()))
}
addtask printdate after do_fetch before do_build
```

### Variable scope

Variables set within a task do not carry into another task (as if they were
flagged `local`). Environment variables can be explicitly `export`ed outside
of a task and used throughout. Variables in the datastore can be referenced
throughout their context.

### The datastore and common libraries

In Python code in recipes/classes, the
[`datastore`](https://docs.yoctoproject.org/bitbake/2.0/bitbake-user-manual/bitbake-user-manual-metadata.html#functions-for-accessing-datastore-variables)
is simply the variable
[`d`](https://docs.yoctoproject.org/dev-manual/common-tasks.html#class-attributes)
and is available everywhere. Also available are the basic libraries
[`lib/bb`](https://git.yoctoproject.org/poky/tree/bitbake/lib/bb) from BitBake
and [`lib/oe`](https://git.yoctoproject.org/poky/tree/meta/lib/oe) from
openembedded-core aka "meta". These are especially powerful with the
[inline python variable expansion syntax](https://docs.yoctoproject.org/bitbake/2.0/bitbake-user-manual/bitbake-user-manual-metadata.html#inline-python-variable-expansion)

### Everything is cross-compiled

Everything in Yocto Project/Openembedded is "cross-compiled", even tools for an
x86_64 host (`-native` recipes) are built in an environment with
`x86_64-<distro>-*` triplet tools (except those explicitly in the `HOSTTOOLS`
variable). A _very_ small number of host tools are used directly, most are only
used to bootstrap others (e.g. the host `gcc` is used to bootstrap the `gcc`
used to compile everything). "Host contamination" is a frequently occuring
problem and can lead to broken and/or unpredictable behavior.

### PACKAGECONFIG
For a given recipe, the `PACKAGECONFIG` variable is the equivalent of `IUSE`
(close enough). For a given flag, the `DEPENDS` and `RDEPENDS` can be set in the
definition of the `PACKAGECONFIG[<flag>]`. The six possible comma-delimited
fields are:

1. Extra arguments that should be added to the configure script argument list
(`EXTRA_OECONF` or `PACKAGECONFIG_CONFARGS`) if the feature is enabled.
2. Extra arguments that should be added to `EXTRA_OECONF` or
`PACKAGECONFIG_CONFARGS` if the feature is disabled.
3. Additional build dependencies (`DEPENDS`) that should be added if the feature
is enabled.
4. Additional runtime dependencies (`RDEPENDS`) that should be added if the
feature is enabled.
5. Additional runtime recommendations (`RRECOMMENDS`) that should be added if
the feature is enabled.
6. Any conflicting (that is, mutually exclusive) `PACKAGECONFIG` settings for
this feature.

Board/machine specific features are set in `MACHINE_FEATURES`.
System wide features are set in `DISTRO_FEATURES`.
Image wide features are enabled in `IMAGE_FEATURES`.
System wide classes can be inherited with `INHERIT +=` in a `.conf` file.

`PACKAGECONFIG` can be set conditionally with the inline python variable
expansion syntax such as:

```bash
PACKAGECONFIG ??= "${@bb.utils.filter('MACHINE_FEATURES', 'tpm tpm2', d)}"
```
or
```bash
PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}"
```

### Testing

Most package or unit tests are run on-target (often qemu) with
[`ptest`](https://docs.yoctoproject.org/dev-manual/common-tasks.html#testing-packages-with-ptest).
There are also host tests, commonly known as
[`oe-selftest`](https://docs.yoctoproject.org/test-manual/intro.html#oe-selftest),
which more commonly test the infrastructure (though
[runtime tests](https://git.yoctoproject.org/poky/tree/meta/lib/oeqa/runtime/cases)
are also possible).

## Lessons learned and frequent patterns

The initial proof-of-concept stage of this project was largely a game of
whack-a-mole. While the developer had been a user of Gentoo and Chrome OS,
he had not been a developer and there were new concepts to be learned. Also,
simply looking at an `*.ebuild` did not necessarily translate to knowing, e.g.
build-time dependencies. Also, the Yocto Project upgrades recipes/components
rather agressively and this can be a problem for legacy code (e.g. `gcc-11.3`,
`clang-14.0` and `openssl-3.0`)

### Missing pkgconfig and static libraries

The `gn` (generate ninja) tool expects to find `*.pc` files for many libraries.
Many upstream Yocto Project/OpenEmbedded recipes do not package/ship the `*.pc`
pkgconfig files nor the static libraries (`*.a`). These can be added by
creating a `*.bbappend` for the corresponding recipe from another layer. If you
want to upstream these conveniences, it is normally recommended to add them to
the `FILES:${PN}-staticdev` variable in the recipe.

### Unsatisfied DEPENDS

If you see errors like:
```
| subprocess.CalledProcessError: Command '['pkg-config', 'absl', 'libbrillo', 'libchrome', 'libtpm_manager-client', 'openssl', 'system_api', 'protobuf-lite', '--cflags']' returned non-zero exit status 1.
```
this means you have not satisfied the `DEPENDS` for the recipe. Note that you
will need to translate from pkgconfig name to recipe name, e.g. `absl.pc` is
from `abseil-cpp` and `protofbuf-lite` is from `protobuf`.

One tool which can help with this translation is
[`oe-pkgdata-util`](https://docs.yoctoproject.org/dev-manual/common-tasks.html#viewing-package-information-with-oe-pkgdata-util). For example:
```
$ oe-pkgdata-util find-path /usr/lib/pkgconfig/absl.pc
abseil-cpp-dev: /usr/lib/pkgconfig/absl.pc

$ oe-pkgdata-util lookup-recipe abseil-cpp-dev
abseil-cpp
```

### Versioned shared library objects

Yocto Project/OpenEmbedded always ships versioned `*.so.n` files, with symlinks
for the unversioned `*.so`. Header files and the `*.so` symlink are
"automatically" put into a `${PN}-dev` sub-package. Static libraries and
pkgconfig files are "automatically" put into a `${PN}-staticdev` sub-package.
When you `DEPENDS` on a recipe, these sub-packages are all available in the
`recipe-sysroot`, but they are not included in the shipping binary image.

### Implicit use flags

The overall usage of `common-mk` (`src/platform2/common-mk`) means that often
`use` flags not specifically called out in the `IUSE` variable in an `*.ebuild`
will still need to be set and passed in as arguments, e.g. `cros_host`,
`fuzzer`, `profiling`, `test` and `tcmalloc`.

### Patching: quilt vs. git

It turned out to be simpler for `platform2` recipes to locally (in
`chromiumos/src/platform2`) use `quilt` to create patches rather than use `git`
and wrangling the path to `${S}`. For recipes from `platform` or stand-alone
repos from `third_party` either `quilt` or `git` could be used. In particular,
if a package already used `quilt` for patches in the `*.ebuild` it was much
easier to stick with `quilt` throughout.

### protoc and gen-protobuf-go

Many packages will use `protobuf` and often need to generate files with tools
like `protoc` (`PROVIDED` by the `protobuf-native` recipe) and `gen-protobuf-go`
(`PROVIDED` by the `github.com-golang-protobuf-native` recipe).

### go-generate-chromeos-dbus-bindings

Some packages generate dbus bindings with tools like
`go-generate-chromeos-dbus-bindings` which is provided by the
`chromeos-dbus-bindings-native` recipe.

### gcc-11, clang-14 and static_cast<>

A lot of legacy code needed to have e.g. `static_cast<double>()` added to avoid
gcc/clang errors (`-Werror` is on by default).

### OpenSSL 3.0.y vs OpenSSL 1.1.1

It appears that Gentoo/ChromeOS are still using `OpenSSL 1.1.1`, so several code
bases needed to be patched to not use deprecated functions/calls so as to become
`OpenSSL 3.0.y` ready. This turned out to be too invasive for the timeframe of
the proof-of-concept and instead the
[OpenSSL 1.1.1q recipe](https://layers.openembedded.org/layerindex/recipe/122225/)
from `dunfell` was forked. To use this version, the following is set in
`distro/chromeos.conf`:

```bash
# Override the default 3.0.y version in kirkstone
# with the recipe imported/forked from dunfell
PREFERRED_VERSION_openssl = '1.1.1%'
```

Since a number of packages were already ported to `OpenSSL 3.0.y` the (unused)
patches were kept in the tree for reference and future work.

### Ancient ModemManager files in common-mk

Some ancient `ModemManager` files which were deleted a decade ago upstream were
still being referenced in `common-mk/external_dependencies/BUILD.gn`.

### upstart-1.2 and gettext-0.21/automake-1.6

The [ChromeOS fork of upstart-1.2](https://chromium.googlesource.com/chromiumos/third_party/upstart/+/refs/heads/chromeos-1.2)
had to be patched to work with `gettext-0.21` and `automake-1.6` from
`kirkstone`. This was done by:

```bash
$ bitbake -c devshell upstart
$ gettextize
```
and then adding/merging the resulting files.

### Users and Groups

There are two ways of creating users and groups in Yocto Project/Openembedded.

#### `extrausers` class

For system-wide (really image-wide) users, the `extrausers` class can be used,
such as in `meta-chromeos-base/conf/layer.conf`:

```bash
IMAGE_CLASSES += "extrausers"

# system wide users
# chronos:x:1000:1000:system_user:/home/chronos/user:/bin/bash
# chronos-access:!:1001:1001:non-chronos user with access to chronos data:/dev/null:/bin/false
EXTRA_USERS_PARAMS = "\
    groupadd --gid 1000 --system chronos ; \
    groupadd --gid 1001 --system chronos-access ; \
    useradd --uid 1000 --gid 1000 --comment 'system_user' --home-dir /home/chronos/user --system --shell /bin/bash chronos ; \
    useradd --uid 1001 --gid 1001 --comment 'non-chronos user with access to chronos data' --home-dir /dev/null --system --shell /bin/false chronos-access \
"
```
Take special note of the use of semicolon (`;`) to separate individual
`groupadd` and `useradd` clauses.

#### `useradd` class

For per-recipe users and groups, the `useradd` class can be used, for example:

```bash
inherit useradd
...

USERADD_PACKAGES = "${PN}"

# metrics:!:20140:20140:user for metrics_daemon to run its services in sandboxed environment:/dev/null:/bin/false
GROUPADD_PARAM:${PN} = "--system --gid 20140 metrics"
USERADD_PARAM:${PN} = "--uid 20140 --gid metrics --comment 'user for metrics_daemon to run its services in sandboxed environment' --home-dir /dev/null --system --shell /bin/false metrics"
require recipes-chromeos/files/include/chronos-user.inc
require recipes-chromeos/files/include/chronos-access-user.inc
```

**NOTE:** Using the `-U` or `--user-group` argument to `useradd` does not
guarantee the same numeric `--gid`, so `GROUPADD_PARAM` should be used to
explicitly create the group.

**NOTE:** Common users and groups can be defined in `*.inc` file(s) such as the
`chronos` and `chronos-access` user/groups above. These can then be `require`d
into any recipe that needs to set file permissions, etc. for those
users/groups.

The actual numeric ids, etc. were found by `grep`ing the
`chromiumos/chroot/build/<machine>/etc/passwd` and
`chromiumos/chroot/build/<machine>/etc/group` files from a `base-image` build.

### `tpm2-simulator` no return statement in `constexpr`

With `gcc-11.3` and `clang-14`, `tpm2-simulator` is unbuildable (remember that
`-Werror` is on by default):

```console
| ../src/platform2/tpm2-simulator/tpm_executor_version.cc:22:30: error: no return statement in constexpr function
| constexpr TpmExecutorVersion GetDefaultTpmExecutorVersion() {
|                              ^
| 1 error generated.
```

Added a patch to fallback to returning tpm2 to avoid the error.

## Future Work (and things that are currently broken)

### More completed recipes

In an analysis of a `base-image` build, 161 `chromeos-base` packages were found
to be included in the build. Not all of these recipes are finished and some have
errors. It is also likely that there will be unmet dependencies that need new
recipes.

As these recipes are completed, they should be added to
`packagegroup-chromeos-base`.

### Systemd units

The [`systemd` class](https://docs.yoctoproject.org/ref-manual/classes.html?highlight=systemd#systemd-bbclass)
should be used for enabling services and units in recipes. Because `systemd` is
gated by a `use` flag in the `*.ebuild` this is slightly more complicated. More
work is needed to figure out the most expedient solution to mimic the `use` flag
behavior.

### `attestation` uses deprecated protobuf

With `protobuf-`, `attestation` is unabuildable (remember that `-Werror` is on
by default), with many warnings similar to:

```console
| gen/attestation/common/print_attestation_ca_proto.cc:806:13: error: 'has_device_trust_signals' is deprecated [-Werror,-Wdeprecated-declarations]
|   if (value.has_device_trust_signals()) {
|             ^
| <build>/tmp/work/cortexa57-chromeos-linux/attestation/0.0.1-r3611/recipe-sysroot/usr/include/attestation/proto_bindings/attestation_ca.pb.h:3175:3: note: 'has_device_trust_signals' has been explicitly marked deprecated here
|   PROTOBUF_DEPRECATED bool has_device_trust_signals() const;
|   ^
```

### `chromeos-cr50-dev` optimization and `_FORTIFY_SOURCE`

With `gcc-11.3` and `clang-14`, `chromeos-cr50-dev` is unabuildable (remember
that `-Werror` is on by default):

```console
In file included from usb_updater2.c:8:
In file included from <build>/tmp/work/cortexa57-chromeos-linux/chromeos-cr50-dev/0.0.1-r431/recipe-sysroot/usr/include/endian.h:21:
<build>/tmp/work/cortexa57-chromeos-linux/chromeos-cr50-dev/0.0.1-r431/recipe-sysroot/usr/include/features.h:412:4: error: _FORTIFY_SOURCE requires compiling with optimization (-O) [-Werror,-W#warnings]
#  warning _FORTIFY_SOURCE requires compiling with optimization (-O)
```
This is happening even though `-O2` and `-D_FORTIFY_SOURCE=2` are being passed
in.

### `MACHINE`s and kernels

During the proof-of-concept stage, builds were mostly performed for the
`arm64-generic` machine and the `linux-yocto_5.15` kernel. Once more of the
`chrome-base` components are buildable, this should be the next focus.

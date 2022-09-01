
DESCRIPTION = "Binary crate to generate Rust code from XML introspection data"
HOMEPAGE="https://crates.io/crates/dbus-codegen"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = " \
    file://dbus-codegen/LICENSE-APACHE;md5=ec4df47d4e83d756a74349582f3e99fb \
    file://dbus-codegen/LICENSE-MIT;md5=86713ff7b5518754f2d1d5dd8dbfa694 \
"

SRC_URI = "git://github.com/diwic/dbus-rs;protocol=https;branch=master"
SRCREV = "7672550a12d133fe15987e1187d1e72edef575c4"

CARGO_SRC_DIR = "dbus-codegen"

SRC_URI += " \
    crate://crates.io/ansi_term/0.12.1 \
    crate://crates.io/atty/0.2.14 \
    crate://crates.io/autocfg/1.1.0 \
    crate://crates.io/bitflags/1.3.2 \
    crate://crates.io/cfg-if/1.0.0 \
    crate://crates.io/clap/2.34.0 \
    crate://crates.io/fastrand/1.8.0 \
    crate://crates.io/futures-channel/0.3.21 \
    crate://crates.io/futures-core/0.3.21 \
    crate://crates.io/futures-executor/0.3.21 \
    crate://crates.io/futures-io/0.3.21 \
    crate://crates.io/futures-macro/0.3.21 \
    crate://crates.io/futures-sink/0.3.21 \
    crate://crates.io/futures-task/0.3.21 \
    crate://crates.io/futures-util/0.3.21 \
    crate://crates.io/futures/0.3.21 \
    crate://crates.io/hermit-abi/0.1.19 \
    crate://crates.io/instant/0.1.12 \
    crate://crates.io/libc/0.2.129 \
    crate://crates.io/log/0.4.17 \
    crate://crates.io/memchr/2.5.0 \
    crate://crates.io/mio/0.8.4 \
    crate://crates.io/num_cpus/1.13.1 \
    crate://crates.io/once_cell/1.13.0 \
    crate://crates.io/pin-project-lite/0.2.9 \
    crate://crates.io/pin-utils/0.1.0 \
    crate://crates.io/pkg-config/0.3.25 \
    crate://crates.io/proc-macro2/1.0.43 \
    crate://crates.io/quote/1.0.21 \
    crate://crates.io/redox_syscall/0.2.16 \
    crate://crates.io/remove_dir_all/0.5.3 \
    crate://crates.io/slab/0.4.7 \
    crate://crates.io/socket2/0.4.4 \
    crate://crates.io/strsim/0.8.0 \
    crate://crates.io/syn/1.0.99 \
    crate://crates.io/tempfile/3.3.0 \
    crate://crates.io/textwrap/0.11.0 \
    crate://crates.io/tokio-macros/1.8.0 \
    crate://crates.io/tokio/1.20.1 \
    crate://crates.io/unicode-ident/1.0.3 \
    crate://crates.io/unicode-width/0.1.9 \
    crate://crates.io/vec_map/0.8.2 \
    crate://crates.io/wasi/0.11.0+wasi-snapshot-preview1 \
    crate://crates.io/winapi-i686-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi-x86_64-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi/0.3.9 \
    crate://crates.io/windows-sys/0.36.1 \
    crate://crates.io/windows_aarch64_msvc/0.36.1 \
    crate://crates.io/windows_i686_gnu/0.36.1 \
    crate://crates.io/windows_i686_msvc/0.36.1 \
    crate://crates.io/windows_x86_64_gnu/0.36.1 \
    crate://crates.io/windows_x86_64_msvc/0.36.1 \
    crate://crates.io/xml-rs/0.8.4 \
"

S = "${WORKDIR}/git"

# workaround for crate fetcher
do_compile[network] = "1"

DEPENDS:append = " dbus"

inherit cargo pkgconfig

BBCLASSEXTEND = "native"

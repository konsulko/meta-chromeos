SUMMARY="Protobuf code generator and a protoc-gen-rust protoc plugin"
DESCRIPTION = "Code generator for rust-protobuf. \
 \
Includes a library and `protoc-gen-rust` binary. \
 \
See `protoc-rust` and `protobuf-codegen-pure` crates."
HOMEPAGE="https://github.com/stepancheg/rust-protobuf/protobuf-codegen"
LICENSE="MIT"
LIC_FILES_CHKSUM = "file://protobuf-codegen/LICENSE.txt;md5=b1a3f076cdcd334a681558ac44e1a648"

SRC_URI="git://github.com/stepancheg/rust-protobuf;protocol=https;branch=v2.24"
SRCREV = "6b527b247e0c521b3998ff6fc0a17a543e653476"

inherit cargo

S = "${WORKDIR}/git"

CARGO_SRC_DIR = "protobuf-codegen"

# workaround for crate fetcher
do_compile[network] = "1"

SRC_URI += " \
    crate://crates.io/aho-corasick/0.7.6 \
    crate://crates.io/atty/0.2.13 \
    crate://crates.io/bytes/1.0.0 \
    crate://crates.io/c2-chacha/0.2.3 \
    crate://crates.io/cfg-if/0.1.10 \
    crate://crates.io/env_logger/0.5.13 \
    crate://crates.io/getrandom/0.1.13 \
    crate://crates.io/glob/0.2.11 \
    crate://crates.io/humantime/1.3.0 \
    crate://crates.io/itoa/0.4.4 \
    crate://crates.io/lazy_static/1.1.0 \
    crate://crates.io/libc/0.2.66 \
    crate://crates.io/log/0.4.8 \
    crate://crates.io/memchr/2.2.1 \
    crate://crates.io/ppv-lite86/0.2.6 \
    crate://crates.io/proc-macro2/1.0.6 \
    crate://crates.io/quick-error/1.2.2 \
    crate://crates.io/quote/1.0.2 \
    crate://crates.io/rand/0.7.2 \
    crate://crates.io/rand_chacha/0.2.1 \
    crate://crates.io/rand_core/0.5.1 \
    crate://crates.io/rand_hc/0.2.0 \
    crate://crates.io/redox_syscall/0.1.56 \
    crate://crates.io/regex-syntax/0.6.18 \
    crate://crates.io/regex/1.3.9 \
    crate://crates.io/remove_dir_all/0.5.2 \
    crate://crates.io/ryu/1.0.2 \
    crate://crates.io/serde/1.0.104 \
    crate://crates.io/serde_derive/1.0.104 \
    crate://crates.io/serde_json/1.0.44 \
    crate://crates.io/syn/1.0.11 \
    crate://crates.io/tempfile/3.1.0 \
    crate://crates.io/termcolor/1.0.5 \
    crate://crates.io/thiserror-impl/1.0.20 \
    crate://crates.io/thiserror/1.0.20 \
    crate://crates.io/thread_local/1.0.1 \
    crate://crates.io/unicode-xid/0.2.0 \
    crate://crates.io/version_check/0.1.5 \
    crate://crates.io/wasi/0.7.0 \
    crate://crates.io/which/4.0.1 \
    crate://crates.io/winapi-i686-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi-util/0.1.2 \
    crate://crates.io/winapi-x86_64-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi/0.3.8 \
    crate://crates.io/wincolor/1.0.2 \
"

do_install:append() {
    install -d ${D}${datadir}/proto
}

RPROVIDES:${PN} += "protoc-gen-rust"

FILES:${PN} += "${datadir}/proto"

BBCLASSEXTEND = "native"

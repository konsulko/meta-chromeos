KBRANCH:amd64-generic  = "v5.15/standard/base"
KBRANCH:arm-generic  = "v5.15/standard/arm-versatile-926ejs"
KBRANCH:arm64-generic  = "v5.15/standard/qemuarm64"

KMACHINE:amd64-generic ?= "common-pc-64"
KMACHINE:arm-generic ?= "arm-versatile-926ejs"
KMACHINE:arm64-generic ?= "qemuarm64"

COMPATIBLE_MACHINE:amd64-generic = "amd64-generic"
COMPATIBLE_MACHINE:arm-generic = "arm-generic"
COMPATIBLE_MACHINE:arm64-generic = "arm64-generic"
COMPATIBLE_MACHINE:trogdor = "trogdor"
COMPATIBLE_MACHINE:volteer = "volteer"

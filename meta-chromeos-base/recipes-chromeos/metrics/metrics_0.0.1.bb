SUMMARY = "Metrics aggregation service for Chromium OS"
DESCRIPTION = "Metrics aggregation service for Chromium OS"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/metrics/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn useradd

DEPENDS:append = " protobuf protobuf-native re2 rootdev libbrillo libchrome session-manager-client system-api vboot-reference"

S = "${WORKDIR}/src/platform2/${BPN}"
B = "${WORKDIR}/build"
PR = "r3461"

CXX:append = " -I${STAGING_INCDIR}/session_manager-client"

SRC_URI:append = " \
    file://0001-metrics-metrics-daemon-static-cast.patch \
    file://0002-metrics-vmlog_writer-static-cast.patch \
"

GN_ARGS += 'platform_subdir="${BPN}"'

PACKAGES += "libmetrics"

PACKAGECONFIG ??= ""

# Description of all the possible PACKAGECONFIG fields (comma delimited):
# 1. Extra arguments that should be added to the configure script argument list (EXTRA_OECONF or PACKAGECONFIG_CONFARGS) if the feature is enabled.
# 2. Extra arguments that should be added to EXTRA_OECONF or PACKAGECONFIG_CONFARGS if the feature is disabled.
# 3. Additional build dependencies (DEPENDS) that should be added if the feature is enabled.
# 4. Additional runtime dependencies (RDEPENDS) that should be added if the feature is enabled.
# 5. Additional runtime recommendations (RRECOMMENDS) that should be added if the feature is enabled.
# 6. Any conflicting (that is, mutually exclusive) PACKAGECONFIG settings for this feature.

# Empty PACKAGECONFIG options listed here to avoid warnings.
# The .bb file should use these to conditionally add patches,
# command-line switches and dependencies.
PACKAGECONFIG[cros_host] = ""
PACKAGECONFIG[fuzzer] = ""
PACKAGECONFIG[metrics_uploader] = ""
PACKAGECONFIG[passive_metrics] = ""
PACKAGECONFIG[profiling] = ""
PACKAGECONFIG[systemd] = ""
PACKAGECONFIG[tcmalloc] = ""
PACKAGECONFIG[test] = ""

GN_ARGS += ' \
    use={ \
        cros_host=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        fuzzer=${@bb.utils.contains('PACKAGECONFIG', 'fuzzer', 'true', 'false', d)} \
        metrics_uploader=${@bb.utils.contains('PACKAGECONFIG', 'metrics_uploader', 'true', 'false', d)} \
        passive_metrics=${@bb.utils.contains('PACKAGECONFIG', 'passive_metrics', 'true', 'false', d)} \
        profiling=${@bb.utils.contains('PACKAGECONFIG', 'profiling', 'true', 'false', d)} \
        systemd=${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)} \
        tcmalloc=${@bb.utils.contains('PACKAGECONFIG', 'tcmalloc', 'true', 'false', d)} \
        test=${@bb.utils.contains('PACKAGECONFIG', 'test', 'true', 'false', d)} \
    } \
'

USERADD_PACKAGES = "${PN}"

# metrics:!:20140:20140:user for metrics_daemon to run its services in sandboxed environment:/dev/null:/bin/false
GROUPADD_PARAM:${PN} = "--system --gid 20140 metrics"
USERADD_PARAM:${PN} = "--uid 20140 --gid metrics --comment 'user for metrics_daemon to run its services in sandboxed environment' --home-dir /dev/null --system --shell /bin/false metrics"
require recipes-chromeos/files/include/chronos-user.inc
require recipes-chromeos/files/include/chronos-access-user.inc

do_compile() {
    ninja -C ${B}
}

export SO_VERSION="1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/metrics_client ${D}${bindir}/
    install -m 0755 ${B}/chromeos-pgmem ${D}${bindir}/

    install -d -m 0774 ${D}${sysconfdir}/daemon-store/uma-consent
    chown chronos:chronos-access ${D}${sysconfdir}/daemon-store/uma-consent

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${S}/libmetrics.pc ${D}${libdir}/pkgconfig/
    install -m 0644 ${B}/obj/metrics/structured/libstructuredmetrics.pc ${D}${libdir}/pkgconfig/

    find ${B}${base_libdir} -type f -name lib*.so | while read f; do
        fn=$(basename ${f})
        echo ${fn}
        install -m 0755 ${f} ${D}${libdir}/${fn}.${SO_VERSION}
        ln -sf ${fn}.${SO_VERSION} ${D}${libdir}/${fn}
    done

    install -d ${D}${includedir}/metrics
    install -m 0644 ${S}/c_metrics_library.h ${D}${includedir}/metrics/
    install -m 0644 ${S}/cumulative_metrics.h ${D}${includedir}/metrics/
    install -m 0644 ${S}/metrics_library.h ${D}${includedir}/metrics/
    install -m 0644 ${S}/metrics_library_mock.h ${D}${includedir}/metrics/
    install -m 0644 ${S}/persistent_integer.h ${D}${includedir}/metrics/
    install -m 0644 ${S}/structured/c_structured_metrics.h ${D}${includedir}/metrics/
    install -m 0644 ${S}/timer.h ${D}${includedir}/metrics/
    install -m 0644 ${S}/timer_mock.h ${D}${includedir}/metrics/
    install -m 0644 ${B}/gen/include/metrics/structured/structured_events.h ${D}${includedir}/metrics/

    install -d ${D}${includedir}/metrics/structured
    install -m 0644 ${S}/structured/event_base.h ${D}${includedir}/metrics/structured/

    install -d ${D}${includedir}/metrics/structured/proto
    install -m 0644 ${B}/gen/include/metrics/structured/proto/storage.pb.h ${D}${includedir}/metrics/structured/proto/
    install -m 0644 ${B}/gen/include/metrics/structured/proto/structured_data.pb.h ${D}${includedir}/metrics/structured/proto/

    # Install the protobuf so that autotests can have access to ti.
    install -d ${D}${includedir}/metrics/proto
    install -m 0644 ${S}/uploader/proto/*.proto ${D}${includedir}/metrics/proto/

}

PROVIDES += "libmetrics"

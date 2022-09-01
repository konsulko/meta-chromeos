SUMMARY = "Protobuf installer for the device policy proto definitions."
DESCRIPTION = "Protobuf installer for the device policy proto definitions."
HOMEPAGE = "https://chromium.googlesource.com/chromium/src/components/policy"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${CHROMEOS_COMMON_LICENSE_DIR}/BSD-Google;md5=29eff1da2c106782397de85224e6e6bc"

inherit chromeos_gn
require recipes-chromeos/chromium/chromium-src-components-policy.inc

# private_membership and shell-encryption are not used in Chrome OS at
# the moment. They are just required to compile the proto files. An
# uprev will only be necessary if the respective proto files change.
require recipes-chromeos/chromium/chromium-src-third-party-private-membership.inc
require recipes-chromeos/chromium/chromium-src-third-party-shell-encryption.inc

SRC_URI += "file://VERSION"

S = "${WORKDIR}/src/chromium/src"
B = "${WORKDIR}/build"

do_configure() {
    :
}

POLICY_DIR = "${S}/components/policy"
PRIVATE_MEMBERSHIP_DIR = "${S}/third_party/private_membership/src"
SHELL_ENCRYPTION_DIR = "${S}/third_party/shell-encryption/src"

# A list of the static protobuf files that exist in Chromium.
POLICY_DIR_PROTO_FILES = " \
    chrome_device_policy.proto \
    chrome_extension_policy.proto \
    device_management_backend.proto \
    install_attributes.proto \
    policy_common_definitions.proto \
    policy_signing_key.proto \
    secure_connect.proto \
"

do_compile() {
    # Generate policy_templates.json
    ${POLICY_DIR}/resources/policy_templates.py \
        --src="${POLICY_DIR}/resources/policy_templates.json" \
        --dest="${POLICY_DIR}/resources/generated_policy_templates.json" \
        || bbfatal "Failed to generate policy_templates.json"

    # Generate cloud_policy.proto.
    ${POLICY_DIR}/tools/generate_policy_source.py \
        --cloud-policy-protobuf="${WORKDIR}/cloud_policy.proto" \
        --chrome-version-file="${WORKDIR}/VERSION" \
        --policy-templates-file="${POLICY_DIR}/resources/generated_policy_templates.json" \
        --target-platform="chrome_os" \
        || bbfatal "Failed to generate cloud_policy.proto"
}

do_install() {
    install -d ${D}${includedir}/proto
    install -m 0644 ${POLICY_DIR}/proto/chrome_device_policy.proto ${D}${includedir}/proto/
    install -m 0644 ${POLICY_DIR}/proto/chrome_extension_policy.proto ${D}${includedir}/proto/
    install -m 0644 ${POLICY_DIR}/proto/install_attributes.proto ${D}${includedir}/proto/
    install -m 0644 ${POLICY_DIR}/proto/policy_signing_key.proto ${D}${includedir}/proto/
    install -m 0644 ${POLICY_DIR}/proto/device_management_backend.proto ${D}${includedir}/proto/
    install -m 0644 ${PRIVATE_MEMBERSHIP_DIR}/private_membership_rlwe.proto ${D}${includedir}/proto/
    install -m 0644 ${PRIVATE_MEMBERSHIP_DIR}/private_membership.proto ${D}${includedir}/proto/
    install -m 0644 ${SHELL_ENCRYPTION_DIR}/serialization.proto ${D}${includedir}/proto/

    install -d ${D}${datadir}/protofiles
    install -m 0644 ${POLICY_DIR}/proto/chrome_device_policy.proto ${D}${datadir}/protofiles/
    install -m 0644 ${POLICY_DIR}/proto/policy_common_definitions.proto ${D}${datadir}/protofiles/
    install -m 0644 ${POLICY_DIR}/proto/device_management_backend.proto ${D}${datadir}/protofiles/
    install -m 0644 ${POLICY_DIR}/proto/chrome_extension_policy.proto ${D}${datadir}/protofiles/
    install -m 0644 ${PRIVATE_MEMBERSHIP_DIR}/private_membership_rlwe.proto ${D}${datadir}/protofiles/
    install -m 0644 ${PRIVATE_MEMBERSHIP_DIR}/private_membership.proto ${D}${datadir}/protofiles/
    install -m 0644 ${SHELL_ENCRYPTION_DIR}/serialization.proto ${D}${datadir}/protofiles/
    install -m 0644 ${WORKDIR}/cloud_policy.proto ${D}${datadir}/protofiles/

    install -d ${D}${datadir}/policy_resources
    install -m 0644 ${POLICY_DIR}/resources/policy_templates.json ${D}${datadir}/policy_resources/
    install -m 0644 ${POLICY_DIR}/resources/generated_policy_templates.json ${D}${datadir}/policy_resources/
    install -m 0644 ${WORKDIR}/VERSION ${D}${datadir}/policy_resources/

    install -d ${D}${datadir}/policy_tools
    install -m 0755 ${POLICY_DIR}/tools/generate_policy_source.py ${D}${datadir}/policy_tools/
}

python do_check_policy_dir_proto_files() {
    import os

    # Retrieve the proto files which exist in that path.
    proto_path = os.path.join(d.getVar('POLICY_DIR'), 'proto')
    policy_dir_proto_files = [fn for fn in os.listdir(proto_path)
              if fn.endswith('.proto')]

    # Check whether all protobuf files that exist in Chromium side have already been installed in protofiles package or
    # not. And to verify that the list in autotests package,which is using these protobuf files is up-to-date.
    if policy_dir_proto_files.sort() != list(d.getVar('POLICY_DIR_PROTO_FILES').split()).sort():
        bb.fatal("Add all new protobuf files into the sorted list of chromium protobuf files, which exist in protofiles package.\n"
                 "Please update all the imported protobuf files in autotest package in policy_protos.py file.")
}

addtask do_check_policy_dir_proto_files after do_install before do_populate_sysroot

FILES:${PN} += "${datadir}"

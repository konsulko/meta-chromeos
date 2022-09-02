# Helper bbclass for building DLC packages.

# We need build_dlc from chromite
inherit chromite

# Handles building the DLC image and metadata files and dropping them into
# locations where they can be picked up by the build process and hosted on
# Omaha, respectively.

# https://chromium.googlesource.com/chromiumos/platform2/+/HEAD/dlcservice/docs/developer.md
# ChromeOS Downloadable Content (DLC)

DLC_BUILD_DIR ??= "${D}/dlc"

# The number of blocks to preallocate for each of the DLC A/B partitions.
# Block size is 4 KiB.
DLC_PREALLOC_BLOCKS ?= ""

# Other optional DLC bbclass variables

# The name of the DLC to show on the UI.
DLC_NAME ??= "${PN}"

# A human readable description for DLC.
DLC_DESCRIPTION ??= ""

# Unique ID for the DLC among all DLCs. Needed to generate metadata for
# imageloader. Used in creating directories for the image file and metadata. It
# cannot contain '_' or '/'.
DLC_ID ??= "${PN}"

# Unique ID for the package in the DLC. Each DLC can have multiple
# packages. Needed to generate metadata for imageloader. Used in creating
# directories for the image file and metadata. It cannot contain '_' or '/'.
DLC_PACKAGE ??= "${BPN}"

# Version of the DLC being built.
DLC_VERSION ??= "${PVR}"

# Specify the type of filesystem for the DLC image. Currently we only support
# squashfs.
DLC_FS_TYPE ??= ""

# Determines whether to preload the DLC for test images. A boolean must be
# passed in.
DLC_PRELOAD ??= "false"

# Determines whether to factory install the DLC into FSI. A boolean must be
# passed in. (Please consult @chromeos-core-services team before using this)
DLC_FACTORY_INSTALL ??= "false"

# Determines whether the package will be a DLC package or regular package.
# By default, the package is a DLC package and the files will be installed in
# ${DLC_BUILD_DIR}/${DLC_ID}/${DLC_PACKAGE}/root, but if the variable is set to
# "false", all the functions will ignore the path suffix and everything that
# would have been installed inside the DLC, gets installed in the rootfs.
DLC_ENABLED ??= "true"

# Determines the user of the DLC, e.g. device users vs. system, so
# dlcservice/UI can make predictable actions based on that. Acceptable values
# are "system" and "user".
DLC_USED_BY ??= "system"

# Defines how many days the DLC should be kept before purging it from disk after
# it has been uninstalled. Default is 5 days.
DLC_DAYS_TO_PURGE ??= "5"

# By default, DLC mount points should be retrieved from the DBUS install method.
# Places where DBus isn't accessible, use this flag to generate a file holding
# the mount point as an indirect method of retrieving the DLC mount point.
DLC_MOUNT_FILE_REQUIRED ??= "false"

# Determines whether to always eagerly reserve space for the DLC on disk.
# This should only be used by DLCs which always requires space on the device.
# (Please consult @chromeos-core-services team before using this)
DLC_RESERVED ??= "false"

# Determines whether to always update the DLC with the OS atomically.
# (Please consult @chromeos-core-services team before using this)
DLC_CRITICAL_UPDATE ??="false"

# @USAGE: <path to add the DLC prefix to>
# @RETURN:
# Adds the DLC path prefix to the argument based on the value of |DLC_ENABLED|
# and returns that value.
dlc_add_path() {
	if [ ! "$#" -eq 1 ]; then
		bbfatal "dlc_add_path: takes one argument"
	fi
	input_path=${1}
	if [ "${DLC_ENABLED}" != "true" ]; then
		echo "${input_path}"
	else
		if [ -z "${DLC_ID}" ]; then
			bbfatal "DLC_ID undefined"
		fi
		if [ -z "${DLC_PACKAGE}" ]; then
			bbfatal "DLC_PACKAGE undefined"
		fi
		echo "${DLC_BUILD_DIR}/${DLC_ID}/${DLC_PACKAGE}/root/${input_path}"
	fi
}

# FIXME: needs translation to POSIX shell
#
# Installs DLC files into
# ${DLC_BUILD_DIR}/${DLC_ID}/${DLC_PACKAGE}/root.
dlc_do_install() {
        if [ ! "${DLC_ENABLED}" =~ ^(true|false)$ ]; then bbfatal "Invalid DLC_ENABLED value"
        if [ "${DLC_ENABLED}" != "true" ]; then
                return
        fi

        # Required.
        if [ -z "${DLC_NAME}" ]; then bbfatal "DLC_NAME undefined"
        if [ -z "${DLC_PREALLOC_BLOCKS}" ]; then bbfatal "DLC_PREALLOC_BLOCKS undefined"

        # Optional, but error if derived default values are empty.
        : "${DLC_DESCRIPTION:=${DESCRIPTION}}"
        [[ -z "${DLC_DESCRIPTION}" ]] && bbfatal "DLC_DESCRIPTION undefined"
        [[ -z "${DLC_ID}" ]] && bbfatal "DLC_ID undefined"
        [[ -z "${DLC_PACKAGE}" ]] && bbfatal "DLC_PACKAGE undefined"
        [[ -z "${DLC_VERSION}" ]] && bbfatal "DLC_VERSION undefined"
        [[ "${DLC_PRELOAD}" =~ ^(true|false)$ ]] || bbfatal "Invalid DLC_PRELOAD value"
        [[ "${DLC_FACTORY_INSTALL}" =~ ^(true|false)$ ]] || bbfatal "Invalid DLC_FACTORY_INSTALLvalue"
        [[ -z "${DLC_USED_BY}" ]] && bbfatal "DLC_USED_BY undefined"
        [[ "${DLC_MOUNT_FILE_REQUIRED}" =~ ^(true|false)$ ]] \
                || bbfatal "Invalid DLC_MOUNT_FILE_REQUIRED value"
        [[ "${DLC_RESERVED}" =~ ^(true|false)$ ]] \
                || bbfatal "Invalid DLC_RESERVED value"
        [[ "${DLC_CRITICAL_UPDATE}" =~ ^(true|false)$ ]] \
                || bbfatal "Invalid DLC_CRITICAL_UPDATE value"

        local args=(
                --install-root-dir="${D}"
                --pre-allocated-blocks="${DLC_PREALLOC_BLOCKS}"
                --version="${DLC_VERSION}"
                --id="${DLC_ID}"
                --package="${DLC_PACKAGE}"
                --name="${DLC_NAME}"
                --description="${DLC_DESCRIPTION}"
                --fullnamerev="${CATEGORY}/${PF}"
                --build-package
                --days-to-purge="${DLC_DAYS_TO_PURGE}"
        )

        if [[ -n "${DLC_FS_TYPE}" ]]; then
                args+=( --fs-type="${DLC_FS_TYPE}" )
        fi

        if [[ "${DLC_PRELOAD}" == "true" ]]; then
                args+=( --preload )
        fi

        if [[ "${DLC_FACTORY_INSTALL}" == "true" ]]; then
                args+=( --factory-install )
        fi

        if [[ -n "${DLC_USED_BY}" ]]; then
                args+=( --used-by="${DLC_USED_BY}" )
        fi

        if [[ "${DLC_MOUNT_FILE_REQUIRED}" == "true" ]]; then
                args+=( --mount-file-required )
        fi

        if [[ "${DLC_RESERVED}" == "true" ]]; then
                args+=( --reserved )
        fi

        if [[ "${DLC_CRITICAL_UPDATE}" == "true" ]]; then
                args+=( --critical-update )
        fi

        "${CHROMITE_BIN_DIR}"/build_dlc "${args[@]}" \
                || bbfatal "build_dlc failed."
}

EXPORT_FUNCTIONS do_install

SUMMARY = "A basic image that emulates ChromeOS base-image."

IMAGE_FEATURES += "splash"

LICENSE = "MIT"

inherit chromeos-image

IMAGE_INSTALL = "packagegroup-chromeos-base ${CORE_IMAGE_EXTRA_INSTALL} ${CHROMEOS_IMAGE_EXTRA_INSTALL}"

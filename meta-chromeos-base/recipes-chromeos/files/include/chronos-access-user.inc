# chronos-access:!:1001:1001:non-chronos user with access to chronos data:/dev/null:/bin/false
GROUPADD_PARAM:${PN}:append = "; --system --gid 1001 chronos-access"
USERADD_PARAM:${PN}:append = "; --uid 1001 --gid chronos-access --comment 'non-chronos user with access to chronos data' --home-dir /dev/null --system --shell /bin/false chronos-access"


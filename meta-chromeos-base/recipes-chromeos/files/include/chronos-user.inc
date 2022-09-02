# chronos:x:1000:1000:system_user:/home/chronos/user:/bin/bash
GROUPADD_PARAM:${PN}:append = "; --gid 1000 chronos"
USERADD_PARAM:${PN}:append = "; --uid 1000 --gid chronos --comment 'system_user' --home-dir /home/chronos/user --create-home --shell /bin/bash chronos"


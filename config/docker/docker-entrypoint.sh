#!/bin/bash
set -e

if [[ "$1" = "start" ]]; then
    shift # skip start argument
    echo "Adding USER: user:password to ApplicationRealm"
    /opt/eap/bin/add-user.sh -a user -p password
    cp /asd/ear.ear /deployments
    export ENABLE_JSON_LOGGING=TRUE
    exec /opt/eap/bin/openshift-launch.sh
fi

exec "$@"
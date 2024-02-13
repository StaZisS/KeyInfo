#!/bin/bash

if [ $# -lt 2 ]; then
    echo "nc.sh: hostname port" >&2
    exit 1
fi

host=$1
port=$2

(echo -n > /dev/tcp/$host/$port) >/dev/null 2>&1
result=$?

if [ $result -eq 0 ]; then
    echo "Connection to $host $port port succeeded!"
    exit 0
else
    echo "nc: connect to $host port $port (tcp) failed: Connection refused"
    exit 1
fi
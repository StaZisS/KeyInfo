#!/bin/sh

echo "Waiting for $host port $port..."
if [ -x ./nc.sh ]; then
    timeout $tmout sh -c 'until ./nc.sh "$host" "$port"; do echo -n ".";
    sleep 1; done'
else
    timeout $tmout sh -c 'until nc -vz "$host" "$port"; do echo -n ".";
    sleep 1; done'
fi
rc=$?
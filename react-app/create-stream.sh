#!/bin/bash
port=${1:-8080}
count=0
accessToken=<your access token>

profile () {
  ((count++))
  echo "posting #${count}"
  http POST http://localhost:${port}/profiles email="random${count}" "Authorization: Bearer ${accessToken}"
  if [ $count -gt 120 ]
  then
    echo "count is $count, ending..."
    break
  fi
}

while sleep 1; do profile; done
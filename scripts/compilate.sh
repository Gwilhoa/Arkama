#!/bin/bash
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

mvn verify -f $SCRIPT_DIR/../pom.xml
rm -rf $SCRIPT_DIR/../rendu
(mkdir $SCRIPT_DIR/../rendu && mv $SCRIPT_DIR/../data/**/target/*.jar $SCRIPT_DIR/../rendu && rm -rf $SCRIPT_DIR/../envs/dev/server/plugins && mkdir $SCRIPT_DIR/../envs/dev/server/plugins && cp $SCRIPT_DIR/../rendu/* $SCRIPT_DIR/../envs/dev/server/plugins/)

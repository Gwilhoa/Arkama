#!/bin/bash
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

mvn verify -f $SCRIPT_DIR/../pom.xml
(mkdir $SCRIPT_DIR/../rendu && mv $SCRIPT_DIR/../data/**/target/*.jar $SCRIPT_DIR/../rendu)

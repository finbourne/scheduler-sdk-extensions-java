#!/bin/bash

usage()
{
  echo
  echo "Usage: $0 "
  echo
  echo " -a Application e.g. drive"
  echo
  exit 1
}

while getopts a: flag
do
    case "${flag}" in
        a) application=${OPTARG};;
    esac
done

[ -z "$application" ] && usage

application_lower=${application,,}
application_upper=${application^^}
application_camel=${application^}

echo "Setting up repository for $application_lower-sdk-extensions-java";
echo "Creating directories ..."
mkdir -p sdk/src/main/java/com/finbourne/$application_lower/extensions/auth

echo "Setting up pom.xml ..."
sed -i '' "s/CHANGEME-APPLICATION-LOWER/$application_lower/g" resources/pom.xml
sed -i '' "s/CHANGEME-APPLICATION-UPPER/$application_upper/g" resources/pom.xml
sed -i '' "s/CHANGEME-APPLICATION-CAMEL/$application_camel/g" resources/pom.xml

echo "Setting up GitHub actions ..."
sed -i '' "s/CHANGEME-APPLICATION-LOWER/$application_lower/g" resources/workflows/build-and-test.yaml
sed -i '' "s/CHANGEME-APPLICATION-UPPER/$application_upper/g" resources/workflows/build-and-test.yaml
sed -i '' "s/CHANGEME-APPLICATION-CAMEL/$application_camel/g" resources/workflows/build-and-test.yaml
sed -i '' "s/CHANGEME-APPLICATION-LOWER/$application_lower/g" resources/workflows/cron.yaml
sed -i '' "s/CHANGEME-APPLICATION-UPPER/$application_upper/g" resources/workflows/cron.yaml
sed -i '' "s/CHANGEME-APPLICATION-CAMEL/$application_camel/g" resources/workflows/cron.yaml

echo "Setting up README ..."
sed -i '' "s/CHANGEME-APPLICATION-LOWER/$application_lower/g" resources/README.md
sed -i '' "s/CHANGEME-APPLICATION-UPPER/$application_upper/g" resources/README.md
sed -i '' "s/CHANGEME-APPLICATION-CAMEL/$application_camel/g" resources/README.md

echo "Setting up CONTRIBUTING ..."
sed -i '' "s/CHANGEME-APPLICATION-LOWER/$application_lower/g" docs/CONTRIBUTING.md
sed -i '' "s/CHANGEME-APPLICATION-UPPER/$application_upper/g" docs/CONTRIBUTING.md
sed -i '' "s/CHANGEME-APPLICATION-CAMEL/$application_camel/g" docs/CONTRIBUTING.md

echo "Setting up Docker ..."
sed -i '' "s/CHANGEME-APPLICATION-LOWER/$application_lower/g" resources/Dockerfile
sed -i '' "s/CHANGEME-APPLICATION-UPPER/$application_upper/g" resources/Dockerfile
sed -i '' "s/CHANGEME-APPLICATION-CAMEL/$application_camel/g" resources/Dockerfile
sed -i '' "s/CHANGEME-APPLICATION-LOWER/$application_lower/g" resources/docker-compose.yml
sed -i '' "s/CHANGEME-APPLICATION-UPPER/$application_upper/g" resources/docker-compose.yml
sed -i '' "s/CHANGEME-APPLICATION-CAMEL/$application_camel/g" resources/docker-compose.yml

echo "Moving files ..."
mv resources/pom.xml sdk/
mv resources/Dockerfile sdk/
mv resources/docker-compose.yml sdk/
mv resources/workflows/ .github/
mv resources/README.md ./
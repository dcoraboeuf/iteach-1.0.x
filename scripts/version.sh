#!/bin/bash

# Creation of a version
echo Creating a version from current location

# Gets the current version
MAJOR=`grep SNAPSHOT pom.xml | sed "s/.*\([0-9][0-9]*\)\.\([0-9][0-9]*\)\.\([0-9][0-9]*\).*/\1/"`
MINOR=`grep SNAPSHOT pom.xml | sed "s/.*\([0-9][0-9]*\)\.\([0-9][0-9]*\)\.\([0-9][0-9]*\).*/\2/"`
PATCH=`grep SNAPSHOT pom.xml | sed "s/.*\([0-9][0-9]*\)\.\([0-9][0-9]*\)\.\([0-9][0-9]*\).*/\3/"`
CURRENT_VERSION=${MAJOR}.${MINOR}.${PATCH}
echo Current version = $CURRENT_VERSION

# Gets the next version
let "NEXT_PATCH=$PATCH+1"
NEXT_VERSION=${MAJOR}.${MINOR}.${NEXT_PATCH}
echo Next version = $NEXT_VERSION

# Update the version for the tag
mvn versions:set -DnewVersion=$CURRENT_VERSION -DgenerateBackupPoms=false > /dev/null

# Commits & tags
git commit -am "Version $CURRENT_VERSION"
git tag $CURRENT_VERSION -m "v$CURRENT_VERSION"

# Changes to the next version
mvn versions:set -DnewVersion=$NEXT_VERSION-SNAPSHOT -DgenerateBackupPoms=false > /dev/null

# Commit
git commit -am "Prepare for version $NEXT_VERSION"

# Release property file
echo $CURRENT_VERSION > .release

# End
echo Tag created.
echo Perform the following commands to build and push:
echo git checkout $CURRENT_VERSION
echo mvn clean install -P release -P acceptance -P acceptance-local
echo git checkout release_${MAJOR}.${MINOR}
echo git reset --hard HEAD
echo git push
echo git push --tags
echo Rolling back the version
echo git tag -d $CURRENT_VERSION
echo git reset --hard HEAD~2


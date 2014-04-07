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
mvn versions:set -DnewVersion=$CURRENT_VERSION -DgenerateBackupPoms=false

# Commits & tags
git commit -am "Version $CURRENT_VERSION"
git tag $CURRENT_VERSION -m "v$CURRENT_VERSION"

# Changes to the next version
mvn versions:set -DnewVersion=$NEXT_VERSION-SNAPSHOT -DgenerateBackupPoms=false

# Commit
git commit -am "Prepare for version $NEXT_VERSION"

# End
echo Tag created. Perform the following commands to push:
echo git push
echo git push --tags

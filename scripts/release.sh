#!/bin/bash

# Creation of a branch
echo Creating a branch from current location

# Gets the current version
MAJOR=`grep SNAPSHOT pom.xml | sed "s/.*\([0-9][0-9]*\)\.\([0-9][0-9]*\)\.\([0-9][0-9]*\).*/\1/"`
MINOR=`grep SNAPSHOT pom.xml | sed "s/.*\([0-9][0-9]*\)\.\([0-9][0-9]*\)\.\([0-9][0-9]*\).*/\2/"`
PATCH=`grep SNAPSHOT pom.xml | sed "s/.*\([0-9][0-9]*\)\.\([0-9][0-9]*\)\.\([0-9][0-9]*\).*/\3/"`
RELEASE=${MAJOR}.${MINOR}
CURRENT_VERSION=${MAJOR}.${MINOR}.${PATCH}
echo Current version = $CURRENT_VERSION

# Checks for a release branch
if [ "$PATCH" != "0" ]
then
    echo Cannot create a release branch from a patch
    exit 1
fi

# Gets the next version
let "NEXT_MINOR=$MINOR+1"
NEXT_RELEASE=${MAJOR}.${NEXT_MINOR}
NEXT_VERSION=${MAJOR}.${NEXT_MINOR}.0
echo Next version = $NEXT_VERSION

# Update the version for the branch
mvn versions:set -DnewVersion=$CURRENT_VERSION-SNAPSHOT -DgenerateBackupPoms=false

# Creates the branch
git checkout -b release_$RELEASE
git commit -am "Release branch for $RELEASE"

# Goes back to the main branch
git checkout master

# Changes to the next version
mvn versions:set -DnewVersion=$NEXT_VERSION-SNAPSHOT -DgenerateBackupPoms=false

# Commit
git commit -am "Prepare for release $NEXT_RELEASE"

# End
echo Release branch created locally. Perform the following commands to push:
echo git checkout release_$RELEASE
echo git push origin release_$RELEASE --set-upstream
echo git checkout master
echo git push origin

/**
 * List of global parameters:
 *
 * - JDK8u25
 *
 * List of parameters (see https://github.com/nemerosa/seed):
 *
 * - PROJECT
 * - BRANCH
 * - SCM_URL
 */

/**
 * Folder for the project (making sure)
 */

folder {
    name PROJECT
}

/**
 * Branch
 */

// Branch type
def branchType
int pos = BRANCH.indexOf('/')
if (pos > 0) {
    branchType = BRANCH.substring(0, pos)
} else {
    branchType = BRANCH
}
println "BRANCH = ${BRANCH}"
println "\tBranchType = ${branchType}"

// Keeps only some version types
if (['master', 'feature', 'release', 'hotfix'].contains(branchType)) {

    // Normalised branch name
    def NAME = BRANCH.replaceAll(/[^A-Za-z0-9\.\-_]/, '-')
    println "\tGenerating ${NAME}..."

    // Folder for the branch
    folder {
        name "${PROJECT}/${PROJECT}-${NAME}"
    }

    // Build job
    job {
        name "${PROJECT}/${PROJECT}-${NAME}/${PROJECT}-${NAME}-build"
        logRotator(-1, 40)
        deliveryPipelineConfiguration('Commit', 'Build')
        jdk 'JDK8u25'
        scm {
            git {
                remote {
                    url "git@github.com:nemerosa/iteach.git"
                    branch "origin/${BRANCH}"
                }
                wipeOutWorkspace()
                localBranch "${BRANCH}"
            }
        }
        triggers {
            scm 'H/5 * * * *'
        }
        steps {
            // Version computation
            gradle 'versionDisplay versionFile --info'
            // Loading version information
            environmentVariables {
                propertiesFile 'target/version.properties'
            }
            // Maven version
            maven {
               mavenInstallation 'Maven-3.2.x'
               goals 'versions:set'
               property 'newVersion', '${VERSION_DISPLAY}'
               property 'generateBackupPoms', 'false'
            }
        }
        publishers {
            tasks(
                    '**/*.java,**/*.groovy,**/*.xml,**/*.html,**/*.js',
                    '**/target/**,**/node_modules/**,**/vendor/**',
                    'FIXME', 'TODO', '@Deprecated', true
            )
        }
    }


} else {
    println "\tSkipping ${BRANCH}."
}

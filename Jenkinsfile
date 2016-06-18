// TODO Dedicated build node?
node {

    // Checkout
    stage 'Checkout'

    // Get some code from a GitHub repository
    git url: 'https://github.com/nemerosa/iteach.git', branch: env.BRANCH_NAME

    // Get the JDK
    def javaHome = tool name: 'JDK8u74', type: 'hudson.model.JDK'

    // Mark the code build 'stage'....
    stage 'Build'
    // Runs the Gradle build
    sh """./gradlew \\
        clean \\
        versionDisplay \\
        versionFile \\
        build \\
        --info \\
        --stacktrace \\
        --profile \\
        --console plain \\
        --no-daemon \\
        -Dorg.gradle.java.home=${javaHome}
        """
    // Archiving the tests
    step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*.xml'])
    // TODO Ontrack build

}
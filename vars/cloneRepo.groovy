def call(String repoUrl, String branch = 'main', String credentialsId = '') {
    checkout([
        $class: 'GitSCM',
        branches: [[name: "*/${branch}"]],
        userRemoteConfigs: [[url: repoUrl, credentialsId: credentialsId]]
    ])
}


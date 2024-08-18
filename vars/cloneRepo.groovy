def call(String repoUrl, String branch = 'main', String credentialsId = '') {
    checkout([
        $class: 'GitSCM',
        branches: [[name: "*/${main}"]],
        userRemoteConfigs: [[url: 'https://github.com/Venkat0406-aws/Maven.git', credentialsId: 'Git']]
    ])
}


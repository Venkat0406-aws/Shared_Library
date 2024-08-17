def call(Map params) {
    pipeline {
        agent any

        tools {
            maven 'Maven' // Use the name of your Maven installation in Jenkins
        }

        stages {
            stage('Checkout') {
                steps {
                    git url: params.repoUrl, branch: params.branchName
                }
            }

            stage('Build') {
                steps {
                    sh 'mvn clean install -DskipTests'
                }
            }

            stage('Test') {
                steps {
                    sh 'mvn test'
                }
            }

            stage('Deploy to Nexus') {
                steps {
                    nexusArtifactUploader artifacts: [[artifactId: params.artifactId, classifier: '', file: params.filePath, type: 'war']], credentialsId: 'Nexus', groupId: params.groupId, nexusUrl: params.nexusUrl, nexusVersion: 'nexus3', protocol: 'http', repository: params.nexusRepo, version: params.version
                }
            }

            stage('SonarQube analysis') {
                steps {
                    script {
                        scannerHome = tool 'SonarQube'
                    }
                    withSonarQubeEnv('SonarQube') {
                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${params.sonarProjectKey} -Dsonar.sources=${params.sonarSources}"
                    }
                }
            }
        }
    }
}


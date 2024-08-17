def call(String repoUrl, String branchName, String artifactId, String groupId, String version, String filePath, String nexusUrl, String nexusRepo, String sonarProjectKey, String sonarSources) {
    pipeline {
        agent any

        tools {
            maven 'Maven' // Use the name of your Maven installation in Jenkins
        }

        stages {
            stage('Checkout') {
                steps {
                    git url: repoUrl, branch: branchName
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
                    nexusArtifactUploader artifacts: [[artifactId: artifactId, classifier: '', file: filePath, type: 'war']], credentialsId: 'Nexus', groupId: groupId, nexusUrl: nexusUrl, nexusVersion: 'nexus3', protocol: 'http', repository: nexusRepo, version: version
                }
            }

            stage('SonarQube analysis') {
                steps {
                    script {
                        scannerHome = tool 'SonarQube'
                    }
                    withSonarQubeEnv('SonarQube') {
                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${sonarProjectKey} -Dsonar.sources=${sonarSources}"
                    }
                }
            }
        }
    }
}


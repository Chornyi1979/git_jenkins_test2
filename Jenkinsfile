pipeline {
    agent any
    stages {
        stage('Clone repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Chornyi1979/git_jenkins_test2.git/'
            }
        }
        stage('Static code analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Build Docker image') {
            steps {
                sh 'docker build -t chornyi1979/my-jenkins-agent:latest .'
            }
        }
        stage('Tag Docker image') {
            steps {
                script {
                    def dockerImage = docker.build("chornyi1979/my-jenkins-agent:${env.BUILD_NUMBER}")
                    dockerImage.push()
                    dockerImage.tag("chornyi1979/my-jenkins-agent:latest")
                    dockerImage.push("chornyi1979/my-jenkins-agent:latest")
                }
            }
        }
        stage('Push Docker image to Docker Hub') {
            steps {
                sh 'docker login -u ochornyi -p 1979Ch1922$'
                sh 'docker push chornyi1979/my-jenkins-agent:${env.BUILD_NUMBER}'
                sh 'docker push chornyi1979/my-jenkins-agent:latest'
            }
        }
    }
    post {
        success {
            slackSend (color: '#36a64f', message: "Build successful!")
        }
        failure {
            slackSend (color: '#ff0000', message: "Build failed!")
        }
    }
}

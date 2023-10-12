pipeline {
    agent any
    stages {
        stage('Clone repository') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo.git'
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
                sh 'docker build -t your-dockerhub-username/your-image-name:latest .'
            }
        }
        stage('Tag Docker image') {
            steps {
                script {
                    def dockerImage = docker.build("your-dockerhub-username/your-image-name:${env.BUILD_NUMBER}")
                    dockerImage.push()
                    dockerImage.tag("your-dockerhub-username/your-image-name:latest")
                    dockerImage.push("your-dockerhub-username/your-image-name:latest")
                }
            }
        }
        stage('Push Docker image to Docker Hub') {
            steps {
                sh 'docker login -u your-dockerhub-username -p your-dockerhub-password'
                sh 'docker push your-dockerhub-username/your-image-name:${env.BUILD_NUMBER}'
                sh 'docker push your-dockerhub-username/your-image-name:latest'
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

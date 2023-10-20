def gv
pipeline {
    agent any
    tools {
        maven 'maven-3.9'
	
    }
    stages {

        stage ("init") {
            steps {
                script {
                    gv = load "script.groovy_1"
                }
            }
        }

    
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

        stage ("increment version") {
            steps {
                script{
                    gv.incrementVersion()
                }
            }
        }
        stage ("build jar") {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }
        stage ("build docker images") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }

        stage ("commit version update") {
            steps {
                script {
                    gv.commitVersionUpdate()
                }
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

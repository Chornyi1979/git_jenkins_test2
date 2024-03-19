def gv
pipeline {
    agent any

    triggers {
        GenericTrigger(
            genericVariables: [
            [
                key: 'event',
                value: '$.event'
            ],
            [
                key: 'action',
                value: '$.action'
            ],
            [
                key: 'merged',
                value: '$.pull_request.merged'
            ]
        ],
    
        printContributedVariables: true,
        causeString: 'Webhook triggered',
        printPostContent: true,
        regexpFilterText: '$.pull_request.merged == true && $.event == "pull_request" && $.action == "closed" && $.pull_request.base.ref == "dev"',
        regexpFilterExpression: true
        )
    }
   
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



        stage ("commit GitHub version update") {
            steps {
                script {
                    gv.commitVersionUpdate()
                }
            }   
        }
    }

    post {

        success {
            emailext body: 'Build successful!', subject: 'Build Notification', to: 'ochornyy1979@meta.ua'
        }
        failure {
            emailext body: 'Build failed!', subject: 'Build Notification', to: 'ochornyy1979@meta.ua'
        }
    }
}

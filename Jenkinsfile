def gv
pipeline {
    agent any
   
    tools {
        maven 'maven-3.9'
        dockerTool 'docker'
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

        stage("Get Available Versions") {
            steps {
                script {
                  def versions = []
                  withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                    sh "echo $PASS | docker login -u $USER --password-stdin"
                    sh "docker images chornyi1979/my-repo"
                    def process = sh(script: 'docker images chornyi1979/my-repo', returnStdout: true)
                    versions = process.text.trim().split('\n')
                  }
                  def versionParam = input(
                    id: 'versionInput',
                    message: 'Select version',
                    parameters: [
                        choice(choices: versions, description: 'Select version', name: 'VERSION')
                    ]
                  )
                  env.VERSION = versionParam
                }
            }  
        }


        stage ("deploy") {
            steps {
                script {
                    gv.deployApp()
                }
            }   
        }

        stage('Healthcheck') {
            steps {
                script {
                    gv.healthcheck()
                    echo "Done"
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

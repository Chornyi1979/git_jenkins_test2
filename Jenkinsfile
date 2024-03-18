def gv
pipeline {
    agent any

    parameters {
        choice(
            choices: ['test', 'preprod', 'prod'],
            description: 'Select the environment to deploy',
            name: 'ENVIRONMENT'
        )
    }
   
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

        stage("Choice Versions and Deploy") {
          steps {
            withCredentials([usernamePassword(credentialsId: 'docker-hub-api-token', variable: 'dckr_pat_IeGSdIcxy1KOLMOMZTxMdLmAJlY')]) {             
                script {
                  def versions = []
                  def apiUrl = 'https://hub.docker.com/v2/repositories/chornyi1979/my-repo/tags'
                  
                  def response = sh(script: "curl -s ${apiUrl}", returnStdout: true)
                  echo "Response: ${response}"
                  def json = readJSON text: response
                  if (json.results) {
                    json.results.each { result ->
                      def name = result.name
                      versions.add(name)
                    }
                    echo "Available Versions: ${versions}"
                    
                    def selectedVersion = null
                    while (selectedVersion == null || !versions.contains(selectedVersion)) {
                      selectedVersion = input(
                        id: 'versionInput',
                        message: 'Select version',
                        parameters: [
                          choice(choices: versions, description: 'Select version', name: 'VERSION')
                        ]
                      )
                      if (!versions.contains(selectedVersion)) {
                        echo "Invalid version selected. Please select a valid version."
                      }
                    }
                    gv.deployApp(selectedVersion)
                    echo "Selected Version: ${selectedVersion}"
                    sh "docker pull chornyi1979/my-repo:${selectedVersion}"
                      
                  } 
                  else {
                  error "Failed to retrieve available versions."
                  }
                }
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

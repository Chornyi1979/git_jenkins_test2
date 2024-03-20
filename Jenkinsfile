def gv
properties([
  parameters([
    [$class: 'ChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select version image',
      filterLength: 1,
      filterable: false,
      name: 'VERSION', 
      script: [
            $class: 'GroovyScript',
            fallbackScript: [classpath: [], sandbox: false, script: 'return ["Could not get version"]'],
            script: [
                classpath: [], sandbox: false,
                script: """
                  import groovy.json.JsonSlurperClassic
                  import java.net.HttpURLConnection
                  import java.net.URL

                   
                   def connection = new URL("https://hub.docker.com/v2/repositories/chornyi1979/my-repo/tags")
                   def connection = url.openConnection() as HttpURLConnection
                   connection.requestMethod = "GET"
                   connection.connect()

                   if (connection.responseCode == 200) {
                        def jsonSlurper = new JsonSlurperClassic()
                        def response = jsonSlurper.parse(connection.inputStream)
                       
                        def results = response.results
                       
                        def tags = results.collect { it.name }
                        return tags
                   } else {
                        return ["Could not get version"]
                   }
                }
                                   
                 
                """
            ]
        ]
    ]
  ])
])
pipeline {
    agent any

    tools {
        maven 'maven-3.9'
        dockerTool 'docker'
    }
    parameters {
        choice(
            choices: ['test', 'preprod', 'prod'],
            description: 'Select the environment to deploy',
            name: 'ENVIRONMENT'
        )
        
       
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
                script {
                    gv.deployApp(selectedVersion)
                    echo "Selected Version: ${selectedVersion}"
                    sh "docker pull chornyi1979/my-repo:${selectedVersion}"                   
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


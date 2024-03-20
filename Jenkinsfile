def gv
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
        activeChoice(
            choiceType: 'PT_SINGLE_SELECT',
            description: 'Select version',
            filterLength: 1,
            filterable: false,
            name: 'component',
                $class: 'DynamicReferenceParameter',
                script: [
                    $class: 'GroovyScript',
                    fallbackScript: [classpath: [], sandbox: false, script: 'return ["Could not get version"]'],
                    script: [
                        classpath: [], sandbox: false, 
                        script: """
                          import groovy.json.JsonSlurperClassic
                          import java.net.HttpURLConnection
                          import java.net.URL
                          
                            def list = []
                            def apiUrl = 'https://hub.docker.com/v2/repositories/chornyi1979/my-repo/tags'
                            def connection = new URL(apiUrl).openConnection() as HttpURLConnection
                            .openConnection() as HttpURLConnection
                            connection.setRequestProperty('Accept', 'application/json')
                            def json = connection.inputStream.text
                            data = new JsonSlurperClassic().parseText(json)
                            if (json.results) {
                                json.results.each { result ->
                                    def name = result.name
                                    imageList.add(name)
                                }
                                echo "Available Versions: ${list}"
                            } else {
                                error "Failed to retrieve available versions."
                            }
                            
                            return list
                        """
                    ]
                ]
            ]
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


def gv
pipeline {
    agent any

    tools {
        maven 'maven-3.9'
        dockerTool 'docker'
    }
    parameters([
        [$class: 'ChoiceParameter',
            choiceType: 'PT_SINGLE_SELECT',
            description: 'Select a choice',
            filterLength: 1,
            filterable: false,
            name: 'component',
            script: [$class: 'GroovyScript',
                fallbackScript: [classpath: [], sandbox: false, script: 'return ["Could not get component"]'],
                script: [classpath: [], sandbox: false, 
                    script: """
                         import groovy.json.JsonSlurperClassic
                            def list = []
                            def connection = new URL("https://run.mocky.io/v3/e406ee99-be79-4d50-818f-b186dad7f4f4")
                            .openConnection() as HttpURLConnection
                            connection.setRequestProperty('Accept', 'application/json')
                            def json = connection.inputStream.text
                            data = new JsonSlurperClassic().parseText(json)
                            data.each { component ->
                                list += component.name
                            }
                            return list
                    """
    ]]]])
    
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


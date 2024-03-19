def gv
pipeline {
    agent any

    parameters {
        choice(
            choices: ['test', 'preprod', 'prod'],
            description: 'Select the environment to deploy',
            name: 'ENVIRONMENT'
        )
        activeChoice(
            name: 'VERSION',
            description: 'Select version',
            choices: [
                $class: 'DynamicReferenceParameter',
                script: [
                    $class: 'GroovyScript',
                    fallbackScript: [classpath: [], sandbox: false, script: 'return ["Could not get version"]'],
                    script: [
                        classpath: [], sandbox: false, 
                        script: """
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
                            } else {
                                error "Failed to retrieve available versions."
                            }
                            
                            return versions
                        """
                    ]
                ]
            ]
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


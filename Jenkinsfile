import groovy.json.JsonSlurper

def gv
def images = []

node {
    withCredentials([string(credentialsId: 'docker-hub-api-token', variable: 'DOCKER_HUB_TOKEN')]) {
        def dockerHubUsername = 'chornyi1979'
        def dockerHubPassword = DOCKER_HUB_TOKEN

        // Search all images from Docker Hub
        def imagesCommand = "docker login -u ${dockerHubUsername} -p ${dockerHubPassword} && docker search --no-trunc ${dockerHubUsername}"
        echo "Command: ${imagesCommand}"
        def searchOutput = sh(script: imagesCommand, returnStdout: true).trim()
        def searchLines = searchOutput.split('\n')

        searchLines.each { line ->
            def image = line.replaceAll('\t', '')
            images.add(image)
        }
    }
}

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
                    return images
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
        
        stage('Pull Image') {
            steps {
                script {
                    def selectedVersion = params.VERSION
                    def imageName = "chornyi1979/my-repo:${selectedVersion}"
                    
                    // Pull the selected version from Docker Hub
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker pull ${imageName}"
                        sh "docker save ${imageName} -o target/my-repo:${selectedVersion}.war"
                 
                    } 
                }
            }
        }
    

        stage("Deploy") {
            steps {
                script {                  
                    gv.deployApp() 
                    
                    // Deploy the image to the selected environment
                    switch (params.ENVIRONMENT) {
                        case 'test':
                            // Deploy to test environment
                            echo "Deploy to test environment"
                            break
                        case 'preprod':
                            // Deploy to preprod environment
                            echo "Deploy to preprod environment"
                            break
                        case 'prod':
                            // Deploy to prod environment
                            echo "Deploy to prod environment"
                            break
                        default:
                            println "Invalid environment selected"
                            break
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


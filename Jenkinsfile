def gv
def user = env.USER
def pass = env.PASS

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
                  import groovy.json.JsonSlurper
                  import groovy.json.JsonSlurperClassic
                                    
                    def url = "https://hub.docker.com/v2/repositories/chornyi1979/my-repo/tags"
                    def connection = new URL(url).openConnection() as HttpURLConnection                   
                    connection.setRequestMethod("GET")
                    
                    String userCredentials = '${user}:${pass}';
                    String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
                    connection.setRequestProperty("Authorization", basicAuth);                  
                    connection.connect()
                    def dockerhub_response = [:]
                    if (connection.responseCode == 200) {
                      dockerhub_response = new JsonSlurper().parseText(connection.inputStream.getText('UTF-8'))
                    } else {
                        println("HTTP response error")
                        System.exit(0)
                    }
                    // Prepare a List to collect the tag names into
                    def image_tag_list = []
                    // Iterate the HashMap of all Tags and grab only their "names" into our List
                    dockerhub_response.results.each { tag_metadata ->
                        image_tag_list.add(tag_metadata.name)    
                    }
                    // The returned value MUST be a Groovy type of List or a related type (inherited from List)
                    // It is necessary for the Active Choice plugin to display results in a combo-box
                    return image_tag_list
                    
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


def gv
pipeline {
    agent any
   
    tools {
        maven 'maven-3.9'	
    }

    parameters {
        activeChoiceReactiveParam('VERSION') {
            description('Select the version to deploy')
            choiceType('PT_SINGLE_SELECT')
            groovyScript {
                script("""
                    def versions = []
                    def url = 'https://hub.docker.com/v2/repositories/chornyi1979/my-repo/tags/?page_size=100'
                    def response = new URL(url).text
                    def json = new groovy.json.JsonSlurper().parseText(response)
                    json.results.each { tag ->
                        versions.add(tag.name)
                    }
                    return versions
                """)
            }
        }
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

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
              def apiUrl = 'https://hub.docker.com/v2/repositories/chornyi1979/my-repo/tags'
              def response = sh(script: "curl -s ${apiUrl}", returnStdout: true)
              def json = readJSON(text: response)
              json.results.each { result ->
                def image = "chornyi1979/my-repo:${result.name}"
                versions.add(image)
              }
              // Теперь у вас есть список доступных образов
              // Вы можете использовать этот список для выбора нужной версии образа
              echo "Available Versions: ${versions}"
              
              // Здесь вы можете добавить логику для выбора нужной версии образа
              // Например, используйте input для выбора версии образа
              def selectedVersion = input(
                id: 'versionInput',
                message: 'Select version',
                parameters: [
                  choice(choices: versions, description: 'Select version', name: 'VERSION')
                ]
              )
              
              // Теперь у вас есть выбранная версия образа
              echo "Selected Version: ${selectedVersion}"
              
              // Здесь вы можете продолжить с загрузкой выбранной версии образа на ваше окружение
              sh "docker pull ${selectedVersion}"
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

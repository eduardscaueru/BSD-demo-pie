pipeline {
    agent any
    tools {
        maven
    }
    triggers {
        pollSCM 'H/2 * * * *'
    }
    stages {
        stage('Test') {
            steps {
                echo "Run tests"
                sh "mvn clean verify"
            }
        }
    }
}
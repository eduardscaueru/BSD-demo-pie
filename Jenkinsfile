pipeline {
    agent any
    tools {
        maven 'Maven'
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
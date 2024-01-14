pipeline {
    agent any
    tools {
        maven
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
pipeline {
    agent any
    tools {
        Maven
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
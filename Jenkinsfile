pipeline {
    agent any
    tools {
        maven 'Maven 3.3.9'
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
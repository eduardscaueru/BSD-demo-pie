pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    triggers {
        cron ('H/5 * * * *')
    }
    stages {
        stage('Test') {
            steps {
                echo "Run tests"
                sh "mvn clean verify -s .mvn/settings.xml"
            }
        }
    }
}
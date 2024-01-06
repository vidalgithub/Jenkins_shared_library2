def call() {
    withSonarQubeEnv('sonarqube-9.9') {
        sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=youtube -Dsonar.projectKey=youtube '''
    }
}

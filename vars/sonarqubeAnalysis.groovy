def call() {
    withSonarQubeEnv('sonarqube-9.9') {
        sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=Youtube1 -Dsonar.projectKey=Youtube1 '''
    }
}

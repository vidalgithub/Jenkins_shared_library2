@Library('Jenkins_shared_library') _
def COLOR_MAP = [
    'FAILURE' : 'danger',
    'SUCCESS' : 'good'
]

pipeline{
    // agent any
    agent {
        label 'kemgou'
    }
    options {
      buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '1', numToKeepStr: '5')
      timestamps()
    }
    parameters {
      choice choices: ['create', 'delete'], description: 'Select create or delete', name: 'action'
      string defaultValue: 'vidaldocker', description: 'Docker Hub Username', name: 'DOCKER_HUB_USERNAME'
      string defaultValue: 'youtube', description: 'Docker Image Name', name: 'IMAGE_NAME'
      listGitBranches branchFilter: '.*', credentialsId: '', defaultValue: 'main', description: 'Choose a branch to build', listSize: '5', name: 'branchName', quickFilterEnabled: false, remoteURL: 'https://github.com/vidalgithub/Jenkins_shared_library2.git', selectedValue: 'NONE', sortMode: 'NONE', tagFilter: '*', type: 'PT_BRANCH'
    }
    // parameters {
    //     choice(name: 'action', choices: 'create\ndelete', description: 'Select create or destroy.')
    //     string(name: 'DOCKER_HUB_USERNAME', defaultValue: 'vidaldocker', description: 'Docker Hub Username')
    //     string(name: 'IMAGE_NAME', defaultValue: 'youtube', description: 'Docker Image Name')
    // }
    tools{
        jdk 'jdk17'
        nodejs 'node16'
    }
    environment {
        SCANNER_HOME=tool 'sonar-scanner'
    }
    stages{
        stage('clean workspace'){
            steps{
                cleanWorkspace()
            }
        }
        stage('checkout from Git'){
            steps{
                checkoutGit('https://github.com/vidalgithub/Youtube-clone-app.git', 'main')
            }
        }
        stage('sonarqube Analysis'){
        when { expression { params.action == 'create'}}    
            steps{
                sonarqubeAnalysis()
            }
        }
        stage('sonarqube QualityGate'){
        when { expression { params.action == 'create'}}    
            steps{
                script{
                    def credentialsId = 'sonarqube-token'
                    qualityGate(credentialsId)
                }
            }
        }
        stage('Npm'){
        when { expression { params.action == 'create'}}    
            steps{
                npmInstall()
            }
        }
        stage('Trivy file scan'){
        when { expression { params.action == 'create'}}    
            steps{
                trivyFs()
            }
        }
        stage('OWASP FS SCAN') {
            steps {
                dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'DP-Check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage('Docker Build'){
        when { expression { params.action == 'create'}}    
            steps{
                script{
                   def dockerHubUsername = params.DOCKER_HUB_USERNAME
                   def imageName = params.IMAGE_NAME
                   
                   dockerBuild(dockerHubUsername, imageName)
                }
            }
        }
        stage('Trivy image'){
        when { expression { params.action == 'create'}}    
            steps{
                trivyImage()
            }
        }
        stage('Run container'){
        when { expression { params.action == 'create'}}    
            steps{
                runContainer()
            }
        }
        stage('Remove container'){
        when { expression { params.action == 'delete'}}    
            steps{
                removeContainer()
            }
        }
        stage('Kube deploy'){
        when { expression { params.action == 'create'}}    
            steps{
                kubeDeploy()
            }
        }
        stage('kube delete'){
        when { expression { params.action == 'delete'}}    
            steps{
                kubeDelete()
            }
        }
    }
    post {
    always {
        echo 'Slack Notifications'
        slackSend (
            channel: '#jenkins-pipelines',   
            color: COLOR_MAP[currentBuild.currentResult],
            message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} \n build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
        )
    }
}

}

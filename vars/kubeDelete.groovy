def call() {
    withKubeConfig(caCertificate: '', clusterName: '', contextName: '', credentialsId: 'kubeconfig', namespace: '', restrictKubeConfigAccess: false, serverUrl: '') {
        sh "kubectl delete -f deployment.yml"
    }
}

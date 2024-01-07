def call() {
    sh 'trivy image vidaldocker/youtube:latest > trivyimage.txt'
}

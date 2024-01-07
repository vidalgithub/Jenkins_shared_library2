def call(String dockerHubUsername, String imageName) {
    // Build the Docker image
    sh "docker build --build-arg REACT_APP_RAPID_API_KEY=42e2928167mshd20e5fa6074323fp174f73jsn41b6161ea15d -t ${imageName} ."
     // Tag the Docker image
    sh "docker tag ${imageName} ${dockerHubUsername}/${imageName}:latest"
    // Push the Docker image
    withDockerRegistry([url: 'https://index.docker.io/v1/', credentialsId: 'dockerhub']) {
        sh "docker push ${dockerHubUsername}/${imageName}:latest"
    }
}

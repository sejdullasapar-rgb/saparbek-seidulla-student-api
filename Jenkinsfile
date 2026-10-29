pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    tools {
        jdk 'jdk21'
    }

    environment {
        STUDENT_NAME = 'Saparbek Seidulla'
        IMAGE_NAME   = 'saparbek-seidulla-student-api'
        IMAGE_TAG    = 'v1.1-saparbek-seidulla'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Pipeline of student: ${env.STUDENT_NAME}"
                echo "Branch: ${env.BRANCH_NAME ?: 'main'}"
                sh 'git log -1 --oneline'
            }
        }

        stage('Build') {
            steps {
                echo "Building Spring Boot app of ${env.STUDENT_NAME} with Gradle"
                sh 'chmod +x gradlew && ./gradlew --no-daemon clean assemble'
            }
        }

        stage('Test') {
            steps {
                echo "Running automated tests for ${env.STUDENT_NAME}"
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building image ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                sh 'docker build -t "$IMAGE_NAME:$IMAGE_TAG" --label "jenkins.build=$BUILD_TAG" .'
            }
        }

        stage('Verify') {
            steps {
                sh '''
                    docker image inspect "$IMAGE_NAME:$IMAGE_TAG" \
                      --format 'Image {{index .RepoTags 0}} | created {{.Created}} | size {{.Size}} bytes'
                    docker images "$IMAGE_NAME"
                '''
                echo "VERIFIED: image ${env.IMAGE_NAME}:${env.IMAGE_TAG} exists (student: ${env.STUDENT_NAME})"
            }
        }
    }

    post {
        success {
            echo "SUCCESS: pipeline of ${env.STUDENT_NAME} finished, image ${env.IMAGE_NAME}:${env.IMAGE_TAG} is ready"
        }
        failure {
            echo "FAILURE: pipeline of ${env.STUDENT_NAME} stopped. Check the failed stage above."
        }
    }
}

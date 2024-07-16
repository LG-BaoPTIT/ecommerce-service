pipeline{
    agent any

    environment{
        IMAGE_TAG = 'v1'
        DOCKERHUB_PWD = credentials('dockerhup-pwd')
    }

    tools{
        maven 'maven_3_5_0'
    }

    stages{

        stage('Build maven'){
            steps{
                script{
                    checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/LG-BaoPTIT/ecommerce-service']])
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t lgbptit/ecommerce-service:${IMAGE_TAG} .'
                }
            }
        }

        stage('Push docker image to Hub'){
            steps{
                script{
                    sh 'docker login -u lgbptit -p ${DOCKERHUB_PWD}'
                    sh 'docker push lgbptit/ecommerce-service:${IMAGE_TAG}'
                }
            }
        }
        stage('Stop and remove old container'){
            steps{
                script{
                    sh 'docker stop ecommerce-service-container || true'
                    sh 'docker rm ecommerce-service-container || true'
                }
            }
        }
        stage('Run container'){
            steps{
                script{
                    sh 'docker run -d --name ecommerce-service-container -p 8099:8099 --network myNetwork --ip 172.19.0.7 lgbptit/ecommerce-service:${IMAGE_TAG}'
                }
            }
        }
        // stage('Remove old image'){
        //     steps{
        //         script{
        //             sh 'docker rmi -f lgbptit/ecommerce-service || true'
        //         }
        //     }
        // }
    }

}
#!groovy

String GIT_VERSION

node {
  def buildEnv
  def serverImage
  def serviceName = 'miami-publish'
  def imageTag

  stage ('Checkout') {
    deleteDir()
    checkout scm
    GIT_VERSION = sh (
      script: 'git rev-parse HEAD',
      returnStdout: true
    ).trim()
  }

  def now = new Date()
  def nowString = now.format("yyyyMMddHHmmss", TimeZone.getTimeZone('UTC'))
  def uniqueId = "${GIT_VERSION}_${nowString}"
      
  stage ('Build Custom Environment') {
    buildEnv = docker.build("build_env:${uniqueId}", 'build-env')
  }

  buildEnv.inside {
    stage('build') {
      sh '''mvn clean package -DskipTests'''
    }
  }
  
  if (env.BRANCH_NAME == 'master') {
    withCredentials([string(credentialsId: 'registry-address', variable: 'registryAddress')]) {
      stage('docker build') {
        imageTag = "${serviceName}:${uniqueId}"
        docker.withRegistry("${registryAddress}") {
          serverImage = docker.build(imageTag)
          serverImage.push()
        }
      }
    }

    withCredentials([string(credentialsId: 'registry-address2', variable: 'registryAddress2')]) {
      stage('deploy') {
        docker.image('lachlanevenson/k8s-kubectl').inside {
          withCredentials(bindings: [[$class: "FileBinding", credentialsId: 'kubeconfig', variable: 'KUBE_CONFIG']]) {
            def kubectl = "kubectl --kubeconfig=\$KUBE_CONFIG"
            sh "sed 's~IMAGE_TAG_HERE~${registryAddress2}/${imageTag}~g' k8s.yml | ${kubectl} apply -f -"
          }
        }
      }
    }
  }
}

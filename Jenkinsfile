node {
  def sbtHome = tool 'sbt'

  def SBT = "${sbtHome}/bin/sbt -Dsbt.log.noformat=true"

  def branch = env.BRANCH_NAME

  echo "current branch is ${branch}"
  echo "current directory is ${pwd()}"

  // Mandatory, to maintain branch integrity
  checkout scm

  stage('Cleanup') {
    sh "${SBT} clean"
  }

  stage('Build') {
    sh "${SBT} package"
  }

  stage('Publish-Local') {
    sh "${SBT} publish-local"
  }

  stage('Archive') {
    archive 'target/**/test-dep*.jar'
  }

}
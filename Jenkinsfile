node {
  def sbtHome = tool 'sbt'

  def SBT = "${sbtHome}/bin/sbt -Dsbt.log.noformat=true"

  def branch = env.BRANCH_NAME

  echo "current branch is ${branch}"
  echo "current directory is ${pwd()}"

  // Mandatory, to maintain branch integrity
  checkout scm

  cd "./ClientService"

  stage('clean') {
    sh "${SBT} clean"
  }

  stage('build') {
    sh "${SBT} compile"
  }

  stage('test') {
    sh "${SBT} test"
  }
}
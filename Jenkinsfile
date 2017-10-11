node {
   stage('git') {
      git 'https://github.com/Guardian-Development/NewcastleUniversityDissertation.git'
   }
   stage('build-client-service') {
       sh "cd ./ClientService"
       sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean update compile test"
   }
}

node {
  def sbtHome = tool 'sbt'
  //def SBT = "${sbtHome}/bin/sbt -Dsbt.log.noformat=true -Dsbt.override.build.repos=true"
  def SBT = "${sbtHome}/bin/sbt -Dsbt.log.noformat=true"

  def branch = env.BRANCH_NAME

  echo "current branch is ${branch}"

  // Mandatory, to maintain branch integrity
  checkout scm

  sh "cd ./ClientService"

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
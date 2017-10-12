node {
  def sbtHome = tool 'sbt'

  def SBT = "${sbtHome}/bin/sbt -Dsbt.log.noformat=true"

  def branch = env.BRANCH_NAME

  echo "current branch is ${branch}"
  echo "current directory is ${pwd()}"

  // Mandatory, to maintain branch integrity
  checkout scm

  stage('clean') {
    dir ('./ClientService') { 
        sh "${SBT} clean"
    }
  }

  stage('build') {
      dir ('./ClientService') { 
        sh "${SBT} compile"
      }
  }

  stage('test') {
      dir ('./ClientService') { 
        sh "${SBT} test"
      }
  }
}
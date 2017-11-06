node 
{
  //'sbt' is the plugin configuration name configured on Jenkins Docker image configured in ./ci. 
  def sbtHome = tool 'sbt'

  def SBT = "${sbtHome}/bin/sbt -Dsbt.log.noformat=true"
  def branch = env.BRANCH_NAME

  echo "current branch is ${branch}"

  // Mandatory, to maintain branch integrity
  checkout scm

  stage('client-service')
  {
    dir ('./ClientService') 
    { 
      stage('clean') 
      {
          sh "${SBT} clean"
      }

      stage('update') 
      {
          sh "${SBT} update"
      }

      stage('compile') 
      {
        sh "${SBT} compile"
      }

      stage('test') 
      {
        sh "${SBT} test"
      }
    }
  }
}
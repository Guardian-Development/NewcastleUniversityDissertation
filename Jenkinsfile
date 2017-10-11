node {
   stage('git') {
      git 'https://github.com/Guardian-Development/NewcastleUniversityDissertation.git'
   }
   stage('build-client-service') {
       sh "cd ./ClientService"
       sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean update compile test"
   }
}
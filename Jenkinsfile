node {
   stage('git') {
      git 'https://github.com/Guardian-Development/NewcastleUniversityDissertation.git'
   }
   stage('build-client-service') {
       sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation' subdirPath: './ClientService'}/bin/sbt clean update compile test"
   }
}
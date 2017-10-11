node {
   stage('Git') {
      git 'https://github.com/Guardian-Development/NewcastleUniversityDissertation.git'
   }
   stage('Build') {
       def builds = [:]
       builds['scala'] = {
           // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.13'
           sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean update compile test"
       }
       builds['frontend'] = {
           echo 'building the frontend'
       }
     parallel builds
   }
   stage('Results') {
      junit '**/target/test-reports/*.xml'
   }
}
pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        git(url: 'https://github.com/jumkid/share-jar.git', branch: 'master')
        sh 'mvn clean compile'
        sh 'find . | sed -e "s/[^-][^\\/]*\\// |/g" -e "s/|\\([^ ]\\)/|-\\1/"'
      }
    }

  }
}

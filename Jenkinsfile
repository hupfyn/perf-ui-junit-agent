pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        git(url: 'https://github.com/hupfyn/perf-ui-junit-agent.git', branch: 'master')
      }
    }
    stage('up container') {
      steps {
        sh 'mkdir report'
        sh 'docker run -v /var/jenkins_home/workspace/run_test/reports/:/tmp/reports/ -p 8585:8585 --name perfui-reporter hupfyn/perf-reporter:onwork -d '
        tool(name: 'Docker', type: 'docker')
      }
    }
  }
}
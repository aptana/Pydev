#!groovy
@Library('pipeline-build') _

timestamps() {
	node('linux && ant && eclipse && jdk && vncserver') {
		try {
			stage('Checkout') {
				checkout scm
				gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
			}

			def studio3Repo = "file://${env.WORKSPACE}/studio3-core/dist/"
			buildPlugin('Feature Build') {
				dependencies = [
					'studio3-core': '../studio3'
				]
				builder = 'com.aptana.pydev.build'
				properties = [
					'studio3.p2.repo': studio3Repo
				]
			}

			// If not a PR, trigger downstream builds for same branch
			if (!env.BRANCH_NAME.startsWith('PR-')) {
				build job: "../studio3-rcp/${env.BRANCH_NAME}", wait: false
			}
		} catch (e) {
			// if any exception occurs, mark the build as failed
			currentBuild.result = 'FAILURE'
			throw e
		} finally {
			step([$class: 'WsCleanup', notFailBuild: true])
		}
	} // end node
} // timestamps

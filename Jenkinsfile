#!groovy
@Library('pipeline-build') _

// Keep logs/reports/etc of last 3 builds, only keep build artifacts of last build
properties([
	buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '1')),
	// specify projects to allow to copy artifacts with a comma-separated list.
	copyArtifactPermission("/aptana-studio-sync/sync-nightlies-aptana-${env.BRANCH_NAME},../studio3-rcp/${env.BRANCH_NAME}"),
])

timestamps {
	def targetBranch = 'development'
	def isPR = false

	// node('((linux && vncserver) || osx) && jdk') {
	node('linux && vncserver && jdk') {
		stage('Checkout') {
			// checkout scm
			// Hack for JENKINS-37658 - see https://support.cloudbees.com/hc/en-us/articles/226122247-How-to-Customize-Checkout-for-Pipeline-Multibranch
			checkout([
				$class: 'GitSCM',
				branches: scm.branches,
				extensions: scm.extensions + [[$class: 'CleanBeforeCheckout'], [$class: 'CloneOption', honorRefspec: true, noTags: true, reference: '', shallow: true, depth: 30, timeout: 30]],
				userRemoteConfigs: scm.userRemoteConfigs
			])
			isPR = env.BRANCH_NAME.startsWith('PR-')
			if (isPR) {
				targetBranch = env.CHANGE_TARGET
			} else {
				targetBranch = env.BRANCH_NAME
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

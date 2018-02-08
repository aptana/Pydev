#! groovy
// Keep logs/reports/etc of last 3 builds, only keep build artifacts of last build
properties([buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '1'))])

timestamps {
	def studio3RepoURL = ''
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
		}

		stage('Dependencies') {
			step([$class: 'CopyArtifact',
				filter: 'dist/',
				fingerprintArtifacts: true,
				selector: lastSuccessful(),
				projectName: "/aptana-studio/studio3/${targetBranch}",
				target: 'studio3'])
				studio3RepoURL = "file:${pwd()}/studio3/dist"
		}

		stage('Build') {
			withEnv(["PATH+MAVEN=${tool name: 'Maven 3.5.0', type: 'maven'}/bin"]) {
				withCredentials([usernamePassword(credentialsId: 'aca99bee-0f1e-4fc5-a3da-3dfd73f66432', passwordVariable: 'STOREPASS', usernameVariable: 'ALIAS')]) {
					wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
						try {
							timeout(30) {
								// TODO Get package vs verify goals running in separate stages!
								sh "mvn -Dstudio3.p2.repo.url=${studio3RepoURL} -Dmaven.test.failure.ignore=true -Djarsigner.keypass=${env.STOREPASS} -Djarsigner.storepass=${env.STOREPASS} -Djarsigner.keystore=${env.KEYSTORE} clean verify"
							}
						} finally {
							// record tests even if we failed
							junit 'tests/*/target/surefire-reports/TEST-*.xml'
						}
					} // xvnc
				} // withCredentials
			} // withEnv(maven)
			// Archive the generated p2 repo
			dir('releng/org.python.pydev.update/target') {
				// To keep backwards compatability with existing build pipeline, rename to "dist"
				sh 'mv repository dist'
				archiveArtifacts artifacts: 'dist/**/*'
				def jarName = sh(returnStdout: true, script: 'ls dist/features/com.aptana.pydev.feature_*.jar').trim()
				def version = (jarName =~ /.*?_(.+)\.jar/)[0][1]
				currentBuild.displayName = "#${version}-${currentBuild.number}"
			}
		} // stage('Build')

		// If not a PR, trigger downstream builds for same branch
		if (!isPR) {
			build job: "../studio3-rcp/${env.BRANCH_NAME}", wait: false
		}
	} // node
} // timestamps

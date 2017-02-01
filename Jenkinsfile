#!groovy

node('linux && ant && eclipse && jdk && vncserver') {
	try {
		def targetBranch = 'development'
		def minimums = []
		def isPR = false
		timestamps() {
			stage('Checkout') {
				checkout scm
				isPR = env.BRANCH_NAME.startsWith('PR-')
				if (!isPR) {
					targetBranch = env.BRANCH_NAME
				} else {
					targetBranch = env.CHANGE_TARGET
				}
			}

			// Copy over dependencies
			stage('Dependencies') {
				step([$class: 'CopyArtifact',
					filter: 'dist/',
					fingerprintArtifacts: true,
					projectName: "Studio/studio3/${targetBranch}",
					target: 'studio3-core'])
			}

			stage('Build') {
				env.PATH = "${tool name: 'Ant 1.9.2', type: 'ant'}/bin:${env.PATH}"
				writeFile file: 'override.properties', text: """scs.branch.name=${targetBranch}
workspace=${env.WORKSPACE}
deploy.dir=${env.WORKSPACE}/dist
buildDirectory=${env.WORKSPACE}
vanilla.eclipse=${env.ECLIPSE_4_4_HOME}
launcher.plugin=${env.ECLIPSE_4_4_LAUNCHER}
builder.plugin=${env.ECLIPSE_4_4_BUILDER}
configs=win32, win32, x86 & win32, win32, x86_64 & linux, gtk, x86 & linux, gtk, x86_64 & macosx, cocoa, x86 & macosx, cocoa, x86_64
studio3.p2.repo=file://${env.WORKSPACE}/studio3-core/dist/
"""
				timeout(10) {
					sh 'ant -propertyfile override.properties -f builders/com.aptana.pydev.build/build.xml build'
				}

				dir('dist') {
					def zipName = sh(returnStdout: true, script: 'ls *.zip').trim()
					def version = (zipName =~ /^.*?\-(.+)\.zip/)[0][1]
					currentBuild.displayName = "#${version}-${currentBuild.number}"
				}
				archiveArtifacts artifacts: 'dist/**/*', excludes: 'dist/*.zip'
			} // end Build stage

			// If not a PR, trigger downstream builds for same branch
			if (!env.BRANCH_NAME.startsWith('PR-')) {
				build job: "studio3-feature-${env.BRANCH_NAME}", wait: false
			}
		} // end timestamps
	} catch (e) {
		// if any exception occurs, mark the build as failed
		currentBuild.result = 'FAILURE'
		office365ConnectorSend(message: 'Build failed', status: currentBuild.result, webhookUrl: 'https://outlook.office.com/webhook/ba1960f7-fcca-4b2c-a5f3-095ff9c87b22@300f59df-78e6-436f-9b27-b64973e34f7d/JenkinsCI/5dcba6d96f54460d9264e690b26b663e/72931ee3-e99d-4daf-84d2-1427168af2d9')
		throw e
	} finally {
		step([$class: 'WsCleanup', notFailBuild: true])
	}
} // end node

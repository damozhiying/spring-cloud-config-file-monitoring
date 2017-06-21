/*
 * Copyright 2011 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.gitblit.GitBlit
import com.gitblit.Keys
import com.gitblit.models.RepositoryModel
import com.gitblit.models.UserModel
import com.gitblit.utils.JGitUtils
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.transport.ReceiveCommand
import org.eclipse.jgit.transport.ReceiveCommand.Result
import org.slf4j.Logger
import com.gitblit.utils.JGitUtils
import java.text.SimpleDateFormat
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.Config
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.transport.ReceiveCommand
import org.eclipse.jgit.transport.ReceiveCommand.Result
import org.slf4j.Logger
import groovy.json.JsonBuilder

/**
 * Sample Gitblit Post-Receive Hook: fisheye
 *
 * The Post-Receive hook is executed AFTER the pushed commits have been applied
 * to the Git repository.  This is the appropriate point to trigger an
 * integration build or to send a notification.
 * 
 * This script is only executed when pushing to *Gitblit*, not to other Git
 * tooling you may be using.
 * 
 * If this script is specified in *groovy.postReceiveScripts* of gitblit.properties
 * or web.xml then it will be executed by any repository when it receives a
 * push.  If you choose to share your script then you may have to consider
 * tailoring control-flow based on repository access restrictions.
 *
 * Scripts may also be specified per-repository in the repository settings page.
 * Shared scripts will be excluded from this list of available scripts.
 * 
 * This script is dynamically reloaded and it is executed within it's own
 * exception handler so it will not crash another script nor crash Gitblit.
 * 
 * Bound Variables:
 *  gitblit			Gitblit Server	 			com.gitblit.GitBlit
 *  repository		Gitblit Repository			com.gitblit.models.RepositoryModel
 *  receivePack		JGit Receive Pack			org.eclipse.jgit.transport.ReceivePack
 *  user			Gitblit User				com.gitblit.models.UserModel
 *  commands		JGit commands 				Collection<org.eclipse.jgit.transport.ReceiveCommand>
 *	url				Base url for Gitblit		String
 *  logger			Logs messages to Gitblit 	org.slf4j.Logger
 *  clientLogger	Logs messages to Git client	com.gitblit.utils.ClientLogger
 *
 * Accessing Gitblit Custom Fields:
 *   def myCustomField = repository.customFields.myCustomField
 *  
 */
// Indicate we have started the script
logger.info("fisheye hook triggered by ${user.username} for ${repository.name}")

// define your fisheye url here or set groovy.fisheyeServer in 
// gitblit.properties or web.xml
def fisheyeUrl = gitblit.getString('groovy.fisheyeServer', 'http://localhost:8888/monitor')

// define your fisheye API token or set groovy.fisheyeApiToken in
// gitblit.properties or web.xml
def fisheyeApiToken = gitblit.getString('groovy.fisheyeApiToken', '')

// whether to remove .git suffix from repository name
// may be defined in gitblit.properties or web.xml
def fisheyeRemoveGitSuffix = gitblit.getBoolean('groovy.fisheyeRemoveGitSuffix', false)
def summaryUrl
def commitUrl
def repoName = repository.name
def repo = repository.name
if (gitblit.getBoolean(Keys.web.mountParameters, true)) {
	repo = repo.replace('/', gitblit.getString(Keys.web.forwardSlashCharacter, '/')).replace('/', '%2F')
	summaryUrl = url + "/summary/$repo"
	commitUrl = url + "/commit/$repo/"
} else {
	summaryUrl = url + "/summary?r=$repo"
	commitUrl = url + "/commit?r=$repo&h="
}

Repository r = gitblit.getRepository(repoName)
if (fisheyeRemoveGitSuffix && repoName.toLowerCase().endsWith('.git')) repoName = repoName.substring(0, repoName.length() - 4)

// define the trigger url
def triggerUrl = "$fisheyeUrl"

// construct a simple text summary of the changes contained in the push
def branchBreak = '>---------------------------------------------------------------\n'
def commitBreak = '\n\n ----\n'
def commitCount = 0
def changes = ''
SimpleDateFormat df = new SimpleDateFormat(gitblit.getString(Keys.web.datetimestampLongFormat, 'EEEE, MMMM d, yyyy h:mm a z'))
def table = { "\n ${JGitUtils.getDisplayName(it.authorIdent)}\n ${df.format(JGitUtils.getCommitDate(it))}\n\n $it.shortMessage\n\n $commitUrl$it.id.name" }
def filelist = []
for (command in commands) {
	def ref = command.refName
	def refType = 'branch'
	if (ref.startsWith('refs/heads/')) {
		ref  = command.refName.substring('refs/heads/'.length())
	} else if (ref.startsWith('refs/tags/')) {
		ref  = command.refName.substring('refs/tags/'.length())
		refType = 'tag'
	}
	
	switch (command.type) {
		case ReceiveCommand.Type.CREATE:
			def commits = JGitUtils.getRevLog(r, command.oldId.name, command.newId.name).reverse()
			for (commit in commits) {
				def files = JGitUtils.getFilesInCommit(r, commit)
				for (file in files) {
					println "Change type: ${file.changeType}\n" +
					"File's path property: ${file.path}\n" +
					"File's name property: ${file.name}"
					filelist.add(file.name)
				}
			}
			
			break
		case ReceiveCommand.Type.UPDATE:
			def commits = JGitUtils.getRevLog(r, command.oldId.name, command.newId.name).reverse()
			for (commit in commits) {
				def files = JGitUtils.getFilesInCommit(r, commit)
				for (file in files) {
					println "Change type: ${file.changeType}\n" +
					"File's path property: ${file.path}\n" +
					"File's name property: ${file.name}"
					filelist.add(file.name)
				}
			}
			break
		case ReceiveCommand.Type.UPDATE_NONFASTFORWARD:
			def commits = JGitUtils.getRevLog(r, command.oldId.name, command.newId.name).reverse()
				for (commit in commits) {
				def files = JGitUtils.getFilesInCommit(r, commit)
				for (file in files) {
					println "Change type: ${file.changeType}\n" +
					"File's path property: ${file.path}\n" +
					"File's name property: ${file.name}"
					filelist.add(file.name)
				}
			}
			break
		case ReceiveCommand.Type.DELETE:
			// deleted branch/tag
			def commits = JGitUtils.getRevLog(r, command.oldId.name, command.newId.name).reverse()
			for (commit in commits) {
				def files = JGitUtils.getFilesInCommit(r, commit)
				for (file in files) {
					println "Change type: ${file.changeType}\n" +
					"File's path property: ${file.path}\n" +
					"File's name property: ${file.name}"
					filelist.add(file.name)
				}
			}
			break
		default:
			break
	}
}
// close the repository reference
r.close()
logger.info('changes >>' + changes);
// trigger the build
def _url = new URL(triggerUrl)
def _con = _url.openConnection()

// set up connection
_con.setRequestMethod("POST")
_con.setRequestProperty("X-Api-Key", fisheyeApiToken)
_con.setRequestProperty("User-Agent", "Gitblit")
_con.setRequestProperty("Content-Type","application/json")
def pathstring="\"path\":"

def jsonText = '''
{"uuid":"1",
	"location":"badalona",
	"name":"david",
	"content":"badalona",
	"owner":"badalona",
	"path": ["*"]
}'''
def data = [
  path: filelist.collect {it}
]

def json = new JsonBuilder(data)
logger.info("json list >>" + json)
byte[] postDataBytes = json.toString().getBytes("UTF-8");
_con.setDoOutput(true)
_con.getOutputStream().write(postDataBytes);
// send post request


logger.info("fisheye response code: ${_con.responseCode}")

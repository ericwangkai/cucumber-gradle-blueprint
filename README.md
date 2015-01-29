## Cucumber-Gradle-Blueprint

Blueprint project to demonstrate the source code based generation of Jenkins jobs and views via a Gradle task. The jobs are thought to trigger the (repeated) execution of Cucumber features and monitor the results. The job names are generated out of the feature file names, the execution environment (e.g. prod, uat) and cucumber tags (e.g. smoke) contained in the feature files. For each env and tag combination (e.g. 'smoke.uat' or 'untagged.prod') a view and a *multijob* is created. A *multijob* is a parent (or upstream) job which comprises all child (or downstream) jobs which correspond to the tag/env - specification.


### Setup

  * download, install and start [Jenkins](http://jenkins-ci.org/) (example assumes Jenkins at http://localhost:8080, as configured [here](/jenkins.gradle#L16))

  * install Jenkins plugins
	  * [Git](https://wiki.jenkins-ci.org/display/JENKINS/Git+Plugin)
	  * [Gradle](https://wiki.jenkins-ci.org/display/JENKINS/Gradle+Plugin)
	  * [Cucumber Test Result](https://wiki.jenkins-ci.org/display/JENKINS/Cucumber+Test+Result+Plugin)
	  * [Cucumber Performance Report](https://wiki.jenkins-ci.org/display/JENKINS/Cucumber+Performance+Reports+Plugin)
	  * [Naginator](https://wiki.jenkins-ci.org/display/JENKINS/Naginator+Plugin)
	  * [Copy Artifacts](https://wiki.jenkins-ci.org/display/JENKINS/Copy+Artifact+Plugin)
	  * [Workspace Cleanup](https://wiki.jenkins-ci.org/display/JENKINS/Workspace+Cleanup+Plugin)
	  * [Multijob](https://wiki.jenkins-ci.org/display/JENKINS/Multijob+Plugin)
  * setup the user permissions: non-logged users have read-only permissions, the jenkins user is allowed to run scripts as well as to create, update and delete jobs and views.
 ![jenkins ui multijob](/images/permissions.png)

  * configure the number of executors in global Jenkins settings


### Usage

create and/or update jobs/views on your Jenkins server

    ./gradlew updateJenkinsItems -Pjenkins_user=jenkins -Pjenkins_pass=<password>

remove all items on server

    ./gradlew deleteJenkinsItems -Pjenkins_user=jenkins -Pjenkins_pass=<password>

check which project job/view configuration differs from the server version

    ./gradlew validateJenkinsItems -Pjenkins_user=jenkins -Pjenkins_pass=<password>

to run all cucumber features

    ./gradlew cucumber

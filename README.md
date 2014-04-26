Installation of iTeach using Puppet
===================================

This document describes the installation and the updates of iTeach using Puppet. We will assume that
Puppet is used in a standalone mode but this mechanism could be extended to a master/agent mode if needed.

### Set-up of the server

#### Prerequisites

The basic set-up to operate on the server is:

* Ubuntu 14.04
* Having a `install` user which is part of the `sudoers`
* Having `git` installed: `sudo apt-get install git`
* Having `puppet-common` installed: `sudo apt-get install puppet-common`

Additionally, the `LC_ALL` environment variable must be set, typically in the `.bash_profile`:

	export LC_ALL=en_GB.UTF-8

#### Puppet configuration

Connect the Puppet configuration directory to Git:

As `root`, `sudo -s`:

	cd /etc/puppet
	rm -rf *
	git clone --branch puppet https://github.com/nemerosa/iteach.git .

This installs the Puppet files of iTeach in `/etc/puppet`.

### Manual installation

This chapter describes the installation of (or upgrade to) a version `x.y.z` of iTeach.

As `root`, `sudo -s`, update the version of the Puppet scripts to the correct version:

	cd /etc/puppet
	git pull
	git checkout x.y.z

Then, just run Puppet:

	sudo puppet apply -v /etc/puppet/manifests/site.pp

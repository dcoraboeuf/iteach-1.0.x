class iteach {
	
    file { 'iteach-working-directory':
        path => '/etc/iteach',
        ensure => directory,
    }

    include oraclejdk8
    oraclejdk8::install{oraclejdk8-local:}

    package { 'tomcat7':
        name => 'tomcat7',
        ensure => installed,
	}
	
	service { 'tomcat7':
		name => 'tomcat7',
		ensure => running,
		enable => true,
		hasrestart => true,
		hasstatus => true,
	}

	Package['tomcat7'] ~> Service['tomcat7']
	Package['oracle-java8-installer'] ~> Service['tomcat7']

}

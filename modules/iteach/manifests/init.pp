class iteach {
    file { 'iteach-working-directory':
        path => '/etc/iteach',
        ensure => directory,
    }

    package { 'tomcat7':
        name => 'tomcat7',
        ensure => installed,
    }
}

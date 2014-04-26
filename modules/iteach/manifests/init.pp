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
}

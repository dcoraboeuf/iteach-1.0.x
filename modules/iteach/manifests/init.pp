class iteach {
    file { 'iteach-working-directory':
        path => '/etc/iteach',
        ensure => directory,
    }
}

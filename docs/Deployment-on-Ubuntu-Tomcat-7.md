On an Ubuntu box, using a `sudoer` user (named `install` below).

### JDK

Install the latest version of the JDK 7:

```bash
sudo apt-get update
sudo apt-get install openjdk-7-jdk
```

Check the installed version by:

```bash
java -version
```

### Tomcat

Install the Tomcat 7 package by:

```bash
sudo apt-get install tomcat7
```

Tomcat is now installed and runs on port 8080. Connect on http://host:8080 to check this.

### Configuring Tomcat for iteach

Stop Tomcat:

```bash
sudo service tomcat7 stop
```

#### Additional Tomcat libraries

In `/usr/share/tomcat7/lib`, put the following libraries:

* H2 database driver - `h2-1.3.174.jar` - downloaded from the Maven Central (`com.h2database`, `h2`)
* Java mail libraries - `mail.jar` - downloaded from http://www.oracle.com/technetwork/java/index-138643.html

Upload then in the `/home/install` directory and:

```bash
sudo chown root:root /home/install/*.jar
sudo mv /home/install/*.jar /usr/share/tomcat7/lib
```

#### Tomcat environment variables

Edit the `JAVA_OPTS` definition in `/etc/default/tomcat`:

```bash
JAVA_OPTS="-Diteach.url=http://yourhost -Dspring.profiles.active=prod -Djava.awt.headless=true -Xmx128m -XX:+UseConcMarkSweepGC"
```

Note the use of:
* `spring.profiles.active=prod` to indicate the application profile
* `iteach.url` to indicate to iTeach which URL to use (emails, base HREF)

#### Remove the ROOT application

Remove the root application:

```bash
sudo rm -rf /var/lib/tomcat7/webapps/ROOT
```

#### iteach directory

```bash
sudo mkdir -p /opt/iteach/db
sudo chown -R tomcat7:tomcat7 /opt/iteach
```

#### Deploying iteach configuration

Put the default configuration file in `/var/lib/tomcat7/conf/Catalina/localhost` as `ROOT.xml`.

#### Deploying the iteach application

Copy the `iteach` WAR into `/var/lib/tomcat7/webapps` as `ROOT.war`.

Restart Tomcat:

```bash
sudo service tomcat7 start
```

### nginx as a SSL proxy

Install `nginx`:

```bash
sudo apt-get install nginx
```

Remove default configuration:

```bash
cd /etc/nginx/sites-available
sudo rm default ../sites-enabled/default
```

For the SSL certificates, create a new directory:

```
sudo mkdir /etc/nginx/ssl
```

Create the server key with a pass phrase:

```
cd /etc/nginx/ssl
sudo openssl genrsa -des3 -out server.key 1024
```

Create the server certificate:

```
sudo openssl req -new -key server.key -out server.csr
```

Remove the passphrase:

```
sudo cp server.key server.key.org
sudo openssl rsa -in server.key.org -out server.key
```

Sign the certificate:

```
sudo openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
```

Create the `/etc/nginx/sites-available/iteach` file and set the following content:

```
upstream app_server {
    server 127.0.0.1:8080 fail_timeout=0;
}

server {
    listen 443;
    listen [::]:443 default ipv6only=on;
    server_name iteach.yourcompany.com;

    ssl on;
    ssl_certificate /etc/nginx/ssl/server.crt;
    ssl_certificate_key /etc/nginx/ssl/server.key;

    location / {
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-Proto https;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_redirect http:// https://;

        add_header Pragma "no-cache";

        proxy_pass http://app_server;
    }
}
```

Link your configuration from sites-available to sites-enabled:

```bash
sudo ln -s /etc/nginx/sites-available/iteach /etc/nginx/sites-enabled/
```

Configure Tomcat in proxy mode by editing the `<Connector>` node in `/var/lib/tomcat7/conf/server.xml`:

```xml
<Connector port="8080" protocol="HTTP/1.1"
    connectionTimeout="20000"
    URIEncoding="UTF-8"
    redirectPort="8443"
    scheme="https"
    proxyPort="443" />
```

Note the addition of `scheme` and `proxyPort`.

Restart Nginx:

```bash
sudo service nginx restart
```

### Hardening

Enabling the firewall:

```bash
sudo ufw enable
```

Opening the SSH port (if you still want to access the server later...):

```bash
sudo ufw allow ssh
```

Opening the SSL port:

```bash
sudo ufw allow 443/tcp
```

### Resources

* https://www.digitalocean.com/community/articles/how-to-install-apache-tomcat-on-ubuntu-12-04
* https://wiki.jenkins-ci.org/display/JENKINS/Installing+Jenkins+on+Ubuntu
* http://serverfault.com/questions/278555/jenkins-use-it-with-ssl-https
* https://help.ubuntu.com/community/UFW
* https://www.digitalocean.com/community/articles/how-to-create-a-ssl-certificate-on-nginx-for-ubuntu-12-04/
* http://tomcat.apache.org/tomcat-7.0-doc/proxy-howto.html
* http://webapp.org.ua/sysadmin/setting-up-nginx-ssl-reverse-proxy-for-tomcat/ (the `scheme` trick)
Developer Setup and Build instructions
NOTE: This project does not include an installer or any sort of tool to make it easy for
people who are not java software developers to install and use.  The following instructions
assume the user is an experienced java software developer with familiarity with J2EE, MySQL and
web development in general.

Prerequisites
* Mysql 5.x, installed with innodb as the default engine and UTF8 as the default encoding
* JDK 5.x
* Apache Ant 7.x
* Apache Ivy 2.0
* Apache Tomcat 5.5, installed on port 80

Optional
* MyEclipse IDE 
* MyEclipse subversion support

1. Install Prerequisites

2. Create databases
mysql> create database tsm_dev;
mysql> create database tsm_test;
mysql> grant all privileges on tsm_dev.* to 'tsm_dev_user'@'localhost' identified by 'tsm_dev_user';
mysql> grant all privileges on tsm_test.* to 'tsm_test_user'@'localhost' identified by 'tsm_test_user';

3. create the shema
$ ant create-db-dev
$ ant create-db-test

4. Install your google maps key 
* create an alias for your local machine or use one in the map.properties file.  For instance, you can 
edit your /etc/hosts file like so:
127.0.0.1       localhost www.map4d.com
* when you open the application from your browser you will use this new alias: http://www.map4d.com:8080 

5. deploy
* create a file called user.build.properties and edit it to point to your local mysql and tomcat instance
* deploy
$ ant deploy-test

6. start
* start tomcat
* errors are logged to catalina.out
* navigate to your webapp http://www.map4d.com:8080 

7. Login
* the create-db-test script creates an admin user called "frank" with the password "cat"
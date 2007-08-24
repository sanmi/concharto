This is a skeleton application that I've mashed up from spring and hibernate.
For an practicing java developer, I think it gets pretty close to the developer 
productivity of Ruby on Rails.  

LICENSE.  This software is licensed under the Apache License

General requirements were:
1. Hosting costs = Support high traffic web sites with a low number of 
   CPU's needed per million unique visitors per month.
2. Developer productivity = approach the ease of Ruby on Rails.
3. Complexity = The less disparate parts, the better
4. Learning curve = Knowing java, jsp and web apps should be good enough to 
   get moving quickly.
5. Maximize third party tools and components = maximize the available choices 
   of UI widgets, AJAX libaries, etc.

Skeleton application mashed up from:
* Spring 2.0.6 MVC-convention sample webapp
* Hibernate 3.0 annotations example application
* jsp tag examle pagetag: 
  http://fforw.de/post/Creating_JSP_Layouts_with_Page_Tags/anonreply.save

Also demonstrates:
* MyEclipse/eclipse hot deploy capabilities
* Maven and ant
* ant script for generation of the schema and installation on the datbase server
* auto generation of ant script for dao "integration" tests

Tools I used:
* MyEclipse
* Jdk 1.5.0.10
* Ant 1.6.5
* maven 2.0.7
* MySQL 5.0.27
* Apache Tomcat 5.5
* source code packages for the following (so you can have javadocs and step into 
  the source while debugging)
** spring 2.0.6
** hibernate 3.0.2

Installation and running:

1. Create the database
* Generate the schema and deploy
** create a database in MySQL called "tsm_test" and a user called "tsm_test_user"
** edit build.properties with username and password
** edit src/main/webapp/WEB-INF/tsm-datasource.xml with database username and password

2. BUILD
* Update the maven repository with Sun Jars
Due to sun licensing, maven can't host jta jars.  Instead do this:
{code}
 mvn install:install-file -Dfile=lib/local/jta.jar -DgroupId=javax.transaction -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar
{code}

* from the project directory
* type "mvn war:exploded"

3. Generate the database schema
* type ant create-db-test

4. Deploy the webapp
* you can do this from MyEclipse or by copying the target/tsm to the tomcat webapps directory.

5. Run
* start tomcat 
* open the main page: http://<hostname>/tsm/ (e.g. http://localhost:8080/tsm/)



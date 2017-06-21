# spring-cloud-config-file-monitoring app
## Base Code
Base code is taken from:  https://github.com/spring-guides/gs-centralized-configuration.git. This example is based on a manual /refresh trigger from the client after any properties file has been updated; it is not configured for automatic properties refreshing (automatic server -> client notifications not happening).

## Project description
This is a Spring Cloud Config example application which monitors for local folder configuration files updates and autmoatically notifies clients about these changes.

The communication between Server and Clients is through amqp protocol, sending messages to a shared RabbitMQ server.

The goal of this example is to provide a working example of Spring Cloud Config for automatic refreshing of application configuration files on update of these files in the server.

For the Server app, this example is using the following Spring packages:
1. spring-cloud-config-server
2. spring-cloud-starter-monitor
4. spring-cloud-starter-bus-amqp

For the Client app, this example is using the following Spring packages:
1. spring-cloud-starter-config
2. spring-cloud-starter-actuator
3. spring-cloud-starter-web
4. spring-cloud-starter-bus-amqp

## Maven (or Gradle?)
The application is ready to be build and run from the provided **maven pom files**. 
Gradle files are not maintained for now.

## Run the app
2 different usage demonstrations are provided: *Local folder for config file* and *GitBlit webhook*

### Local folder config files
To run the app to monitor a local folder configuration files, please follow the following steps:

1. Since we are using spring-cloud-starter-bus-amqp dependency, this means that we need RabbitMQ Server to be running. You can download and install the version you prefer from https://www.rabbitmq.com/download.html.
2. The local folder to the Server where the configuration files are located is configured in the  bootstrap.properties of the Server through the following property, which must be modified according to your local configuration:
   * spring.cloud.config.server.native.searchLocations=file:///${USERPROFILE}/Desktop/config
   
### GitBlit webhook

For this demonstration I have locally installed a GitBlit server, but of course this can be configured in a remote Git server (either it is GitHub, GitLab, etc) if you provide the corresponding webhook.

1. In the *complete/configuration-service* **gitblit** folder you can find a Groovy GitBlit webhook, named *notify-commit.groovy*. Copy and paste this Groovy script under [GitBlit-config-folder]/data/groovy folder and configure this hook as active for a GitBlit repository and event (Repository > Edit > receive > post-receive scripts)
2. To configure the server to listen to a Git repository folder, just uncomment in the *bootstrap.properties* of the Server app the property **spring.cloud.config.server.git.uri** and point it to your required folder.
3. In the same *bootstrap.properties* comment out the lines containing:  
  3.1. spring.profiles.active=native  
  3.2. spring.cloud.config.server.native.searchLocations
 
## Stones found in the way and solved
1. There is a mandatory Dependency Management configuration, which in the base code was set as follows:
  *`<dependencyManagement>
       <dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Camden.SR5</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>`*  
  I had to switch this dependency to the **Dalston.RELEASE** to make the example work. With the **Camden.SR5** dependency the Client wasn't being aware of the configuration file changes. So the dependency now reads as follows:  
  *`<dependencyManagement>
       <dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Dalston.RELEASE</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>`*

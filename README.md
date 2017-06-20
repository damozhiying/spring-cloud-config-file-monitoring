# spring-cloud-config-file-monitoring app
## Base Code
Base code is taken from:  https://github.com/spring-guides/gs-centralized-configuration.git

## Project description
This is a Spring Cloud Config example application which monitors for local folder configuration files updates and notifies clients about these changes.

The goal of this example is to provide a working example of Spring Cloud Config for automatic refreshing of application configuration files.

For the Server app, this example is using the following Spring packages:
1. spring-cloud-config-server
2. spring-cloud-starter-monitor
4. spring-cloud-starter-bus-amqp

For the Client app, this example is using the following Spring packages:
1. spring-cloud-starter-config
2. spring-cloud-starter-actuator
3. spring-cloud-starter-web
4. spring-cloud-starter-bus-amqp

## Maven (and Gradle?)
The application is ready to be build and run from the provided **maven pom files**. 
Gradle files are not maintained for now.

## Run the app

To run the app, please follow the following steps:

1. Since we are using spring-cloud-starter-bus-amqp dependency, this means that we need RabbitMQ Server to be running. You can download and install the version you prefer from https://www.rabbitmq.com/download.html.
2. The local folder to the Server where the configuration files are located are configured in the  bootstrap.properties of the Server through the following property, which must be modified according to your local configuration:
   * spring.cloud.config.server.native.searchLocations=file:///${USERPROFILE}/Desktop/config
 
## Issues found and solved
1. There is a mandatory Dependecy Management configuration, which in the base code was set as follows:
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
  I had to switch this dependency to the Dalston.RELEASE to make the example work. With the Camden.SR5 dependency the Client wasn't being aware of the configuration file changes for some reason. So the dependency now reads as follows:
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

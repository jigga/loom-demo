# Getting Started

## Building Loom JDK

### MacOS

Just run ```bin/rebuild-mac.script path/to/an/existing/dir/where/to/clone/loom/sources```

## Running examples with Gradle

### Gradle JVM

You may need to update the value of the ```org.gradle.java.home``` property in the [gradle.properties](gradle.properties) file to point to your local build of the Loom JDK.

## Running the VT-Friendly Tomcat demo

### Building Tomcat

To run the VT-Friendly Tomcat demo, you'll have to build [my Tomcat fork](https://github.com/jigga/tomcat/tree/fibers). To build it first run the deploy, and then the embed Ant targets. Next, install the generated ```tomcat-embed-*``` files to your local maven repo using the [install-files.sh](https://github.com/jigga/tomcat/blob/fibers/install-files.sh) script available in my fork.

### HTTP BIN

Before running the Spring Boot demo app, make sure you have the [httpbin](https://httpbin.org/) docker image running locally.

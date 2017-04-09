# Goal #

Tool for HTTP Operations  and JMS Publish.

Built to assist in developing applications that use the HTTP protocol, such as Web services, REST services, etc.

The tool allows you to:

* Make HTTP methods: POST, GET, PUT, DELETE, HEAD
* Post messages in JMS queue
* Save the settings used
* Logging of actions performed
* Perform queries on Web Services
* And add properties to be sent in the header of the HTTP protocol.
* The tool has only English as the language available.

# Build #

We are using ant for building the program.
So goes to impl directory and rund command: ant

Ant will genereate the following files:

* posttool_bin.zip : Binaries with the program.
* posttool_src.zip : The program's source code.

# Installation #

## Prerequisites ##
Java JRE 1.7
Unzip the file "posttool_bin.zip" in a subfolder of the system. To run the program on Windows run the file "start.bat". In Linux run "start.sh"
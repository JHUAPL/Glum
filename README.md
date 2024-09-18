# GLUM: Great Library of User Modules


## Description
GLUM is a Java library that provides a collection of generic components, modules, and utilities which aid in development of Java software applications.

It provides the following high level capabilities:

- various components and utilities to simplify the development (creation, layout, organization, setup) of a Swing based user interface (UI)

- a data model focused framework for the display, editing, filtering, and searching of tabular data

- capability to monitor, query, or alter a “task” (mechanism to allow an end user to get information, track progress, and abort a process)

- serialization mechanism for saving and loading of application state

- capability to configure the output of data values (numeric, textual, date/time, etc)

- framework to allow retrieval (start, pause, resume) and management of data (local and/or remote) resources


## Usage
The latest release of GLUM is 2.0.0 and is distributed as a binary jar (glum-2.0.0.jar) and the corresponding source jar (glum-2.0.0-src.jar). These will need to be added to your class path.

It is intended that support via Maven mechanism will be provided in a future release.


## Dependencies
The GLUM library has the following (linking) dependencies utilized while developing the GLUM library:

- Java 17
- Guava: 18.0
- MigLayout: 3.7.2
- DockingFrames: 1.1.3

In addition, to compile GLUM the following software packages are needed:

- JDK 17+
- Python 3.6+
- Apache Ant 1.10.8+

Note the following:

- In theory GLUM should work with later versions of the above listed software, but these were the ones utilized during the primary development phase.
- The DockingFrames dependency is only necessary if gui docking capabilities are desired.

## Building GLUM
To build GLUM from the console, run the following command:

&nbsp;&nbsp;&nbsp;&nbsp;./tools/buildRelease

Yet Another Logcat Dumper 

- Description -
That's a cross platform GUI based android logcat filter system. You can define more than one rule per filter. 

- Usage -
You can find usage instructions on the project **[wiki page](https://github.com/0ffffffffh/jyald/wiki)** 

- Development IDE -
Eclipse Helios, Indigo

- Dependencies -
Eclipse Standart Widget Toolkit (SWT) 3.7.0

This project uses Ant Build system and the project contains ant build file for windows, linux and mac os x

- Compiling -
First, you must install apache ant build if necessary. If you already have, you can pass ant installation step.

Installing Apache Ant.

1) Go to http://ant.apache.org

2) Click "Binary Distributions" from left section.

3) Download ant binary package zip or tar

4) Go to ant setup page for detailed installation instructions. (http://ant.apache.org/manual/index.html)


When you are ready, open terminal/console and locate current path to jyald project root.

Just type "ant" and press enter. The ant builder automatically detects the build.xml and starts the build progress. This usage detects automatically your running platform information. If you want to platform specific build, you should use like following command.

ant [PLATFORM_IDENTIFIER] 

PLATFORM_IDENTIFIER can be following values:

* windows_x86 - for 32 Bit Windows
* windows_x64 - for 64 Bit Windows
* linux_x86 - for 32 Bit Linux
* linux_x64 - for 64 Bit Linux
* macosx_x86 - for 32 Bit Mac OSX
* macosx_x64 - for 64 Bit Mac OSX


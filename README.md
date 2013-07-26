Nodemapper
==========

Nodemapper is a 2D tree mapping application. It's a powerful tool for creating trees using a GUI software.

License
-------

Copyright (c) 2013 QuarterCode <http://www.quartercode.com/>

Nodemapper may be used under the terms of either the GNU General Public License (GPL). See the LICENSE.md file for details.

Compilation
-----------

We use maven to handle our dependencies and build. To compile Nodemapper, follow these steps:

* Install [Maven 3](http://maven.apache.org/download.html).
* Check out this repository (clone or download).
* Navigate to the project folder of this repository which contains a `pom.xml` and run:

        $ mvn clean install


Run
---

If you downloaded a binaries package or built your own one, you have several options to run Nodemapper:

* You can try to execute nodemapper-<version>.jar (e.g. with a double-click).
* If that doesn't work, open a command prompt, navigate to the binaries folder and run:

        $ java -jar nodemapper-<version>.jar

You need the Java JRE for every of those methods. If you haven't done yet, download it [here](www.java.com/download).

Usage
-----

If you start a new tree, you will only have one node with the content "Root". You can double-click on the text to edit it to something else.

You can add a new link to another node by clicking on the border of a node and dragging the line to the other node. If there's no node, the program will create a new one. If you link to nodes together a second time, the link will be removed.

Use trees with your software
----------------------------

If you want to use created trees in your own software, you can copy the source files from the package "com.quartercode.nodemapper.tree" and use them as an API.
For loading/saving, you need the packages "com.quartercode.nodemapper.ser" and "com.quartercode.nodemapper.ser.types".

The only requirement for using this classes is a reference to this original repository.

Nodemapper
==========

![Nodemapper Screenshot](http://quartercode.com/w/images/4/4e/Nodemapper_Screenshot.png)

Nodemapper is a 2D tree mapping application. It's a powerful tool for creating trees using a GUI software.
More information can be found on the [wiki page](http://quartercode.com/wiki/Nodemapper).

License
-------

Copyright (c) 2013 QuarterCode <http://quartercode.com/>

Nodemapper may be used under the terms of the GNU General Public License (GPL) v3.0. See the LICENSE.md file or https://www.gnu.org/licenses/gpl-3.0.txt for details.

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

* You can try to execute `nodemapper-<version>.jar` (e.g. with a double-click).
* If that doesn't work, open a command prompt, navigate to the binaries folder and run:

        $ java -jar nodemapper-<version>.jar

You need the Java JRE for each of those methods. If you haven't done yet, download it [here](www.java.com/download).

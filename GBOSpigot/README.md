
# GBO Spigot

Custom 1.8.8 WindSpigot fork.


## Installation

1 - Use build tools with flag `--rev 1.8.8` to generate all folders (e.g. `java -jar BuildTools.jar --rev 1.8.8`).

2 - Delete all files & folders apart from the `Spigot` folder in the same folder as the `BuildTools.jar`.

3 - Rename `Spigot` to `GBOSpigot`.

4 - Delete `GBOSpigot/Spigot-Server` & `GBOSpigot/Spigot-API`.

5 - Move all of the files into the `GBOSpigot` folder and replace the `pom.xml` file.

6 - Build using `mvn clean install` in the `GBOSpigot` (parent) folder.

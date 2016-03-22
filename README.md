ASGbreezeGui
------------

ASGbreezeGui provides a graphical representation of a Breeze netlist with support for hierarchical designs.

### Installation ###

No installation or changes in environment variables needed. To run it you will need a Java runtime environment (JRE) v1.7 (or later).

### Usage ###

To see a list of supported command line arguments execute

    bin/ASGbreezeGui

To show a graphical representation of a Breeze netlist on screen execute

    bin/ASGbreezeGui -mode gui /path/to/breezefile.breeze

* Shape semantics
  * Normal outline: Handshake components (Labelled with the component type and the component id)
  * Bold outline: Ports to the environment (Labelled with the name and the direction)
  * Dashed outline: Sub components (Labelled with the name)
    * Double click the component to open its graph in a new window
* Edge semantics
  * Caption: channel id (and, if present, the data width of the channel in parenthesis)
  * Arrow: If present shows that this channel is a pull or push channel depending on its direction
* The routing is
  * Primary: Control flow from top to down
  * Secondary: Data flow from left to right (or vice versa - depending on if its a push or pull channel)
  * You can adjust it by dragging the components
    * You can save this to png and svg by pressing *s* on your keyboard (File name is always snap_DATE)
* You can zoom with the mouse wheel

To export a graph to png directly execute

    bin/ASGbreezeGui -mode png -out outfile.png /path/to/breezefile.breeze

To export a graph to svg directly execute

    bin/ASGbreezeGui -mode svg -out outfile.svg /path/to/breezefile.breeze

### Build instructions ###

To build ASGbreezeGui, Apache Maven v3 (or later) and the Java Development Kit (JDK) v1.7 (or later) are required.

1. Build [ASGcommon](https://github.com/hpiasg/asgcommon)
2. Execute `mvn clean install -DskipTests`
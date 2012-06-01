WhenChanged
===========

Run a command **when** a file **changes** in one of your selected directories.
This might be an opportunity to run linters or unittests.

Running
-------

Short way of running WhenChanged:

    java -jar whenchanged.jar

Running WhenChanged with a config file that isn't named whenchanged.properties:

    java -jar whenchanged.jar my.properties

Setting the logging config file:

    java -Djava.util.logging.config.file=logging.properties -jar whenchanged.jar

Configuration
-------------

A file with the name whenchanged.properties must be present in the same folder as the whenchanged.jar file.
This is an example.

    delay=2000
    command.dir=C\:/Projects/test/java/whenchanged
    command.size=2
    command.0=C\:/Projects/test/java/whenchanged/test.bat
    command.1=ok
    folders.size=2
    folders.0=C\:/Projects/test/java/whenchanged/src
    folders.1=C\:/Projects/test/java/whenchanged/test

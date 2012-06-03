WhenChanged
===========

Run a command **when** a file **changes** in one of your selected directories.
This might be an opportunity to run linters or unittests.

Running
-------

Short way of running WhenChanged:

    java -jar whenchanged.jar

Running WhenChanged with a config file that isn't named `whenchanged.properties`:

    java -jar whenchanged.jar my.properties

Setting the logging config file:

    java -Djava.util.logging.config.file=logging.properties -jar whenchanged.jar

Configuration
-------------

Unless youspecify the config file a file with the name `whenchanged.properties` should be present in the same folder as the whenchanged.jar file.
This is an example.

    delay=2000
    command.dir=C\:/Projects/test/java/whenchanged
    command.size=2
    command.0=C\:/Projects/test/java/whenchanged/test.bat
    command.1=ok
    folders.size=2
    folders.0=C\:/Projects/test/java/whenchanged/src
    folders.1=C\:/Projects/test/java/whenchanged/test

Warning
-------

Running batch files that contain a `pause` statement will block WhenChanged from running. I suggest creating a wrapper batch file to echo a newline into them like in this example below.

    @ECHO OFF
    ECHO. | CALL %~dp0.\WillRunPause.bat

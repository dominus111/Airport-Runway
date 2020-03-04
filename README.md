# RunwayRedeclaration2020

## Build

`./gradlew build` will build the project

`./gradlew jar` will produce a fatjar containing javafx dependencies, including native libraries for linux/mac/windows
The result of this can be found in build/libs/RunwayRedeclarationTeam19.jar

`./gradle jlink` will create a custom runtime optimised for the local system.

Sadly, the project cannot currently support jlink and fatjars.
This is because requires the project to be a module, which implicitly includes the javafx dependencies.  
However in order to be able to make a cross platform, we must explicitly include the dependencies.
This causes an error, as it cannot have two versions of a library bundled.  
It may be possible to have both possible, which warrants further investigation.  
Making the project use jlink would be the preferable approach, 
but I have been informed that we are required to submit a jar, meaning that we must take the fatjar approach.
Therefore as of the time of writing, only building a jar works (which is still perfectly fine).

## Running

Java runtime 55.0 (java 11) or higher is required to run the software

The project can be run with `./gradlew run`

The fatjar can be run with `java -jar build/libs/RunwayRedeclarationTeam19.jar`

The jlink runtime can be run with `build/image/bin/runway`

## Tests

The test suite can be run with `./gradlew test`
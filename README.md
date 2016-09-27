## 1. What is Menthor API?

Menthor API is our yet-to-be infrastructure of the Menthor Editor 2.0. Its goal is to give you access to all Menthor Editor's capabilities dynamically via code. So far that includes OntoUML modeling with the addition of events, powertypes and qualities, syntactical checking, content importation from the EA tool and support for both UML and OLED. On going work includes codification to OWL/RDF and the Alloy generation.

## 2. License

Distributed under MIT license.

## 6. Source Code

In order to run the API with the source code, you'll need the following tools:
* Java 7 or superior
* IntelliJ with GROOVY installed
* Git Client application (GitHub, Tortoise, Source Tree, etc)

### 6.1 Additional Information...

Visit our wiki pages [here](https://github.com/tgoprince/menthor-api/wiki) to learn how to use the API.

IntelliJ:
* IntelliJ -> Open -> /menthor-api -> OK
* BootStarter.groovy -> Right Click -> Run
* Access it at http://localhost:8080/

JAR Generation:
* On terminal, run task "gradle fatJar" at a particular module's folder

Tortoise Git:
* Learn [here](http://joelabrahamsson.com/remote-branches-with-tortoisegit/) how to create your own local branch of this project.




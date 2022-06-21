# ingsw2022-AM28

PROVA FINALE (INGEGNERIA DEL SOFTWARE) Group members:

- Lorenzo Biasiolo (lorenzo.biasiolo@mail.polimi.it)
- Isaia Belardinelli (isaia.belardinelli@mail.polimi.it)
- Leonardo Brusini (leonardo.brusini@mail.polimi.it)

--------------------------------------------
Eriantys is the final test of "Software Engineering", course of "Computer Science Engineering" held at Politecnico di Milano (2021/2022).

Teacher Alessandro Margara

What has been implemented:
Complete rules + socket + CLI + GUI + 2 FA
- FA 1: 12 Character Cards
- FA 2: Resilience to client disconnections

## Project specification
------------------------------------------------------------------------
The project consists of a Java version of the board game Eriantys, made by Cranio creation.

You can find the full game here: https://www.craniocreations.it/prodotto/eriantys/.

The final version includes:

initial UML diagram;
final UML diagram, generated from the code by automated tools;
working game implementation;
source code of the implementation;
source code of unity tests.

## Implemented Functionalities
------------------------------------------------------------------------
| Functionality | Status |
|:-----------------------|:------------------------------------:|
| Basic rules | 🟢 |
| Complete rules | 🟢 |
| Socket | 🟢 |
| CLI | 🟢 |
| GUI | 🟢 |
| 12 Character cards | 🟢 |
| Resilience to disconnections | 🟡 |

#### Legend
🔴 Not Implemented &nbsp;&nbsp;&nbsp;&nbsp;🟡 Implementing&nbsp;&nbsp;&nbsp;&nbsp;🟢 Implemented

## Tests
-----------------------------------------------------
All tests in model and controller has a classes' coverage at 100%.

Coverage criteria: code lines.

Package	Tested Class	Coverage

Controller | Game Manager   | 87% Methods, 91 % lines covered

Controller | Turn Manager   | 90% Methods, 95 % lines covered

Controller |EndOfGameChecker| 100% Methods, 78 % lines covered

Model      | Dash Board     | 100% Methods, 100 % lines covered

Model      | Board          | 100% Methods, 100 % lines covered


test coverage as reported by the IntelliJ tool
83% classes, 73% line covered

# Requirements
-----------------------------------------------------
## Windows
Download and install JAVA DEVELOPER KIT - https://www.oracle.com/java/technologies/downloads/#jdk18-windows
Then set JAVA_HOME - <jdk folder path> and Path - <jdk folder path>\bin environment variables.

## MacOs
Download and install JDK FX - https://www.azul.com/downloads/?version=java-18-sts&os=macos&architecture=arm-64-bit&package=jdk-fx

# Launch server and clients using JAR file:
-----------------------------------------------------
## CLI configuration
1. Download Windows Terminal (unlike the classic cmd, it supports all necessary unicode characters)
   https://apps.microsoft.com/store/detail/windows-terminal/9N0DX20HK701
2. From WINDOWS 10/11, go to SETTINGS -> Date / time and language -> LANGUAGE -> Settings
   language administration -> Administration options -> Change system locale.
   Add the check to "use UTF-8 unicode for high level language support"

Open server from terminal typing "java -jar <file path> -s [port]".
   
Oper CLI client from terminal typing "java -jar <file path> -c [server ip] [server port]".

## GUI
Double click on the jar file. Server ip and port will be asked on title screen.
--------------------------------------------------------
   
You need at least 1 server and 2 clients are required to start the client
if you want to start a client on another pc:
- if the pc is on another network than the server -> you need a port forwarding and the IP of the server network
- if the pc is on the same network as the server -> the local IP of the server is required


ingsw2022-AM28

PROVA FINALE (INGEGNERIA DEL SOFTWARE) Group members:

- Lorenzo Biasiolo (lorenzo.biasiolo@mail.polimi.it)
- Isaia Belardinelli (isaia.belardinelli@mail.polimi.it)
- Leonardo Brusini (leonardo.brusini@mail.polimi.it)

--------------------------------------------
Eriantys is the final test of "Software Engineering", course of "Computer Science Engineering" held at Politecnico di Milano (2021/2022).

Teacher Alessandro Margara

What has been implemented:
Complete rules + communication + CLI + GUI + 2 FA

FA 1: Character Cards
FA 2: Resilience to client disconnections


Project specification
------------------------
------------------------------------------------
The project consists of a Java version of the board game Eriantys, made by Cranio creation.

You can find the full game here: https://www.craniocreations.it/prodotto/eriantys/.

The final version includes:

initial UML diagram;
final UML diagram, generated from the code by automated tools;
working game implementation, which has to be rules compliant;
source code of the implementation;
source code of unity tests.


Implemented Functionalities
------------------------
------------------------------------------------


Functionality	Status

Simple mode	🟢

Expert mode	🟢

Socket	🟢

GUI	🟢

CLI	🟢

Multiple games	🔴

Character Cards	🟢

Persistence	 🟢

Legend
🔴 Not Implemented     🟡 Implementing    🟢 Implemented

Test Case
---------
--------------------------------------------
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



Ways to launch servers and clients from the command line:
---------
--------------------------------------------

you need at least 1 server and 2 clients are required to start the client
if you want to start a client on another pc:
- if the pc is on another network than the server -> you need a port forwarding and the IP of the server network
- if the pc is on the same network as the server -> the local IP of the server is required




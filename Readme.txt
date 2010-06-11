* Copyright (c) 2009, Antony Dzeryn
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the names "PacMan", "Simian Zombie" nor the
*       names of its contributors may be used to endorse or promote products
*       derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY Antony Dzeryn ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL Antony Dzeryn BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


PacMan
------

  PacMan is a version of the classic arcade game designed for multiple players
  across a network.  It was written in Java.


Requirements
------------

  PacMan requires the Java 1.5 virtual machine.


Compiling the Game
------------------

  To compile the game, load the client and server projects into NetBeans and hit
  "Build".  If running via NetBeans, ensure that the server is running first.
  Note that the game comes in pre-compiled form.


Running the Game - The Server
-----------------------------

  PacMan comes in two parts: the server and the client.  To start a game, there
  are several steps.  The server must be run first, via the command line, with
  the following command:
  
    java -jar PacServer.jar
  
  The jar file can be found in the "server/PacServer/dist" folder.
  

Running the Game - The Client
-----------------------------
  
  The clients must be run oncethe server is running, and they must also be 
  configured so that each client knows where the server is:
  
    java -jar PacMan.jar -ss localhost 4444 local
  
  The jar file can be found in the "client/PacMan/dist" folder.
  
  Replace "localhost" with the IP address or hostname of the computer running
  the server.  If the server is running on the same computer as the client,
  leave this set to "localhost".  "4444" represents the port number on which
  the server is running; this must be 4444.  "Local" is the name of the XML
  file that will be saved as the configuration file for these settings.
  
  Next, run the client again:
  
    java -jar PacMan.jar -s local
  
  The word "local" should match the name given to the configuration file in the
  previous step.


Playing the Game
----------------

  If all went well at the configuration stage, a window will appear requesting
  a username.  Enter anything at this point and click "Login".  This will take
  you to a second menu.  Choose a game from the drop-down list (either 2, 4, 6
  or 8-player PacMan) and click "Play Game".  Once the required number of
  players have run their clients and joined the game, the game will start.
  
  The first player to enter a game plays as PacMan.  The others play as the
  ghosts.  PacMan receives 10 points for each pill eaten and 100 points for
  each ghost eaten.  Ghosts receive 1500 points each time they kill PacMan.
  
  The game ends when all of the pills have been eaten, PacMan runs out of lives,
  PacMan quits the game, or all of the ghosts quit the game.
  
  The cursor keys control movement of the ghosts and PacMan.
  

Limitations
-----------

  This version of PacMan is cut down from the original version submitted for my
  degree.  It initially had a PostgreSQL database behind it to store high
  scores, user data, login information and game data.  It also had a second game
  in the form of Connect 4.  These features have been removed for two reasons:
  
   - Virtually all of the code in this archive is my own; I do not want to
     distribute the work of the others involved in this project.
   - Although the excised code includes my work in addition to the work of
     others, the game is far simpler to set up in its current database-free
     form.
  
  Due to the omission of the database, the high score and statistics sections
  of the game are non-functional.
  

Beyond PacMan
-------------

  PacMan is just a small part of the system.  Though it is not obvious from the
  game itself, the architecture behind it is capable of supporting any number
  of action or turn-based multiplayer games, running simultaneously from one
  server.  The features present in the system include (though the database
  functionality has obviously been crippled):
  
   - Supports multiple games.
   - Supports realtime and turn-based games.
   - Supports multiple networked players.
   - Stores highscores.
   - Stores player and game statistics.
   - Includes cheat protection.
   - Uses clean tiered system design for communication between client, server
     and database layers.
   - Supports inter-client communication.
   - Detects failed clients/server and responds appropriately.
   - Detects client logouts.
   - Simple user authentication system.
   
  The "docs" folder includes a variety of documents describing the system and
  its features.
  
The Author
----------

  Written by Antony Dzeryn.  For more info, email me at spam_mail250@yahoo.com
  (that *is* a real email address).
  
  You can find my blog at http://ant.simianzombie.com/blog
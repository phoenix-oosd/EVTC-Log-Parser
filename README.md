# EVTC Log Parser

## About

This program is a parser for ` .evtc ` logs in GW2. It is written in Java 8, so requires an installation of ` JRE 1.8 `. Upon running the following folders will be created in the launch directory: ` /logs/ `, ` /graphs/ `, and ` /tables/ `. Copy ` .evtc ` files for parsing into the ` /logs/ ` folder. The program will recursively search the folder for ` .evtc ` files, and process all of them with the selected option. Both ` /graphs/ ` and ` /tables/ ` are output folders.

## Running

The Java program is a console application and needs to be ran with ` java.exe `. By default (on Windows), double-clicking a ` .jar ` file will instead run the program with ` javaw.exe `, resulting in the program not running. To circumvent this, run the program either in the command line or using the supplied ` run.bat ` file. Edit the  ` .bat ` files such that there is a path to the parser. There are two different ways to run this program:

### Standalone ###
Double clicking the standalone  `standalone .bat` will open a console. This will display a menu which will apply the chosen option to each  ` .evtc ` in the  `/logs/` directory. Simply enter the option you want by number (e.g. 1 for Final DPS) and press Enter.

### File Association ###
Double clicking any  ` .evtc ` file and when prompted with "Open with..." select the  ` fileassociation.bat` file. This will display the output into a console based on the command arguments given. The default argument is  ` "156" ` which displays the Final DPS, Misc. Combat Stats, and Final Boons in that order. Option 4 and 8 do not work with file association. Edit the  ` .bat ` file for the desired output.

## Options

The program displays all data in a tabular or graphical format. All DPS numbers are calculated from the damage each player deals to the boss (no cleave damage). Phases are sections of the fight when the bosses are vulnerable to damage. The parser has the following options:

1. Final DPS
    * DPS by player and group
    * Damage dealt by each player and group
2. Phase DPS
    * DPS for each phase where applicable
    * Phase duration
3. Damage Distribution - ranks damage output by skill by each player
    * Damage breakdown of each player by skill
    * Ranks skills in order of contribution
4. Total Damage Graph
    * Graphs the damage
    * Can be used to identify mechanical portions of the fight (e.g. flat lines for Matthias sacrifices)
5. Miscellaneous Combat Statistics
    * Healing, Toughness and Condition damage of each player
    * Fight rates such as Scholar uptime, seaweed salad movement, and critical rates
6. Final Boons
    * Show relevant boon uptime
    * Show relevant class buff uptime
7. Phase Boons
    * Boons for each phase where applicable
8. Text Dump
    * Saves the above graph(s) into ` /graphs/ `
    * Saves the above tables into ` /tables/ `

 
## Known Problems
Players that disconnect or join a fight late will be ignored.
Invulnerable detection on KC, Xera is not supported.
This program has only been tested on Windows.
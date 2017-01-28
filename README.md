# EVTC Log Parser

## About

This program is a parser for ` .evtc ` event chain logs created by ` arcdps `. It is written in Java 8, so requires an installation of ` JRE 1.8 `.

On first use, the following folders are created in the launch directory: ` /logs/ `, ` /graphs/ `, and ` /tables/ `. Copy ` .evtc ` file(s) for parsing into ` /logs/ `. The program will recursively search ` /logs/ ` for ` .evtc ` files. Both ` /graphs/ ` and ` /tables/ ` are output folders.

The ` .evtc ` files can be found at ` Documents\arcdps.cbtlogs `. Each folder corresponds to a different event, consult the table below to find the files you need.

| Folder        | Boss           |
| ------------- |----------------|
| 15438         | Vale Guardian  |
| 15429         | Gorseval       |
| 15375         | Sabetha        |
| 16123         | Slothasor      |
| 16088         | Berg           |
| 16137         | Zane           |
| 16125         | Narella        | 
| 16115         | Matthias       |
| 16235         | Keep Construct |
| 16246         | Xera           |

## Instructions

This Java program is a console application and needs to be ran with ` java.exe `. By default (on Windows), double-clicking a ` .jar ` file will instead run the program with ` javaw.exe `, resulting in nothing happening. To circumvent this, run the program either in the command line or using the supplied ` run.bat ` file. You **must** edit the ` .bat ` file so that the path to ` evtc_log_parser.jar ` is correct.

### Running ###

Double clicking `run .bat` will open a console with a menu. Enter the option you want by number (e.g. 1 for Final DPS) and press Enter. Each  ` .evtc ` file in `/logs/` will be processed. The results will be displayed on the console.

### File Association ###

Double clicking any ` .evtc ` file will display an ` Open with... ` dialogue. Tick ` Always use this app to open .evtc files` and choose ` run.bat`. Now, whenever you double-click an ` .evtc ` file, the file will be parsed on the spot based on the second command argument. The default argument is  ` "516" ` which displays the ` Miscellaneous Combat Statistics`, ` Final DPS `, and ` Final Boons ` in that order. Option 4 and 8 do not work with file association. Edit the  ` .bat ` file for the desired output.

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
8. Text Dump Tables
    * Saves the above tables into ` /tables/ `

## Known Problems
    * Players that disconnect or join a fight late will be ignored.
    * Phase related statistics may not work if the log is not a boss clear.
    * This program has only been tested on Windows 10.
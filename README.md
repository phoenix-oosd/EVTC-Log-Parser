# EVTC Log Parser

## About

This program is a parser for ` .evtc ` raid logs in GW2. It is written in Java, so requires an installation of ` JRE 1.8 `. Upon its first time running (and if the following folders do not exist), these folders will be created in the same directory: ` /logs/ `, ` /graphs/ `, and ` /tables/ `. The ` /logs/ ` folder is where you put the ` .evtc ` files you are interested in parsing. Both ` /graphs/ ` and ` /tables/ ` are output folders.

## Running

The Java program is a console application and needs to be ran with ` java.exe `. By default (on Windows), double-clicking a ` .jar ` file will instead run the program with ` javaw.exe `, resulting in the program not running. To circumvent this, run the program either in the command line or using the supplied ` .bat `.

## Options

The program displays all data in a tabular format. All DPS numbers are calculated from the damage each player deals to the boss (no cleave damage). Rates are a number between 0 and 1 (100% uptime). The parser has the following options:

1. Final DPS - Displays DPS and total damage for each player. Also displays the team DPS and team total damage, along with the boss of the HP to gauge log accuracy.
2. Phase DPS - Displays DPS for each player during each damage phase. Only supports W1 + Xera.
3. Total Damage Graph (not implemented yet)
4. Combat Statistics - Displays miscelleneous combat stats such as moving rate and scholar rate.
5. Final Boons - Displays the uptime of each releveant boon and class buff by the end of the fight.
6. Phase Boons (not implemented yet)
7. Text Dump - Saves each of the above tables into a single file at ` /tables/ `.
 
## Known Problems
1. Players that disconnect or join a fight late will be ignored.
2. Damage numbers on Xera will be overestimated, depending on how much damage is dealt during her break phases.

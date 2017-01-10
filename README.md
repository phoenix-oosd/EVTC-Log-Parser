# EVTC LogParser

## About

This program is a parser for ` .evtc ` raid logs in GW2. Upon its first time runningc (and if the folders do not exist), the following folders will be created ` /logs/ `, ` /graphs/ `, and ` /tables/ `. The ` /logs/ ` folder is where you put the ` .evtc ` files you are interested in parsing. Both ` /graphs/ ` and ` /tables/ ` are output folders.

## Running

The Java program is a console application and needs to be ran with ` java.exe ` and ` JRE 1.8 `. By default. double-clicking a ` .jar ` file will instead run the program with ` javaw.exe `, resulting in the program not running. To circumvent this run the program either in the command line or using the supplied ` .bat `.

## Options

The program will display all data in a tabular format. All DPS numbers are calculated from the damage each player deals to the boss (no cleave damage). Rates are a number between 0 and 1 (100% uptime). The parser has the following options:

1. Final DPS - Displays DPS and total damage for each player. Also displays the team DPS and team total damage, along with the boss of the HP to gauge accuracy.
2. Phase DPS - Displays DPS for each player during each damage phase. Only supports W1 + Xera.
3. Total Damage Graph (not implemented yet)
4. Combat Statistics - Displays miscelleneous combat stats such as moving rate and scholar rate.
5. Final Boons - Displays the uptime of each releveant boon and class buff by the end of the fight.
6. Phase Boons (not implemented yet)
7. Text Dump - Saves each of the above tables into a single file at ` /tables/ `.
 

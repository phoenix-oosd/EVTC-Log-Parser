# EVTC Log Parser #

## About ##

The project was started to help re-live boss encounters and identify areas for improvement in my own organized group raids. The resultant program is a parser for ` .evtc ` event chain logs created by [arcdps](https://www.deltaconnected.com/arcdps/).
It is written in Java 8 and requires an installation of [JRE 1.8](https://www.java.com/en/download/), but in most cases you probably already have it installed.

## User Manual ##

### Setting Up ###

Because of the way Java works in tandem with Windows, you don't want to run the program by just double-clicking ` evtc_log_parser.jar `. Instead you will have to run using the supplied `run .bat`, but more on this later. First you want to create a folder to contain the two files.

On the very first run, the following folders are created in the launch directory: `/logs/`, `/graphs/`, and `/tables/`. You will want to re-run the program after it detects `/logs/` is empty. Copy .evtc file(s) for parsing into /logs/. The program will recursively search `/logs/` and its subdirectories for `.evtc files`. Both `/graphs/` and `/tables/` are output folders for the related options.

### Basic Use ###

For basic use you can double click `run .bat` which will open up a console with a menu. Enter the option you want by number (e.g. 1 for Final DPS) and press Enter to confirm. Each  ` .evtc ` file in `/logs/` will be processed. The results will be displayed directly into the console, or be directed to files in the subdirectories where appropriate.

### Locating the Logs ###

The ` .evtc ` files are automatically created by ` arcdps ` at the end of encounters.
Your files can be found at ` Documents\arcdps.cbtlogs `, each subdirectory corresponds to a different encounter.
Consult the table below to find the logs you need want to analyze.


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
| 17194         | Cairn          |
| ?????         | BotP 2         |
| 17188         | Samarog        |
| ?????         | BotP 4         |

### File Association ###

Double clicking any ` .evtc ` file will display an ` Open with... ` dialogue. Tick ` Always use this app to open .evtc files` and choose ` run.bat`. Now, whenever you double-click an ` .evtc ` file, the file will be parsed on the spot based on the `options` command argument. The default argument is ` options=516 ` which displays the ` Miscellaneous Combat Statistics`, ` Final DPS `, and ` Final Boons ` in that order. Option 4 and 8 do not work with file association.

### Output Customization ###

You can customize the parsing to your liking by opening `run .bat` in a text editor such as `Notepad`.

You will notice everything is on a single line prefixed by ` start "EVTC Log Parser" /MAX `. If you don't want to maximize the console you can delete this, but it is highly recommended to maximize the console so text does not wrap around.

The `java -jar "path"` section is required and you *MUST* make sure you have an absolute path to ` evtc_log_parser.jar` (e.g. `C:\Users\JohnDoe\Desktop\EVTC Log Parser\evtc_log_parser.jar`). On Windows, the default path will only work if you create a desktop folder named "EVTC Log Parser" on your desktop, and move the attached files from the latest release into it.

After the path is specificed you can add arguments in any order by space separating `arg_name=value`. For convienience all the arguments have already been written for you.

The program has the following general arguments that apply to both basic running and file association: 

1. is_anon
    * The default value is `0`
    * You can edit this string to `1` to anonymize all player names

The program has the following arguments specific to file association:

1. file_path
    * The default value is `%1`
    * *NEVER* change or remove this argument as it is required for file association.
2. options
    * The default value is `5126`
    * *NEVER* remove this argument as it is requried for file association
    * You can edit this string to match any of the available options below to display tables in a certain order
   

### Options ###

All DPS numbers are derived from the players (and pets) to *ONLY* the boss. Phases are sections of the fight when the boss is vulnerable to damage. This only works for encounters which consist of predictable invulnerability sections, so not Keep Construct.

The program has the following options:

0. Text Dump
    * For debugging, or if you want to see a human readable version of the log
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
    * Healing, Toughness and Condition damage of each player on a scale of 1-10
    * Fight rates such as Scholar uptime, seaweed salad movement, and critical rates
6. Final Boons
    * Show relevant boon uptime
    * Show relevant class buff uptime
7. Phase Boons
    * Boons for each phase where applicable
8. Text Dump Tables
    * Saves options 1, 2, 3, 5, 6, and 7 tables into ` /tables/ `

### Interpreting Output ###

![example](https://github.com/phoenix-oosd/EVTC-Log-Parser/blob/master/example.png)

## Future ##

### Known Problems ###

  -Phase related statistics if you don't reach the final phase of the boss
  
  -This program has only been tested on Windows 10

### Working on... ###

  -Dynamic phase detection for non-linear fights like Keep Construct 
  
  -Damage taken statistics
  
  -If you have a request for a feature you can contact via PM on [reddit](https://www.reddit.com/user/ghandi-gandhi) or GW2 (Phoenix.5719)

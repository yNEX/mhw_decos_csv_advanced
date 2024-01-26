Utility for exporting your decorations inventory to Honey Hunter and [MHW-Wiki-DB](https://mhw.wiki-db.com/sim/?hl=en), directly from your save-file, but with some exciting modifications compared to the original. To track the differences between your exported files you can use a python tool called [`decoCompare`](https://github.com/yNEX/MHW_decoExportCompare)

**Additional changes made to expand the funcitonalitys of the original tool:**
1. Automatically searches for your savedata file if not specified (assumes the name is SAVEDATA1000, not sure if that's always the case)
2. Exceeds the maximum count limit for each decoration and displays the true amount for each. (The export method for mh-wiki-db uses some elements from the Honey Hunter export code, such as the maximum skill level and the corresponding maximum number of decorations recommended to achieve the max skill level.)
3. Defaults to exporting the mhw-wiki-db version in English. (HoneyHunter is no longer available as a browser version, and I didn't like the clutter it produced by creating four files when one is needed most of the time.)
4. Has command-line arguments:
- Export without success dialog box
- Export mhw-wiki-db or HoneyHunter version
- Export for Japanese or Traditional Chinese
- Set the output directory where the export files get saved
- Add the .exe to the user %PATH% environment variabl to make it usable without beeing in the same folder as the .exe (works but more testing needed)

# Usage 
Open your Terminal at the location of the downloaded executeable or just open the `MHW-ExportDecos - Start.bat`.
```
$ MHW-ExportDecos.exe <input_file> [options]
Options:
	-auto			Automatically search for the SAVEDATA1000 file
	-mhw			Export decorations in MHW Wiki DB format (default)
	-hh			Export decorations in Honeyhunter format
	-jp			Use Japanese language for output
	-tw			Use Traditional Chinese language for output
	-s, -silent		Suppress success message
	-o, -of, -output	Specify the output directory (default is the directory of the executable)
	-ap, -addPath		Add path of the .exe to your user PATH variable
	-h, -help		Show this usage message

You can adjust the filename by writing a string behind the last slash of output argument.
For now, the default file name gets appended to your custom filename.
If you want spaces in your filename, embrace the output path with double quotes.

Example: MHW-ExportDecos.exe <input_file> -mhw -tw -of "C:/output/old "
Outputfile: "old mhw-wiki-db-1.txt"
```
# Releases
https://github.com/yNEX/mhw_decos_csv_advanced/releases/latest


# Requirements
* Java Runtime Environment (JRE) version 8.0.0 or higher


# To do
* ~Decrypt the save file~
* ~Export to MHW DB~
* ~Create an executable and distribute~
* Make it read from equipped slots
* Integrate [`decoCompare`](https://github.com/yNEX/MHW_decoExportCompare) Python tool*

*I've developed a Python tool, `decoCompare`, that efficiently compares two export lists, highlighting any differences. This tool provides flexibility, allowing exports in various formats such as Excel, Text, or as a command line table. Consideration for GitHub upload is on the horizon.


# Credit
## MHW modding discord
* [legendff](https://github.com/LEGENDFF/mhw-Savecrypt) - save decrypting tool
* Deathcream - decoration byte offsets
* Vuze - script prototype
* Ice - jewel item ID to names
* [TanukiSharp](https://github.com/TanukiSharp/MHWSaveUtils)
* [AsteriskAmpersand](https://github.com/AsteriskAmpersand/MHW-Save-Editor)
## Others
* [TheNameKevinWasTaken](https://github.com/TheNameKevinWasTaken/mhw-deco-exporter) - honeyhunter deco export format
* [flier268](https://github.com/flier268) - support for Traditional Chinese and Japanese

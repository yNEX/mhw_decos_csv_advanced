Utility for exporting your decorations inventory to Honey Hunter, directly from your save-file.



*This is a quick and dirty GitHub Repo just to share this fork. I will do a clean repo with describing commits later. In the meanwhile feel free to test this version and to submit any feature requests or ideas. I also made a python script which compares two export files to get the differences between both and exports it as Excel, Text or as a Table in the command line. I will either integrate it into the .exe file or upload it seperately.*

**Additional changes made to expand the funcitonalitys of the original tool:**

1. Automatically searches for your savedata file if not specified (assumes the name is SAVEDATA1000, not sure if that's always the case)

2. Exceeds the maximum count limit for each decoration and displays the true amount for each. (The export method for mh-wiki-db uses some elements from the Honey Hunter export code, such as the maximum skill level and the corresponding maximum number of decorations recommended to achieve the max skill level.)

3. Defaults to exporting the mhw-wiki-db version in English. (HoneyHunter is no longer available as a browser version, and I didn't like the clutter it produced by creating four files when one is needed most of the time.)

4. Has command-line arguments like:
- Export without success dialog box
- Export mhw-wiki-db or HoneyHunter version
- Export for Japanese or Traditional Chinese
- Set the output directory where the export files get saved
- Add the .exe to the user %PATH% environment variabl to make it usable without beeing in the same folder as the .exe (works but more testing needed)

# Releases
https://github.com/yNEX/mhw_decos_csv_advanced/releases/latest

# Requirements
* Java 8u162+
* Maven 

# Build
```
$ mvn package
```

# Usage 
```
$ MHW-ExportDecos.exe <input_file> [options]
Options:
  -auto --> Automatically search for the SAVEDATA1000 file 
  -mhw --> Export decorations in MHW Wiki DB format (default)
  -hh --> Export decorations in Honeyhunter format
  -jp --> Use Japanese language for output
  -tw --> Use Traditional Chinese language for output
  -s, -silent --> Suppress success message
  -o, -of, -output --> Specify the output directory (default is the directory of the executable)
  -ap, -addPath --> Add path of the .exe to your user PATH variable
  -h, -help --> Show this usage message
Example: mhw_export_decos_cli.exe <input_file> -mhw -tw -of C:/output/
```

# To do

* ~Decrypt the save file~
* ~Export to MHW DB~
* ~Create an executable and distribute~
* Make it read from equipped slots
* add additional user feedback for easier error handling
* improve end user useability
* Integrate decoCompare python tool

# Credit

* [flier268](https://github.com/flier268) - support for Traditional Chinese and Japanese
## MHW modding discord

* [legendff](https://github.com/LEGENDFF/mhw-Savecrypt) - save decrypting tool
* Deathcream - decoration byte offsets
* Vuze - script prototype
* Ice - jewel item ID to names
* [TanukiSharp](https://github.com/TanukiSharp/MHWSaveUtils)
* [AsteriskAmpersand](https://github.com/AsteriskAmpersand/MHW-Save-Editor)

## Others

* [TheNameKevinWasTaken](https://github.com/TheNameKevinWasTaken/mhw-deco-exporter) - honeyhunter deco export format

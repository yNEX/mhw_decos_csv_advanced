package io.github.clicksilver.exporter.cmdLine;


public class CommandLineParser {
    public static String startDir = System.getProperty("launch4j.oldpwd") + "\\"; // used to get the current directory of terminal
    // default parameters set for command line options
    public static class CommandLineOptions {

        public boolean autoSearch = false;
        public boolean generateHoneyHunter = false;
        public boolean generateWikiDB = true;
        public boolean silentMode = false;
        public boolean jpLang = false;
        public boolean twLang = false;
        public String outputPath = startDir;
        public boolean addPath = false;
        public boolean showHelp = false;
    }

    // help message shown when .exe is executed without any parameter or with -h
    public static void showUsage() {
        System.err.println("Usage: MHW-ExportDecos.exe <input_file> [options]");
        System.err.println("");
        System.err.println("Options:");
        System.err.println("  -auto                 Automatically search for the SAVEDATA1000 file");
        System.err.println("  -mhw                  Export decorations in MHW Wiki DB format (default)");
        System.err.println("  -hh                   Export decorations in Honeyhunter format");
        System.err.println("  -jp                   Use Japanese language for output");
        System.err.println("  -tw                   Use Traditional Chinese language for output");
        System.err.println("  -s, -silent           Suppress success message");
        System.err.println("  -o, -of, -output      Specify output directory (default is the directory of the executable)");
        System.err.println("  -ap, -addPath         Add path of the .exe to your user PATH variable");
        System.err.println("  -h, -help             Show this usage message");
        System.err.println("");
        System.out.println("Example: mhw_export_decos_cli.exe <input_file> -mhw -tw -of C:/output/");
    }

    public static CommandLineOptions parse(String[] args) {
        CommandLineOptions options = new CommandLineOptions();

        // Check for custom command line options
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-auto":
                    options.autoSearch = true;
                    break;
                case "-mhw":
                    options.generateHoneyHunter = false;
                    break;
                case "-hh":
                    options.generateWikiDB = false;
                    break;
                case "-silent":
                case "-s":
                    options.silentMode = true;
                    break;
                case "-jp":
                    options.jpLang = true;
                    break;
                case "-tw":
                    options.twLang = true;
                    break;
                case "-o":
                case "-of":
                case "-output":
                    if (i + 1 < args.length) {
                        options.outputPath = startDir + args[i + 1];
                        i++; // skip already processed path argument
                    }
                    break;
                case "-ap":
                case "-addPath":
                    options.addPath = true;
                    break;
                case "-h":
                    options.showHelp = true;
                    break;
            }
        }
        return options;
    }

}

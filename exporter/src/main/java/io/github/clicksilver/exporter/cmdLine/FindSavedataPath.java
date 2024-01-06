package io.github.clicksilver.exporter.cmdLine;
import java.io.File;

public class FindSavedataPath {
    public static String findSavedataPath() {
        // Default path to the Steam userdata directory
        String steamDir = "C:\\Program Files (x86)\\Steam\\userdata\\";

        File steamDirFile = new File(steamDir);
        File[] userFolders = steamDirFile.listFiles();

        if (userFolders != null) {
            for (File userFolder : userFolders) {
                if (userFolder.isDirectory()) {
                    // Construct the path to the "remote" directory where saved data might be stored
                    String remoteDirPath = userFolder.getAbsolutePath() + "\\582010\\remote";
                    
                    // Construct the path to the specific saved data file ("SAVEDATA1000")
                    String savedataPath = remoteDirPath + "\\SAVEDATA1000";
                    File savedataFile = new File(savedataPath);

                    // Check if the SAVEDATA1000 file exists
                    if (savedataFile.exists()) {
                        // Return the path to the saved data file if it exists
                        return savedataPath;
                    }
                }
            }
        }
        return null;
    }
}

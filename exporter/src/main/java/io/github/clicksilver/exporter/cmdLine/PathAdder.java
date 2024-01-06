package io.github.clicksilver.exporter.cmdLine;

import java.io.IOException;

public class PathAdder {
    public static void addExePathToUserPath() throws IOException, InterruptedException {
        String pathOfExe = System.getProperty("user.dir"); // Pfad des aktuellen Verzeichnisses
        String userEnvPath = System.getenv("PATH");

        // Setze die PATH-Variable mit dem neuen Wert
        String newPath = userEnvPath + ";" + pathOfExe;
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "setx", "PATH", "\"" + newPath + "\"");

        processBuilder.inheritIO();  // Leitet die Ausgaben des Prozesses an die Konsole des Java-Prozesses weiter
        Process process = processBuilder.start();
        process.waitFor();

        System.out.println("Path of the .exe added successfully to your user-specific %PATH% environment variable.");
    }
}

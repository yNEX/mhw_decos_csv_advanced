package io.github.clicksilver.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.Desktop;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import io.github.clicksilver.exporter.LocalizeMapping.ILocalizeMapping;
import io.github.clicksilver.exporter.LocalizeMapping.en_US;
import io.github.clicksilver.exporter.LocalizeMapping.ja_JP;
import io.github.clicksilver.exporter.LocalizeMapping.zh_TW;

import io.github.clicksilver.exporter.cmdLine.CommandLineParser;
import io.github.clicksilver.exporter.cmdLine.FindSavedataPath;
import io.github.clicksilver.exporter.cmdLine.PathAdder;

public class App {

  // Decoration item IDs
  static final int kMinJewelId = 727;
  static final int kMaxJewelId = 2275;
  static final int kNumDecos = kMaxJewelId - kMinJewelId + 1;

  // 10 pages, 50 jewels per page
  static final int kDecoInventorySize = 50 * 10;

  // 8 bytes per jewel, (4 for ID + 4 for count)
  static final int kNumBytesPerDeco = 8;

  // direct offsets into the decrypted save, where each decorations list starts
  static final int kSaveSlotDecosOffsets[] = new int[] { 4302696, 6439464, 8576232 };

  private static String processPath(String path) {
    // Replace forward slashes with backslashes
    String correctedPath = path.replace('/', '\\');

    // Remove any leading backslashes
    correctedPath = correctedPath.replaceAll("^\\\\+", "");

    // Remove duplicate backslashes
    correctedPath = correctedPath.replaceAll("\\\\{2,}", "\\\\");

    // Remove trailing backslash if not just a drive letter (e.g., "C:\")
    if (correctedPath.length() > 3 && correctedPath.endsWith("\\")) {
      correctedPath = correctedPath.substring(0, correctedPath.length() - 1);
    }

    return correctedPath;
  }

  public static void main(String[] args) {
    CommandLineParser.CommandLineOptions options = CommandLineParser.parse(args);

    if (args.length == 0 || options.showHelp) {
      CommandLineParser.showUsage();
      System.exit(0);
    }

    boolean pathAdded = false;

    // Check if given parameter equals -addPath or -ap to add the .exe to user
    // %PATH% and exit the program without throwing error about invalid savedata
    // file
    if (options.addPath) {
      try {
        PathAdder.addExePathToUserPath();
        pathAdded = true;
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }

    Path p = null;
    if (options.autoSearch) {
      String savedataPath = FindSavedataPath.findSavedataPath();
      if (savedataPath == null) {
        System.out.println("SAVEDATA1000-file not found.");
        if (!pathAdded) {
          System.exit(1);
        }
      }
      p = Paths.get(savedataPath);
    } else if (args.length > 0 && !args[0].startsWith("-")) {
      // Assumes the first non-option argument is the file path
      p = Paths.get(args[0]);
    }
    if (p != null) {
      options.outputPath = processPath(options.outputPath);
      try {
        byte[] save = Files.readAllBytes(p);
        byte[] decrypted_save = io.github.legendff.mhw.save.Savecrypt.decryptSave(save);

        for (int i = 0; i < 3; ++i) {
          int[] decorationCounts = getJewelCounts(decrypted_save, kSaveSlotDecosOffsets[i]);

          if (decorationCounts == null) {
            continue;
          }

          if (options.generateHoneyHunter) {
            try {
              String fileName = "honeyhunter-" + String.valueOf(i + 1) + ".txt";
              FileWriter honeyFile = prepareFileWriter(options.outputPath, fileName);
              honeyFile.write(outputHoneyHunter(decorationCounts));
              honeyFile.write("\n");
              honeyFile.close();
            } catch (Exception e) {
              e.printStackTrace();
              JFrame frame = new JFrame();
              JOptionPane.showMessageDialog(frame, "Failed to write honey hunter output");
            }
          }

          if (options.generateWikiDB) {
            try {
              String fileName = "mhw-wiki-db-" + String.valueOf(i + 1) + getLangSuffix(options.jpLang, options.twLang)
                  + ".txt";
              FileWriter wikidbFile = prepareFileWriter(options.outputPath, fileName);
              wikidbFile.write(outputWikiDB(decorationCounts, getLangCode(options.jpLang, options.twLang)));
              wikidbFile.write("\n");
              wikidbFile.close();
            } catch (Exception e) {
              e.printStackTrace();
              JFrame frame = new JFrame();
              JOptionPane.showMessageDialog(frame, "Failed to write mhw-wiki-db output");
            }
          }
        }

        if (!options.silentMode) {
          JFrame frame = new JFrame();
          JOptionPane.showMessageDialog(frame, "Successfully exported decorations", "COMPLETE",
              JOptionPane.INFORMATION_MESSAGE);
        }

        if (options.outputPath != null) {
          // Dynamically determine the output directory
          String outputDirectory;
          File outputFile = new File(options.outputPath);

          if (outputFile.isDirectory()) {
            outputDirectory = options.outputPath;
          } else {
            // Extract the directory part before the last "/"
            int lastSeparatorIndex = options.outputPath.lastIndexOf(File.separator);
            if (lastSeparatorIndex != -1) {
              outputDirectory = options.outputPath.substring(0, lastSeparatorIndex);
            } else {
              outputDirectory = options.outputPath;
            }
          }

          // Print the dynamically determined output directory
          System.out.println("\nFile saved at " + outputDirectory);

          waitForUserInput(options, outputDirectory);
        }

      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e);
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, "Not a valid save file.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
      }
    } else {
      if (!pathAdded) {
        System.err.println("No valid file path provided.");
        System.exit(1);
      }
    }
    if (pathAdded) {
      System.exit(0);
    }
    System.exit(0);
  }

  private static void waitForUserInput(CommandLineParser.CommandLineOptions options, String outputDirectory) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Press Enter to open the output path or type 'q' and press Enter to exit.");
    String userInput = scanner.nextLine();
    
    if ("q".equalsIgnoreCase(userInput)) {
      System.exit(0);
    }
    
    scanner.close();
    
      try {
        File directoryFile = new File(outputDirectory);
        if (directoryFile.exists()) {
          Desktop.getDesktop().open(directoryFile);
        } else {
          System.err.println("Output directory does not exist: " + outputDirectory);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }


  // helper method to check if the destination folder exists and create of not,
  // before trying to write output file
  private static FileWriter prepareFileWriter(String outputPath, String fileName) throws IOException {
    File file = new File(outputPath + fileName);
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    FileWriter fileWriter = new FileWriter(file);
    fileWriter.write("WARNING: Unequip all decorations before using this otherwise the count will be wrong.\n\n");
    return fileWriter;
  }

  private static String getLangSuffix(boolean jpLang, boolean twLang) {
    if (jpLang) {
      return "-jp";
    } else if (twLang) {
      return "-tw";
    } else {
      return "";
    }
  }

  private static String getLangCode(boolean jpLang, boolean twLang) {
    if (jpLang) {
      return "ja-JP";
    } else if (twLang) {
      return "zh-TW";
    } else {
      return "en-US";
    }
  }

  public static void printJewels(int[] counts) {
    for (int i = 0; i < counts.length; ++i) {
      String name = DecorationNames.getDecorationName(i + kMinJewelId);
      int count = counts[i];
      if (name.length() != 0 && count != 0) {
        System.out.println(name + ": " + counts[i]);
      }
    }
  }

  public static String outputWikiDB(int[] counts, String lang) {
    StringBuilder contents = new StringBuilder("");
    contents.append("{");
    for (int i = 0; i < counts.length; ++i) {
      String name = DecorationNames.getDecorationName(i + kMinJewelId);
      if (name.length() == 0) {
        continue;
      }
      int count = Math.min(counts[i], 9999);
      contents.append("\"");

      ILocalizeMapping mapping;
      switch (lang) {
        case "zh-TW":
          mapping = new zh_TW();
          break;
        case "ja-JP":
          mapping = new ja_JP();
          break;
        default:
          mapping = new en_US();
          break;
      }

      contents.append(mapping.Map(name));

      contents.append("\":");
      contents.append(count);
      if (i != counts.length - 1) {
        contents.append(",");
      }
    }
    contents.append("}");
    return contents.toString();
  }

  public static String outputHoneyHunter(int[] counts) {
    int hhCounts[] = new int[HoneyHunter.kNumDecos];
    Arrays.fill(hhCounts, 0);

    for (int i = 0; i < counts.length; ++i) {
      String name = DecorationNames.getDecorationName(i + kMinJewelId);
      if (name.length() == 0) {
        continue;
      }
      int count = Math.min(counts[i], HoneyHunter.getMaxCountFromName(name));
      int order = HoneyHunter.getOrderingFromName(name);
      if (order < 0 || count < 0) {
        continue;
      }
      hhCounts[order] = count;
    }

    StringBuilder contents = new StringBuilder("");
    contents.append(hhCounts[0]);
    for (int i = 1; i < hhCounts.length; ++i) {
      contents.append(",");
      contents.append(hhCounts[i]);
    }
    return contents.toString();
  }

  public static int[] getJewelCounts(byte[] bytes, int offset) {
    int counts[] = new int[kNumDecos];

    ByteBuffer buf = ByteBuffer.wrap(bytes, offset, kDecoInventorySize * kNumBytesPerDeco);
    buf.order(ByteOrder.LITTLE_ENDIAN);

    boolean anyNonZero = false;

    for (int i = 0; i < kDecoInventorySize; i++) {
      int jewelId = buf.getInt();
      int jewelCount = buf.getInt();
      if (jewelId == 0) {
        continue;
      }
      if (jewelId < kMinJewelId || jewelId > kMaxJewelId) {
        System.out.println("Error parsing decorations. Index=" + i +
            " ID=" + jewelId +
            " Count=" + jewelCount);
        return null;
      }

      if (jewelCount > 0) {
        anyNonZero = true;
      }

      counts[jewelId - kMinJewelId] = jewelCount;
    }

    if (anyNonZero) {
      return counts;
    }
    return null;
  }
}

package cli;

import dictionary.EnglishDictionary;

import java.util.Scanner;

/**
 * Command line interface for the Perfect Hashing Dictionary
 */
public class DictionaryCommandLine {
  private EnglishDictionary dictionary;
  private Scanner scanner;

  public DictionaryCommandLine(EnglishDictionary dictionary) {
    this.dictionary = dictionary;
    dictionary.build();
    this.scanner = new Scanner(System.in);
  }

  /**
   * Starts the command line interface
   */
  public void start() {
    System.out.println("Perfect Hashing Dictionary");
    System.out.println("Available commands:");
    System.out.println("  insert <word>     - Insert a word into the dictionary");
    System.out.println("  delete <word>     - Delete a word from the dictionary");
    System.out.println("  search <word>     - Search for a word in the dictionary");
    System.out.println("  batch-insert <filename> - Insert words from file");
    System.out.println("  batch-delete <filename> - Delete words from file");
    System.out.println("  size              - Print the current dictionary size");
    System.out.println("  space             - Print the current space used by the dictionary");
    System.out.println("  help              - Print this help message");
    System.out.println("  exit              - Exit the program");

    boolean running = true;
    while (running) {
      System.out.print("> ");
      String input = scanner.nextLine().trim();
      String[] parts = input.split("\\s+", 2);
      String command = parts[0].toLowerCase();
      String arg = parts.length > 1 ? parts[1] : "";

      // try {
        switch (command) {
          case "insert":
            insertWord(arg);
            break;
          case "delete":
            deleteWord(arg);
            break;
          case "search":
            searchWord(arg);
            break;
          case "batch-insert":
            batchInsert(arg);
            break;
          case "batch-delete":
            batchDelete(arg);
            break;
          case "size":
            printSize();
            break;
          case "space":
            printSpace();
            break;
          case "help":
            printHelp();
            break;
          case "exit":
            running = false;
            System.out.println("Exiting...");
            break;
          default:
            System.out.println("Unknown command. Type 'help' for available commands.");
            break;
        }
      // } catch (Exception e) {
      //   System.out.println("Error: " + e.getMessage());
      // }
    }
    scanner.close();
  }

  private void insertWord(String word) {
    if (word.isEmpty()) {
      System.out.println("Please specify a word to insert.");
      return;
    }

    boolean result = dictionary.insert(word);
    if (result) {
      System.out.println("Word '" + word + "' inserted successfully.");
    } else {
      System.out.println("Failed to insert word '" + word + "'. It may already exist.");
    }
  }

  private void deleteWord(String word) {
    if (word.isEmpty()) {
      System.out.println("Please specify a word to delete.");
      return;
    }

    boolean result = dictionary.delete(word);
    if (result) {
      System.out.println("Word '" + word + "' deleted successfully.");
    } else {
      System.out.println("Word '" + word + "' not found.");
    }
  }

  private void searchWord(String word) {
    if (word.isEmpty()) {
      System.out.println("Please specify a word to search for.");
      return;
    }

    boolean found = dictionary.search(word);
    if (found) {
      System.out.println("Word '" + word + "' found in dictionary.");
    } else {
      System.out.println("Word '" + word + "' not found in dictionary.");
    }
  }

  private void batchInsert(String filename){
    if (filename.isEmpty()) {
      System.out.println("Please specify a filename.");
      return;
    }

    System.out.println("Batch inserting words from file: " + filename);
    int[] result = dictionary.batchInsert(filename);
    System.out.println("Successfully inserted " + result[0] + " words, failed to insert " + result[1] + " words.");
  }

  private void batchDelete(String filename){
    if (filename.isEmpty()) {
      System.out.println("Please specify a filename.");
      return;
    }

    System.out.println("Batch deleting words from file: " + filename);
    int[] result = dictionary.batchDelete(filename);
    System.out.println("Successfully deleted " + result[0] + " words, failed to delete " + result[1] + " words.");
  }

  private void printSize() {
    System.out.println("Dictionary size: " + dictionary.size() + " words");
  }

  private void printSpace() {
    System.out.println("Dictionary space usage: " + dictionary.getSpace() + " units");
  }

  private void printHelp() {
    System.out.println("Available commands:");
    System.out.println("  insert <word>     - Insert a word into the dictionary");
    System.out.println("  delete <word>     - Delete a word from the dictionary");
    System.out.println("  search <word>     - Search for a word in the dictionary");
    System.out.println("  batch-insert <filename> - Insert words from file");
    System.out.println("  batch-delete <filename> - Delete words from file");
    System.out.println("  size              - Print the current dictionary size");
    System.out.println("  space             - Print the current space used by the dictionary");
    System.out.println("  help              - Print this help message");
    System.out.println("  exit              - Exit the program");
  }

  /**
   * Main method to run the command line interface
   */
  public static void main(String[] args) {
    Scanner startupScanner = new Scanner(System.in);
    String hashTableType = "";

    // Ask user for hash table type
    while (!hashTableType.equals("linear") && !hashTableType.equals("quadratic")) {
      System.out.println("Select hash table type:");
      System.out.println("1. Linear space hash table");
      System.out.println("2. Quadratic space hash table");
      System.out.print("Enter your choice (1 or 2): ");

      String choice = startupScanner.nextLine().trim();

      if (choice.equals("1")) {
        hashTableType = "linear";
      } else if (choice.equals("2")) {
        hashTableType = "quadratic";
      } else {
        System.out.println("Invalid choice. Please enter 1 or 2.");
      }
    }

    System.out.println("Initializing " + hashTableType + " space perfect hash table...");
    EnglishDictionary dictionary = new EnglishDictionary(hashTableType);
    DictionaryCommandLine cli = new DictionaryCommandLine(dictionary);
    cli.start();

    startupScanner.close();
  }
}
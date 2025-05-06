package cli;

import dictionary.EnglishDictionary;

import java.util.Scanner;

/**
 * Command line interface for the Perfect Hashing Dictionary
 */
public class DictionaryCommandLine {
  private EnglishDictionary dictionary;
  private Scanner scanner;

  // ANSI color and style codes
  private static final String RESET = "\u001B[0m";

  // Success and positive outcomes
  private static final String SUCCESS = "\u001B[1;32m"; // Bold Green

  // Errors and critical failures
  private static final String ERROR = "\u001B[1;31m"; // Bold Red

  // Informational text
  private static final String INFO = "\u001B[1;34m"; // Bold Blue

  // Titles and headers
  private static final String HEADER = "\u001B[1;35m"; // Bold Purple

  // Commands and user inputs
  private static final String COMMAND = "\u001B[1;36m"; // Bold Cyan

  // Values and statistics
  private static final String VALUE = "\u001B[1;96m"; // Bold Bright Cyan

  // Prompt
  private static final String PROMPT = "\u001B[1;93m"; // Bold Bright Yellow

  public DictionaryCommandLine(EnglishDictionary dictionary) {
    this.dictionary = dictionary;
    dictionary.build();
    this.scanner = new Scanner(System.in);
  }

  /**
   * Starts the command line interface
   */
  public void start() {
    System.out.println(HEADER + "╔═══════════════════════════════════╗");
    System.out.println("║     Perfect Hashing Dictionary     ║");
    System.out.println("╚═══════════════════════════════════╝" + RESET);
    printHelp();

    boolean running = true;
    while (running) {
      System.out.print(PROMPT + "➤ " + RESET);
      String input = scanner.nextLine().trim();
      String[] parts = input.split("\\s+", 2);
      String command = parts[0].toLowerCase();
      String arg = parts.length > 1 ? parts[1] : "";

      try {
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
            System.out.println(INFO + "Exiting..." + RESET);
            break;
          default:
            System.out.println(ERROR + "Unknown command. Type 'help' for available commands." + RESET);
            break;
        }
      } catch (Exception e) {
        System.out.println(ERROR + "Error: " + e.getMessage() + RESET);
      }
    }
    scanner.close();
  }

  private void insertWord(String word) {
    if (word.isEmpty()) {
      System.out.println(ERROR + "Please specify a word to insert." + RESET);
      return;
    }

    boolean result = dictionary.insert(word);
    if (result) {
      System.out.println(SUCCESS + "✓ Word '" + VALUE + word + SUCCESS + "' inserted successfully." + RESET);
    } else {
      System.out
          .println(ERROR + "⚠ Failed to insert word '" + VALUE + word + ERROR + "'. It may already exist." + RESET);
    }
  }

  private void deleteWord(String word) {
    if (word.isEmpty()) {
      System.out.println(ERROR + "Please specify a word to delete." + RESET);
      return;
    }

    boolean result = dictionary.delete(word);
    if (result) {
      System.out.println(SUCCESS + "✓ Word '" + VALUE + word + SUCCESS + "' deleted successfully." + RESET);
    } else {
      System.out.println(ERROR + "⚠ Word '" + VALUE + word + ERROR + "' not found." + RESET);
    }
  }

  private void searchWord(String word) {
    if (word.isEmpty()) {
      System.out.println(ERROR + "Please specify a word to search for." + RESET);
      return;
    }

    boolean found = dictionary.search(word);
    if (found) {
      System.out.println(SUCCESS + "✓ Word '" + VALUE + word + SUCCESS + "' found in dictionary." + RESET);
    } else {
      System.out.println(ERROR + "⚠ Word '" + VALUE + word + ERROR + "' not found in dictionary." + RESET);
    }
  }

  private void batchInsert(String filename) {
    if (filename.isEmpty()) {
      System.out.println(ERROR + "Please specify a filename." + RESET);
      return;
    }

    System.out.println(INFO + "Batch inserting words from file: " + VALUE + filename + RESET);
    int[] result = dictionary.batchInsert(filename);
    System.out.println(SUCCESS + "✓ Successfully inserted " + VALUE + result[0] + SUCCESS + " words" + RESET +
        (result[1] > 0 ? ERROR + ", failed to insert " + VALUE + result[1] + ERROR + " words." + RESET : "."));
  }

  private void batchDelete(String filename) {
    if (filename.isEmpty()) {
      System.out.println(ERROR + "Please specify a filename." + RESET);
      return;
    }

    System.out.println(INFO + "Batch deleting words from file: " + VALUE + filename + RESET);
    int[] result = dictionary.batchDelete(filename);
    System.out.println(SUCCESS + "✓ Successfully deleted " + VALUE + result[0] + SUCCESS + " words" + RESET +
        (result[1] > 0 ? ERROR + ", failed to delete " + VALUE + result[1] + ERROR + " words." + RESET : "."));
  }

  private void printSize() {
    System.out.println(INFO + "Dictionary size: " + VALUE + dictionary.size() + INFO + " words" + RESET);
  }

  private void printSpace() {
    System.out.println(INFO + "Dictionary space usage: " + VALUE + dictionary.getSpace() + INFO + " units" + RESET);
  }

  private void printHelp() {
    System.out.println(HEADER + "Available Commands:" + RESET);
    System.out.println(COMMAND + "  insert " + VALUE + "<word>" + RESET + "     - Insert a word into the dictionary");
    System.out.println(COMMAND + "  delete " + VALUE + "<word>" + RESET + "     - Delete a word from the dictionary");
    System.out.println(COMMAND + "  search " + VALUE + "<word>" + RESET + "     - Search for a word in the dictionary");
    System.out.println(COMMAND + "  batch-insert " + VALUE + "<file>" + RESET + " - Insert words from file");
    System.out.println(COMMAND + "  batch-delete " + VALUE + "<file>" + RESET + " - Delete words from file");
    System.out.println(COMMAND + "  size" + RESET + "              - Print the current dictionary size");
    System.out.println(COMMAND + "  space" + RESET + "             - Print the current space used by the dictionary");
    System.out.println(COMMAND + "  help" + RESET + "              - Print this help message");
    System.out.println(COMMAND + "  exit" + RESET + "              - Exit the program");
    System.out.println();
  }

  /**
   * Main method to run the command line interface
   */
  public static void main(String[] args) {
    Scanner startupScanner = new Scanner(System.in);
    String hashTableType = "";

    // Ask user for hash table type
    System.out.println(HEADER + "╔═══════════════════════════════════╗");
    System.out.println("║  Perfect Hashing Implementation   ║");
    System.out.println("╚═══════════════════════════════════╝" + RESET);

    while (!hashTableType.equals("linear") && !hashTableType.equals("quadratic")) {
      System.out.println(INFO + "Select hash table type:" + RESET);
      System.out.println(COMMAND + "1. Linear space hash table (O(n) space complexity)");
      System.out.println("2. Quadratic space hash table (O(n²) space complexity)" + RESET);
      System.out.print(PROMPT + "Enter your choice (1 or 2): " + RESET);

      String choice = startupScanner.nextLine().trim();

      if (choice.equals("1")) {
        hashTableType = "linear";
      } else if (choice.equals("2")) {
        hashTableType = "quadratic";
      } else {
        System.out.println(ERROR + "Invalid choice. Please enter 1 or 2." + RESET);
      }
    }

    System.out.println(INFO + "Initializing " + VALUE + hashTableType + INFO + " space perfect hash table..." + RESET);
    EnglishDictionary dictionary = new EnglishDictionary(hashTableType);
    DictionaryCommandLine cli = new DictionaryCommandLine(dictionary);
    cli.start();

    startupScanner.close();
  }
}
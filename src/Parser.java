import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Handles the parsing of a single .vm file. Reads a VM command,
 * parses the command into its lexical components, and provides
 * convenient access to these components. Ignores all white space
 * and comments
 */
public class Parser {
    private final Scanner scanner;
    private String currentCommand;

    /**
     * Opens the input file/stream and gets ready to parse it
     *
     * @param vmCode
     * @throws FileNotFoundException
     */
    public Parser(File vmCode) throws FileNotFoundException {
        scanner = new Scanner((vmCode));
        currentCommand = "";
    }

    /**
     * Are there more commands in the input?
     *
     * @return
     */
    public boolean hasMoreCommands() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command from the input and makes it the
     * current command. Should be called only if hasMoreCommands()
     * is true. Initially there is no current command
     */
    public void advance() {
        currentCommand = scanner.nextLine().toLowerCase().trim();
    }

    /**
     * Returns a constant representing the type of the current
     * command. C_ARITHMETIC is returned for all the arithmetic
     * and logical commands
     *
     * @return
     */
    public String commandType() {
        return "commandType";
    }

    public String arg1() {
        return "arg1";
    }

    public int arg2() {
        return 0;
    }

    public void printFile() {
        while (hasMoreCommands()) {
            advance();
            System.out.println(currentCommand);
        }
    }

    public void printCommandType() {
        while (hasMoreCommands()) {
            advance();
            System.out.println(commandType());
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File vmCode = new File("../MemoryAccess/BasicTest/BasicTest.vm");
        Parser parser = new Parser(vmCode);
        parser.printFile();
        // parser.printCommandType();
    }
}

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
     * Splits the current command into tokens delimited by whitespaces
     * and returns a non-empty token at some given index
     *
     * @param tokenNumber
     * @return
     */
    public String getToken(int tokenNumber) {
        String[] tokens = currentCommand.split(" ");
        int tokenCount = 0;
        for (String token : tokens) {
            if (!token.isEmpty()) {
                tokenCount++;
            }
            if (tokenCount == tokenNumber) {
                return token.trim();
            }
        }
        return "INVALID";
    }

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
        currentCommand = scanner.nextLine().trim();
    }

    /**
     * Returns a constant representing the type of the current
     * command. C_ARITHMETIC is returned for all the arithmetic
     * and logical commands
     *
     * @return
     */
    public String commandType() {
        if (currentCommand.isEmpty()) {
            return "COMMENT_WHITESPACE";
        } else {
            String firstToken;
            if (currentCommand.indexOf(' ') == -1) {
                firstToken = currentCommand;
            } else {
                firstToken = currentCommand.substring(0, currentCommand.indexOf(' ')).trim();
            }
            return switch (firstToken) {
                case "//" -> "COMMENT_WHITESPACE";
                case "add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not" -> "C_ARITHMETIC";
                case "push" -> "C_PUSH";
                case "pop" -> "C_POP";
                case "label" -> "C_LABEL";
                case "goto" -> "C_GOTO";
                case "if-goto" -> "C_IF";
                case "function" -> "C_FUNCTION";
                case "call" -> "C_CALL";
                case "return" -> "C_RETURN";
                default -> "UNIDENTIFIED";
            };
        }
    }

    /**
     * Returns the first argument of the current command. In
     * the case of C_ARITHMETIC, the command itself (add, sub,
     * etc.) is returned. Should not be called if the current
     * command is C_RETURN
     *
     * @return
     */
    public String arg1() {
        if (commandType().equals("C_ARITHMETIC")) {
            return currentCommand;
        } else {
            return getToken(2);
        }
    }

    /**
     * Returns the second argument of the current command.
     * Should be called only if the current command is C_PUSH,
     * C_POP, C_FUNCTION, C_CALL
     *
     * @return
     */
    public int arg2() {
        String thirdToken = getToken(3);
        if (thirdToken.equals("INVALID")) {
            return -1;
        } else {
            return Integer.parseInt(thirdToken);
        }
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
            if (!(commandType().equals("C_RETURN") || commandType().equals("COMMENT_WHITESPACE"))) {
                System.out.print(arg1());
            }
            if (commandType().equals("C_PUSH") || commandType().equals("C_POP") || commandType().equals("C_FUNCTION") || commandType().equals("C_CALL")) {
                System.out.print("\t" + arg2());
            }
            System.out.println();
        }
    }

/*
    public static void main(String[] args) throws FileNotFoundException {
        File vmCode = new File("../08/ProgramFlow/BasicLoop/BasicLoop.vm");
        Parser parser = new Parser(vmCode);
        // parser.printFile();
        parser.printCommandType();
    }
    
 */
}

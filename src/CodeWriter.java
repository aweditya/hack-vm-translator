import java.io.File;

/**
 * Generates assembly code from the parsed VM command
 */
public class CodeWriter {
    /**
     * Opens the output file/stream and gets ready to write into it
     *
     * @param assemblyCode
     */
    public CodeWriter(File assemblyCode) {

    }

    /**
     * Writes to the output file the assembly code that implements
     * the given arithmetic command
     *
     * @param command
     */
    public void writeArithmetic(String command) {

    }

    /**
     * Writes to the output file the assembly code that implements
     * the given command, where command is either C_PUSH, C_POP
     *
     * @param command
     * @param segment
     * @param index
     */
    public void writePushPop(String command, String segment, int index) {

    }

    /**
     * Closes the output file
     */
    public void close() {

    }
}

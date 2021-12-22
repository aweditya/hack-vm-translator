import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Generates assembly code from the parsed VM command
 */
public class CodeWriter {
    private final BufferedWriter writer;

    /**
     * Opens the output file/stream and gets ready to write into it
     *
     * @param assemblyWriter
     */
    public CodeWriter(FileWriter assemblyWriter) {
        writer = new BufferedWriter(assemblyWriter);
    }

    /**
     * Writes to the output file the assembly code that implements
     * the given arithmetic command
     *
     * @param command
     */
    public void writeArithmetic(String command) throws IOException {
        String translation = "// " + command + "\n";
        switch (command) {
            case "add":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=D+M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                break;
            case "sub":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=M-D\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                break;
            case "neg":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=-M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                break;
            case "eq":

                break;
            case "gt":

                break;
            case "lt":

                break;
            case "and":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=D&M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                break;
            case "or":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=D|M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                break;
            case "not":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=!M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                break;
            default:
                translation = "Not a valid arithmetic command\n";
                break;
        }
        writer.write(translation);
    }

    /**
     * Writes to the output file the assembly code that implements
     * the given command, where command is either C_PUSH, C_POP
     *
     * @param command
     * @param segment
     * @param index
     */
    public void writePushPop(String command, String segment, int index) throws IOException {
        String translation = "// " + command + " " + segment + " " + index + "\n";
        if (command.equals("push")) {
            switch (segment) {
                case "local":
                /*
                Logic: address = LCL + index; *SP = *address; SP++;
                 */
                    translation += "@LCL\n"; // address=LCL+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "AD=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "@A=M\n";
                    translation += "@M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    break;
                case "argument":
                /*
                Logic: address = ARG + index; *SP = *address; SP++;
                 */
                    translation += "@ARG\n"; // address=ARG+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "AD=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "@A=M\n";
                    translation += "@M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    break;
                case "this":
                /*
                Logic: address = THIS + index; *SP = *address; SP++;
                 */
                    translation += "@THIS\n"; // address=THIS+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "AD=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "@A=M\n";
                    translation += "@M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    break;
                case "that":
                /*
                Logic: address = THAT + index; *SP = *address; SP++;
                 */
                    translation += "@THAT\n"; // address=THAT+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "AD=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "@A=M\n";
                    translation += "@M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    break;
                case "constant":
                /*
                Logic: *SP = index; SP++;
                 */
                    translation += "@" + index + "\n"; // D=index
                    translation += "D=A";
                    translation += "@SP"; // *SP=D
                    translation += "A=M";
                    translation += "M=D";
                    translation += "@SP"; // SP++
                    translation += "M=M+1";
                    break;
                case "static":

                    break;
                case "pointer":

                    break;
                case "temp":

                    break;
                default:
                    translation = "Not a valid segment\n";
                    break;
            }
        } else if (command.equals("pop")) {
            if (segment.equals("local")) {
                /*
                Logic: address = LCL + index; SP--; *address = *SP;
                 */
                
            } else if (segment.equals("argument")) {
                /*
                Logic: address = ARG + index; SP--; *address = *SP;
                 */

            } else if (segment.equals("this")) {
                /*
                Logic: address = THIS + index; SP--; *address = *SP;
                 */

            } else if (segment.equals("that")) {
                /*
                Logic: address = THAT + index; SP--; *address = *SP;
                 */

            } else if (segment.equals("static")) {

            } else if (segment.equals("pointer")) {

            } else if (segment.equals("temp")) {

            } else {
                translation = "Not a valid segment\n";
            }
        } else {
            translation = "Not a push/pop command\n";
        }
        writer.write(translation);
    }

    /**
     * Closes the output file
     */
    public void close() throws IOException {
        writer.close();
    }

    public static void main(String[] args) {

    }
}

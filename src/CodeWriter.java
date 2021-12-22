import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Generates assembly code from the parsed VM command
 */
public class CodeWriter {
    private final BufferedWriter writer;
    private int instructionPointer;

    /**
     * Opens the output file/stream and gets ready to write into it
     *
     * @param assemblyWriter
     */
    public CodeWriter(FileWriter assemblyWriter) {
        writer = new BufferedWriter(assemblyWriter);
        instructionPointer = 0;
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
                instructionPointer += 8;
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
                instructionPointer += 8;
                break;
            case "neg":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=-M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 5;
                break;
            case "eq":
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "AM=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "@SP\n"; // 6
                translation += "A=M\n"; // 7
                translation += "@" + (instructionPointer + 13) + "\n"; // 8
                translation += "D;JEQ\n"; // 9
                translation += "M=-1\n"; // 10
                translation += "@" + (instructionPointer + 14) + "\n"; // 11
                translation += "0;JMP\n"; // 12
                translation += "M=0\n"; // 13
                translation += "@SP\n"; // 14
                translation += "M=M+1\n"; // 15
                instructionPointer += 16;
                break;
            case "gt":
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "AM=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "@SP\n"; // 6
                translation += "A=M\n"; // 7
                translation += "@" + (instructionPointer + 13) + "\n"; // 8
                translation += "D;JGT\n"; // 9
                translation += "M=-1\n"; // 10
                translation += "@" + (instructionPointer + 14) + "\n"; // 11
                translation += "0;JMP\n"; // 12
                translation += "M=0\n"; // 13
                translation += "@SP\n"; // 14
                translation += "M=M+1\n"; // 15
                instructionPointer += 16;
                break;
            case "lt":
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "AM=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "@SP\n"; // 6
                translation += "A=M\n"; // 7
                translation += "@" + (instructionPointer + 13) + "\n"; // 8
                translation += "D;JLT\n"; // 9
                translation += "M=-1\n"; // 10
                translation += "@" + (instructionPointer + 14) + "\n"; // 11
                translation += "0;JMP\n"; // 12
                translation += "M=0\n"; // 13
                translation += "@SP\n"; // 14
                translation += "M=M+1\n"; // 15
                instructionPointer += 16;
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
                instructionPointer += 8;
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
                instructionPointer += 8;
                break;
            case "not":
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=!M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 5;
                break;
            default:
                translation = "Not a valid arithmetic command\n";
                instructionPointer += 1;
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
                    instructionPointer += 10;
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
                    instructionPointer += 10;
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
                    instructionPointer += 10;
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
                    instructionPointer += 10;
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
                    instructionPointer += 7;
                    break;
                case "static":

                    break;
                case "pointer":

                    break;
                case "temp":

                    break;
                default:
                    translation = "Not a valid segment\n";
                    instructionPointer += 1;
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
                instructionPointer += 1;
            }
        } else {
            translation = "Not a push/pop command\n";
            instructionPointer += 1;
        }
        writer.write(translation);
    }

    /**
     * Closes the output file
     */
    public void close() throws IOException {
        writer.close();
    }
}

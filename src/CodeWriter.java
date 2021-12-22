import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Generates assembly code from the parsed VM command
 */
public class CodeWriter {
    private final BufferedWriter writer;
    private int instructionPointer;
    private final String asmFileName;

    /**
     * Opens the output file/stream and gets ready to write into it
     *
     * @param asmFileName
     */
    public CodeWriter(String asmFileName) throws IOException {
        FileWriter assemblyWriter = new FileWriter(asmFileName);
        writer = new BufferedWriter(assemblyWriter);
        instructionPointer = 0;
        this.asmFileName = asmFileName.substring(asmFileName.lastIndexOf('/') + 1);
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
            case "add" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=D+M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 8;
            }
            case "sub" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=M-D\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 8;
            }
            case "neg" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=-M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 5;
            }
            case "eq" -> {
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "AM=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "@" + (instructionPointer + 13) + "\n"; // 6
                translation += "D;JEQ\n"; // 7
                translation += "@SP\n"; // 8
                translation += "A=M\n"; // 9
                translation += "M=0\n"; // 10
                translation += "@" + (instructionPointer + 16) + "\n"; // 11
                translation += "0;JMP\n"; // 12
                translation += "@SP\n"; // 13
                translation += "A=M\n"; // 14
                translation += "M=-1\n"; // 15
                translation += "@SP\n"; // 16
                translation += "M=M+1\n"; // 17
                instructionPointer += 18;
            }
            case "gt" -> {
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "AM=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "@" + (instructionPointer + 13) + "\n"; // 6
                translation += "D;JGT\n"; // 7
                translation += "@SP\n"; // 8
                translation += "A=M\n"; // 9
                translation += "M=0\n"; // 10
                translation += "@" + (instructionPointer + 16) + "\n"; // 11
                translation += "0;JMP\n"; // 12
                translation += "@SP\n"; // 13
                translation += "A=M\n"; // 14
                translation += "M=-1\n"; // 15
                translation += "@SP\n"; // 16
                translation += "M=M+1\n"; // 17
                instructionPointer += 18;
            }
            case "lt" -> {
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "AM=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "@" + (instructionPointer + 13) + "\n"; // 6
                translation += "D;JLT\n"; // 7
                translation += "@SP\n"; // 8
                translation += "A=M\n"; // 9
                translation += "M=0\n"; // 10
                translation += "@" + (instructionPointer + 16) + "\n"; // 11
                translation += "0;JMP\n"; // 12
                translation += "@SP\n"; // 13
                translation += "A=M\n"; // 14
                translation += "M=-1\n"; // 15
                translation += "@SP\n"; // 16
                translation += "M=M+1\n"; // 17
                instructionPointer += 18;
            }
            case "and" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=D&M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 8;
            }
            case "or" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=D|M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 8;
            }
            case "not" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "M=!M\n";
                translation += "@SP\n";
                translation += "M=M+1\n";
                instructionPointer += 5;
            }
            default -> {
                translation = "Not a valid arithmetic command\n";
                instructionPointer += 1;
            }
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
                    // Logic: address = LCL + index; *SP = *address; SP++;
                    translation += "@LCL\n"; // address=LCL+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "A=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    instructionPointer += 10;
                    break;
                case "argument":
                    // Logic: address = ARG + index; *SP = *address; SP++;
                    translation += "@ARG\n"; // address=ARG+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "A=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    instructionPointer += 10;
                    break;
                case "this":
                    // Logic: address = THIS + index; *SP = *address; SP++;
                    translation += "@THIS\n"; // address=THIS+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "A=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    instructionPointer += 10;
                    break;
                case "that":
                    // Logic: address = THAT + index; *SP = *address; SP++;
                    translation += "@THAT\n"; // address=THAT+index
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "A=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    instructionPointer += 10;
                    break;
                case "constant":
                    // Logic: *SP = index; SP++;
                    translation += "@" + index + "\n"; // D=index
                    translation += "D=A\n";
                    translation += "@SP\n"; // *SP=D
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    instructionPointer += 7;
                    break;
                case "static":
                    translation += "@" + asmFileName + "." + index + "\n";
                    translation += "D=M\n";
                    translation += "@SP\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n";
                    translation += "M=M+1\n";
                    instructionPointer += 7;
                    break;
                case "pointer":
                    // Logic: *SP = THIS/THAT; SP++;
                    String thisOrThat = "";
                    if (index == 0) {
                        thisOrThat = "THIS";
                    } else if (index == 1) {
                        thisOrThat = "THAT";
                    }
                    translation += "@" + thisOrThat + "\n"; // D=THIS/THAT
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=D
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    instructionPointer += 7;
                    break;
                case "temp":
                    //  Logic: address = 5 + index; *SP = *address; SP++;
                    translation += "@5\n"; // address=5+index
                    translation += "D=A\n";
                    translation += "@" + index + "\n";
                    translation += "A=D+A\n";
                    translation += "D=M\n";
                    translation += "@SP\n"; // *SP=*address
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n"; // SP++
                    translation += "M=M+1\n";
                    instructionPointer += 10;
                    break;

                default:
                    translation = "Not a valid segment\n";
                    instructionPointer += 1;
                    break;
            }
        } else if (command.equals("pop")) {
            switch (segment) {
                case "local":
                    // Logic: address = LCL + index; SP--; *address = *SP;
                    translation += "@LCL\n";
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=M\n";
                    translation += "@SP\n";
                    translation += "A=M+1\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    instructionPointer += 14;
                    break;
                case "argument":
                    // Logic: address = ARG + index; SP--; *address = *SP;
                    translation += "@ARG\n";
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=M\n";
                    translation += "@SP\n";
                    translation += "A=M+1\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    instructionPointer += 14;
                    break;
                case "this":
                    // Logic: address = THIS + index; SP--; *address = *SP;
                    translation += "@THIS\n";
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=M\n";
                    translation += "@SP\n";
                    translation += "A=M+1\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    instructionPointer += 14;
                    break;
                case "that":
                    // Logic: address = THAT + index; SP--; *address = *SP;
                    translation += "@THAT\n";
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=M\n";
                    translation += "@SP\n";
                    translation += "A=M+1\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    instructionPointer += 14;
                    break;
                case "static":
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=M\n";
                    translation += "@" + asmFileName + "." + index + "\n";
                    translation += "M=D\n";
                    instructionPointer += 5;
                    break;
                case "pointer":
                    // Logic: SP--; THIS/THAT = *SP;
                    String thisOrThat = "";
                    if (index == 0) {
                        thisOrThat = "THIS";
                    } else if (index == 1) {
                        thisOrThat = "THAT";
                    }
                    translation += "@SP\n"; // SP--;
                    translation += "AM=M-1\n";
                    translation += "D=M\n"; // D=*SP;
                    translation += "@" + thisOrThat + "\n"; // THIS/THAT=D;
                    translation += "M=D\n";
                    instructionPointer += 5;
                    break;
                case "temp":
                    // Logic: address = 5 + index; SP--; *address = *SP;
                    translation += "@5\n";
                    translation += "D=A\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=M\n";
                    translation += "@SP\n";
                    translation += "A=M+1\n";
                    translation += "A=M\n";
                    translation += "M=D\n";
                    instructionPointer += 14;
                    break;
                default:
                    translation = "Not a valid segment\n";
                    instructionPointer += 1;
                    break;
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

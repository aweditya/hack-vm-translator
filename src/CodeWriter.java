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
     * Informs the codeWriter that the translation of a new VM file
     * has started (called by the main program of the VM translator)
     *
     * @param filename
     */
    public void setFileName(String filename) {

    }

    /**
     * Writes the assembly instructions that effect the bootstrap code
     * that initializes the VM. This code must be placed at the beginning
     * of the generated *.asm file
     */
    public void writeInit() {

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
                translation += "A=M-1\n";
                translation += "M=D+M\n";
                instructionPointer += 6;
            }
            case "sub" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "A=M-1\n";
                translation += "M=M-D\n";
                instructionPointer += 6;
            }
            case "neg" -> {
                translation += "@SP\n";
                translation += "A=M-1\n";
                translation += "M=-M\n";
                instructionPointer += 3;
            }
            case "eq" -> {
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "A=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "M=-1\n"; // 6
                translation += "@" + (instructionPointer + 12) + "\n"; // 7
                translation += "D;JEQ\n"; // 8
                translation += "@SP\n"; // 9
                translation += "A=M-1\n"; // 10
                translation += "M=0\n"; // 11
                instructionPointer += 12;
            }
            case "gt" -> {
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "A=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "M=-1\n"; // 6
                translation += "@" + (instructionPointer + 12) + "\n"; // 7
                translation += "D;JGT\n"; // 8
                translation += "@SP\n"; // 9
                translation += "A=M-1\n"; // 10
                translation += "M=0\n"; // 11
                instructionPointer += 12;
            }
            case "lt" -> {
                translation += "@SP\n"; // 0
                translation += "AM=M-1\n"; // 1
                translation += "D=M\n"; // 2
                translation += "@SP\n"; // 3
                translation += "A=M-1\n"; // 4
                translation += "D=M-D\n"; // 5
                translation += "M=-1\n"; // 6
                translation += "@" + (instructionPointer + 12) + "\n"; // 7
                translation += "D;JLT\n"; // 8
                translation += "@SP\n"; // 9
                translation += "A=M-1\n"; // 10
                translation += "M=0\n"; // 11
                instructionPointer += 12;
            }
            case "and" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "A=M-1\n";
                translation += "M=D&M\n";
                instructionPointer += 6;
            }
            case "or" -> {
                translation += "@SP\n";
                translation += "AM=M-1\n";
                translation += "D=M\n";
                translation += "@SP\n";
                translation += "A=M-1\n";
                translation += "M=D|M\n";
                instructionPointer += 6;
            }
            case "not" -> {
                translation += "@SP\n";
                translation += "A=M-1\n";
                translation += "M=!M\n";
                instructionPointer += 3;
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
                    translation += "AM=M-1\n";
                    translation += "D=D+M\n";
                    translation += "A=D-M\n";
                    translation += "M=D-A\n";
                    instructionPointer += 9;
                    break;
                case "argument":
                    // Logic: address = ARG + index; SP--; *address = *SP;
                    translation += "@ARG\n";
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=D+M\n";
                    translation += "A=D-M\n";
                    translation += "M=D-A\n";
                    instructionPointer += 9;
                    break;
                case "this":
                    // Logic: address = THIS + index; SP--; *address = *SP;
                    translation += "@THIS\n";
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=D+M\n";
                    translation += "A=D-M\n";
                    translation += "M=D-A\n";
                    instructionPointer += 9;
                    break;
                case "that":
                    // Logic: address = THAT + index; SP--; *address = *SP;
                    translation += "@THAT\n";
                    translation += "D=M\n";
                    translation += "@" + index + "\n";
                    translation += "D=D+A\n";
                    translation += "@SP\n";
                    translation += "AM=M-1\n";
                    translation += "D=D+M\n";
                    translation += "A=D-M\n";
                    translation += "M=D-A\n";
                    instructionPointer += 9;
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
                    translation += "AM=M-1\n";
                    translation += "D=D+M\n";
                    translation += "A=D-M\n";
                    translation += "M=D-A\n";
                    instructionPointer += 9;
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
     * Writes assembly code that effects the label command
     *
     * @param label
     * @throws IOException
     */
    public void writeLabel(String label) throws IOException {
        String translation = "// label " + label + "\n";
        translation += "(" + label + ")\n";
        writer.write(translation);
    }

    /**
     * Writes assembly code that effects the goto command
     *
     * @param label
     * @throws IOException
     */
    public void writeGoto(String label) throws IOException {
        String translation = "// goto " + label + "\n";
        translation += "@" + label + "\n";
        translation += "0;JMP\n";
        instructionPointer += 2;
        writer.write(translation);
    }

    /**
     * Writes assembly code that effects the if-goto command
     *
     * @param label
     * @throws IOException
     */
    public void writeIf(String label) throws IOException {
        String translation = "// if-goto " + label + "\n";
        translation += "@SP\n";
        translation += "AM=M-1\n";
        translation += "D=M\n";
        translation += "@" + label + "\n";
        translation += "D;JNE\n";
        instructionPointer += 5;
        writer.write(translation);
    }

    /**
     * Writes assembly code that effects the function command
     *
     * @param functionName
     * @param numVars
     */
    public void writeFunction(String functionName, int numVars) {

    }

    /**
     * Writes assembly code that effects the call command
     *
     * @param functionName
     * @param numArgs
     */
    public void writeCall(String functionName, int numArgs) {

    }

    /**
     * Writes assembly code that effects the return command
     */
    public void writeReturn() {

    }

    /**
     * Closes the output file
     */
    public void close() throws IOException {
        writer.close();
    }
}

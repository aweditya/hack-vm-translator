import java.io.File;
import java.io.IOException;

public class VMTranslator {
    private final String fileOrDirectoryName;
    private final CodeWriter codeWriter;

    public VMTranslator(String fileOrDirectoryName) throws IOException {
        this.fileOrDirectoryName = fileOrDirectoryName;
        String asmFileName = "";
        if (fileOrDirectoryName.endsWith(".vm")) {
            asmFileName = fileOrDirectoryName.substring(0, fileOrDirectoryName.lastIndexOf('.')) + ".asm";
        }
        codeWriter = new CodeWriter(asmFileName);
    }

    public void translateVMCodeToAssembly() throws IOException {
        File vmCode = new File(fileOrDirectoryName);

        Parser parser = new Parser(vmCode);
        codeWriter.setFileName(fileOrDirectoryName.substring(fileOrDirectoryName.lastIndexOf('/') + 1, fileOrDirectoryName.lastIndexOf('.')));
        while (parser.hasMoreCommands()) {
            parser.advance();
            switch (parser.commandType()) {
                case "C_ARITHMETIC":
                    codeWriter.writeArithmetic(parser.arg1());
                    break;
                case "C_PUSH":
                    codeWriter.writePushPop("push", parser.arg1(), parser.arg2());
                    break;
                case "C_POP":
                    codeWriter.writePushPop("pop", parser.arg1(), parser.arg2());
                    break;
                case "C_LABEL":
                    codeWriter.writeLabel(parser.arg1());
                    break;
                case "C_GOTO":
                    codeWriter.writeGoto(parser.arg1());
                    break;
                case "C_IF":
                    codeWriter.writeIf(parser.arg1());
                    break;
                case "C_FUNCTION":
                    codeWriter.writeFunction(parser.arg1(), parser.arg2());
                    break;
                case "C_CALL":
                    codeWriter.writeCall(parser.arg1(), parser.arg2());
                    break;
                case "C_RETURN":
                    codeWriter.writeReturn();
                    break;
                default:
                    break;
            }
        }
        codeWriter.close();
    }

    public static void main(String[] args) throws IOException {
        String fileOrDirectoryName = "../07/MemoryAccess/StaticTest/StaticTest.vm";
        VMTranslator translator = new VMTranslator(fileOrDirectoryName);
        translator.translateVMCodeToAssembly();
    }
}

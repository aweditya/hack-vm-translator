import java.io.File;
import java.io.IOException;

public class VMTranslator {
    private final Parser parser;
    private final CodeWriter codeWriter;

    public VMTranslator(File vmCode, String asmFileName) throws IOException {
        parser = new Parser(vmCode);
        codeWriter = new CodeWriter(asmFileName);
    }

    public void translateVMCodeToAssembly() throws IOException {
        while (parser.hasMoreCommands()) {
            parser.advance();
            switch (parser.commandType()) {
                case "C_ARITHMETIC":
                    String command = parser.arg1();
                    codeWriter.writeArithmetic(command);
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
                case "C_IF":
                    codeWriter.writeIf(parser.arg1());
                default:
                    break;
            }
        }
        codeWriter.close();
    }

    public static void main(String[] args) throws IOException {
        String vmPath = "../08/ProgramFlow/FibonacciSeries/FibonacciSeries.vm";
        File vmCode = new File(vmPath);

        String asmFileName = vmPath.substring(0, vmPath.lastIndexOf('.')) + ".asm";
        VMTranslator translator = new VMTranslator(vmCode, asmFileName);
        translator.translateVMCodeToAssembly();
    }
}

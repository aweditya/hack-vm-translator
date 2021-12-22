import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class VMTranslator {
    private final Parser parser;
    private final CodeWriter codeWriter;

    public VMTranslator(File vmCode, FileWriter assemblyWriter) throws FileNotFoundException {
        parser = new Parser(vmCode);
        codeWriter = new CodeWriter(assemblyWriter);
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
                default:
                    break;
            }
        }
        codeWriter.close();
    }

    public static void main(String[] args) throws IOException {
        String vmPath = "../StackArithmetic/SimpleAdd/SimpleAdd.vm";
        File vmCode = new File(vmPath);

        String asmPath = vmPath.substring(0, vmPath.lastIndexOf('.')) + ".asm";
        FileWriter assemblyWriter = new FileWriter(asmPath);
        VMTranslator translator = new VMTranslator(vmCode, assemblyWriter);
        translator.translateVMCodeToAssembly();
    }
}

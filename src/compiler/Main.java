package compiler;

import compiler.AST.Program;
import compiler.Vtable.VtableGenerator;
import compiler.codegen.CodeGenVisitor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class Compiler_test {
    private PrintStream writer;
    private Scanner_phase1 scanner;

    Compiler_test( Scanner_phase1 scanner,PrintStream writer) {
        this.writer = writer;
        this.scanner=scanner;
    }

    public void run() {
        processFile();
    }
    private void processFile() {
        Program cu;
        try {
            cu = parse();
        } catch (Exception e) {
            String textSegment = "";
            textSegment += ".data\n";
            textSegment += "\terror: .asciiz \"Lexical Error\"\n";
            textSegment += ".text\n" + "\t.globl main\n\n";
            textSegment += "\tmain:\n";
            textSegment += "\t\tli $v0, 4\n";
            textSegment += "\t\tla $a0, error\n";
            textSegment += "\t\tsyscall\n";
            textSegment += "\t\t#END OF PROGRAM\n";
            textSegment += "\t\tli $v0,10\n\t\tsyscall\n";
            writer.print(textSegment);
            return;
        }


        try {
            vtableAnalysis(cu);
            generateCode(cu);
        } catch (Exception e) {
            String textSegment = "";
            textSegment += ".data\n";
            textSegment += "\terror: .asciiz \"Semantic Error\"\n";
            textSegment += ".text\n" + "\t.globl main\n\n";
            textSegment += "\tmain:\n";
            textSegment += "\t\tli $v0, 4\n";
            textSegment += "\t\tla $a0, error\n";
            textSegment += "\t\tsyscall\n";
            textSegment += "\t\t#END OF PROGRAM\n";
            textSegment += "\t\tli $v0,10\n\t\tsyscall\n";
            writer.print(textSegment);
            return;
        }

    }

    private Program parse() throws Exception {
        parser par = new parser(scanner);
        par.parse();
        return par.getRoot();
    }
    private void vtableAnalysis(Program cu) throws Exception {
        System.out.println("in type visitor");
        cu.accept(new VtableGenerator());
        System.out.println("TV done\n");
    }

    private void generateCode(Program cu) throws Exception {
        System.out.println("in code gen");
        cu.accept(new CodeGenVisitor(writer));
        System.out.println("CG done\n");
    }
}

public class Main {
    //this is for our test script
    public static void main(String[] args) throws IOException {
        try {
            if (args.length < 4) {
                System.out.println("Usage: java compiler.compiler.Main -i <input> -o <output>");
                return;
            }
            String inputFileName = null;
            String outputFileName = null;
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-i")) {
                        inputFileName = args[i + 1];
                    }
                    if (args[i].equals("-o")) {
                        outputFileName = args[i + 1];
                    }
                }
            }
            PrintStream writer = null;
            if (outputFileName != null) {
                writer = new PrintStream(outputFileName);
            }
            Path path=Paths.get(inputFileName);
            StringBuilder contentBuilder=new StringBuilder();
            try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8))
            {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            String input=contentBuilder.toString();
            Pre_Processor p=new Pre_Processor(input);
            String define_handeled=p.handle_define();
            StringReader aa = new StringReader(define_handeled);
            Scanner_phase1 scanner=new Scanner_phase1(aa);
            Compiler_test compiler = new Compiler_test(scanner,writer);
            compiler.run();
            writer.flush();
            writer.close();
        }catch (Exception e){
        }
    }


    //this is for quera test and should implement and return string
    public static boolean run(File inputFile) throws Exception {
        StringBuilder contentBuilder=new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(inputFile.getAbsolutePath()), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {

        }

        try {
            FileOutputStream fout=new FileOutputStream("file.txt");
            //creating Printstream obj
            PrintStream out=new PrintStream(fout);
            String input=contentBuilder.toString();
            Pre_Processor p=new Pre_Processor(input);
            String define_handeled=p.handle_define();
            Scanner_phase1 scanner=new Scanner_phase1(new StringReader(define_handeled));
            parser par=new parser(scanner);
            par.parse();
            Program cu = par.getRoot();
            cu.accept(new VtableGenerator());
            cu.accept(new CodeGenVisitor(out));

            return true;
        } catch (Exception e ) {
            e.printStackTrace();

            return false;
        }
        catch (Error e){
            return false;
        }

    }

}

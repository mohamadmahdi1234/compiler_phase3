package compiler;
import java.io.File;
import java.io.IOException;

public class Testing {
    public static void main(String[] args) throws IOException {
        File s=new File("C:\\Users\\NeginPardaz\\IdeaProjects\\compiler_phase3\\src\\tests\\file-to-import.d");
        try {
            System.out.println(Main.run(s));
        }catch (Exception e){

        }
    }
}

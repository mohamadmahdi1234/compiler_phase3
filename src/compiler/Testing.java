package compiler;
import compiler.Main;

import java.io.File;
import java.io.IOException;

public class Testing {
    public static void main(String[] args) throws IOException {
        File s=new File("C:\\Users\\NeginPardaz\\Downloads\\Phase2AllTests\\in-out\\t208-define.in");
        try {
            System.out.println(Main.run(s));
        }catch (Exception e){

        }
    }
}

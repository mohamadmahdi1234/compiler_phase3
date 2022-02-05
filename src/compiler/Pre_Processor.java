package compiler;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Pre_Processor {
    private Pattern all_define_expression;
    private Pattern first_two_par_of_define;
    private Map<String,String> define_value;
    private List<String>imp;
    private Pattern all_import_expression;
    private Pattern first_part_of_import;
    String input;
    int count;
    public Pre_Processor(String input) {
        this.input = input;
        first_two_par_of_define=Pattern.compile("(?<= |^|\n)define(?<=\\S)\\h+(?=\\S)[A-Za-z]+[a-zA-Z0-9_]*");
        all_define_expression=Pattern.compile("(?<= |^|\n)define(?<=\\S)\\h+(?=\\S)[A-Za-z]+[a-zA-Z0-9_]*.*");
        all_import_expression=Pattern.compile("(?<= |^|\n)import(?<=\\S)\\h+(?=\\S)\\\"([^\\\"\\r\\n]| \\\\t| \\\\r |\\\\n| \\\\\\\" |\\\\| \\\\')*\\\"");
        first_part_of_import=Pattern.compile("(?<= |^|\n)import(?<=\\S)\\h+(?=\\S)");
        define_value=new LinkedHashMap<>();
        imp=new ArrayList<>();
        count=0;
    }
    public void first_check(){
        String[]spell=input.split("\n");
        for(String s:spell){
            if(s.startsWith("import")||s.startsWith("define")){
                count+=s.length()+1;
            }else{
                break;
            }
        }
    }
    public void handle_import()throws Exception{
        Matcher all_imp=all_import_expression.matcher(input);
        Matcher first_part=first_part_of_import.matcher(input);
        while(all_imp.find()){
            first_part.find();
            if(all_imp.group().substring(first_part.group().length()).matches("\\s*")){
                throw new Exception();
            }
            if(all_imp.start()>count){
                throw new Exception();
            }
            imp.add(input.substring(first_part.end()+1,all_imp.end()-1));
            input=all_imp.replaceFirst("");
            all_imp=all_import_expression.matcher(input);
            first_part=first_part_of_import.matcher(input);
        }
    }
    public void handle_imp_two()throws Exception{
        for(String add:imp){
            String j = "C:\\Users\\NeginPardaz\\IdeaProjects\\compiler_phase3\\src\\tests"+"\\"+add;
            File ss=new File(j);
            StringBuilder contentBuilder=new StringBuilder();
            try (Stream<String> stream = Files.lines( Paths.get(ss.getAbsolutePath()), StandardCharsets.UTF_8))
            {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }
            catch (IOException e)
            {
            }
            System.out.println(j);
            input=contentBuilder.toString()+input;
        }

    }
    public String handle_define()throws Exception{
        first_check();
        find_defines();
        change_defined_values();
        //handle_import();
        //handle_imp_two();
        return input;
    }
    public void find_defines()throws Exception{
        Matcher all_def=all_define_expression.matcher(input);
        Matcher first_two=first_two_par_of_define.matcher(input);
        while(all_def.find()){
            first_two.find();
            if(all_def.group().substring(first_two.group().length()).matches("\\s*")){
                throw new Exception();
            }
            if(all_def.start()>count){
                throw new Exception();
            }
            define_value.put(first_two.group().split("\\s+")[1],input.substring(first_two.end(),all_def.end()));
            input=all_def.replaceFirst("");
            all_def=all_define_expression.matcher(input);
            first_two=first_two_par_of_define.matcher(input);
        }
        //input=input.trim();

    }
    public void change_defined_values(){
        ArrayList<Integer>a=new ArrayList<>();
        ArrayList<Integer>b=new ArrayList<>();
        for (String key:define_value.keySet()) {
            Pattern defined_variable=Pattern.compile("((?<=\\+|-|\\*|\\/|\"|=|>|<|>=|<=|&|\\||%|!|\\^|\\(|\\)|\\}|\\[|\\{|\\]|\\\\|\\.|\\r|\\t|;|,)|(?<= |^|\n))"+key+"((?=\\+|-|\\*|\\/|\"|=|>|<|>=|<=|&|\\||%|!|\\^|\\(|\\)|\\}|\\[|\\{|\\]|\\\\|\\.|\\r|\\t|;|,)|(?= |$|\n))");
            Matcher second=defined_variable.matcher(input);
            String s="";
            while (second.find()){
                a.add(second.start());
                b.add(second.end());
            }
            int holder=0;
            for(int i=0;i<a.size();i++){
                s+=input.substring(holder,a.get(i))+define_value.get(key);
                holder=b.get(i);
                if(i==a.size()-1){
                    s+=input.substring(b.get(i));
                }
            }
            if(!s.equals("")){
                input=s;
            }
            a.clear();
            b.clear();
        }
        //input.trim();
    }

    public static void main(String[] args) {

    }

}

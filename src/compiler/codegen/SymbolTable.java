package compiler.codegen;

import compiler.Vtable.ClassDecaf;
import compiler.Vtable.VtableGenerator;
import compiler.Vtable.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class SymbolTable implements Symbol {
    public static ArrayList<Scope> allScopes = new ArrayList<>();
    private ArrayList<Scope> scopes = new ArrayList<>();
    private Scope currentScope;

    public Scope getCurrentScope() {
        return currentScope;
    }

    public void enterScope(String id) {
        Scope newScope = new Scope(id);
        scopes.add(newScope);
        allScopes.add(newScope);
        currentScope = newScope;

    }

    public void leaveCurrentScope() {
        if (currentScope != null)
            scopes.remove(currentScope);
        currentScope = scopes.get(scopes.size() - 1);
    }

    public void put(String id, SymbolInfo si) throws Exception {
        if (currentScope.getVariables().containsKey(id)) {
            throw new Exception("current scope already contains an entry for " + id);
        }
        currentScope.getVariables().put(id, si);
        System.out.println("current scope is : "+currentScope.getName()+" and var is : "+id );
    }

    public void putsss(String id, SymbolInfo si) throws Exception {
        for(ClassDecaf cd:VtableGenerator.classes){
            if(cd.getName().equals(currentScope.getName())){
                Field field = new Field(id);
                field.setSymbolInfo(si);
                field.setAccessMode(Field.getCurrentAccessMode());
                field.setClassDecaf(ClassDecaf.currentClass);
                cd.getFields().add(field);
            }
        }
    }

    public Symbol get(String id) throws Exception {
        System.out.println();
        for(Scope d:scopes){
            System.out.println(d.getName());
            for(String x:d.getVariables().keySet()){
                System.out.println(x);
            }
        }
        System.out.println();
        System.out.println("variable to find "+id+" we are in scope "+currentScope);
        Symbol sy=null;
        for (Scope s:allScopes) {
            if (s.getName().equals("global"))
                if(s.getVariables().containsKey(id)) {
                    sy = s.getVariables().get(id);
                    System.out.println("variable found in global variable "+id+" symbol "+sy);
                }
        }
        System.out.println("az avalin for gozasht and sy is "+sy);
        for (Scope s:allScopes) {
            if(s.getVariables().containsKey(id)) {
                if(scopes.contains(s)){
                System.out.println("variable found in whole in " + s.getName() + " id " + id + " symbol " + sy);
                sy = s.getVariables().get(id);
            }
               }
        }
        for (Scope s:allScopes) {
            if(s.getVariables().containsKey(id)) {
                if(s.getName().equals(currentScope.getName())){
                System.out.println("variable found in whole in " + s.getName() + " id " + id + " symbol " + sy);
                sy = s.getVariables().get(id);
            }
             }
        }
        System.out.println("az dovim halghe gozasht and sy is "+sy);
        if(currentScope!=null){
            ClassDecaf fimf=null;
            for(ClassDecaf cd:VtableGenerator.classes){
                if(currentScope.getName().split("_")[0].equals(cd.getName())){
                    fimf = cd;
                }
            }
            if(fimf!=null){
                for(Field f:fimf.getFields()){
                    if(f.getName().equals(id)){
                        System.out.println("variable found in scope "+fimf.getName()+" and id "+id+" and symbol "+sy);
                        sy=f.getSymbolInfo();
                    }
                }
            }
        }
        System.out.println("az sevomi gozasht and sy is "+sy);
        if(sy!=null){
            return sy;
        }
        System.out.println("id "+id+" not found");
        throw new Exception("variable " + id + " did'nt declared ");
    }

    String getScopeNameOfIdentifier(String id)throws Exception {
        String sy=null;
        for (Scope s:allScopes) {
            if (s.getName().equals("global"))
                if(s.getVariables().containsKey(id))
                    sy = s.getName();
        }
        for (Scope s:allScopes) {
            if(s.getVariables().containsKey(id)) {
                //if (s.getName().equals(currentScope.getName()))
                    sy = s.getName();
            }
        }
        for (Scope s:allScopes) {
            if(s.getVariables().containsKey(id)) {
                if (s.getName().equals(currentScope.getName()))
                    sy = s.getName();
            }
        }
        if(currentScope!=null){
            ClassDecaf fimf=null;
            boolean dd=false;
            for(ClassDecaf cd:VtableGenerator.classes){
                if(currentScope.getName().split("_")[0].equals(cd.getName())){
                    fimf = cd;
                }
            }
            if(fimf!=null){
                for(Field f:fimf.getFields()){
                    if(f.getName().equals(id)){
                        sy=fimf.getName();
                    }
                }
            }
        }
        return sy;
    }

    public String getCurrentScopeName() {
        return currentScope.getName();
    }


    public ArrayList<Scope> getScopes() {
        return allScopes;
    }

}
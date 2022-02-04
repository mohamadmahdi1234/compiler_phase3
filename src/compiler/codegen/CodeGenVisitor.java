package compiler.codegen;

import compiler.AST.*;

import java.io.PrintStream;
import java.util.*;

import compiler.Vtable.ClassDecaf;
import compiler.Vtable.Function;
import compiler.Vtable.VtableGenerator;

/**
 * An AST visitor which generates Jasmin code.
 */
public class CodeGenVisitor implements SimpleVisitor {
    private PrintStream stream;
    private int labelIndex;
    boolean wc =false;
    String current_id="";
    String result="";
    int label_counter=0;
    private List<Function> functions = VtableGenerator.functions;
    private List<ClassDecaf> classes = VtableGenerator.classes;
    private HashMap<String, String> stringLiterals = new HashMap<>();
    private HashMap<String, String> stringLiterals_rev = new HashMap<>();
    private HashMap<String, String> which_for_which = new HashMap<>();
    private HashMap<String, String> new_holder = new HashMap<>();
    private SymbolTable symbolTable = new SymbolTable();
    private List<Scope> all_scopes = SymbolTable.allScopes;
    private int blockIndex;
    private int arrayNumbers = 0;
    private int DtoItoBLabel = 0;
    private int tempLiteralCounter = 0;
    private int tempLabelCounter = 0;
    private int tempRegsNumber = 8;
    private int tempfRegsNumber = 0;
    List<String> regs = Arrays.asList(
            "$zero", "$at", //0
            "$v0", "$v1", //2
            "$a0", "$a1", "$a2", "$a3", //4
            "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", //8
            "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", //16
            "$t7", "$t8", "$t9", //23
            "$k0", "$k1", //26
            "$gp", "$sp", "fp", "ra" //28
    );

    List<String> fregs = Arrays.asList(
            "$f0", "$f1", "$f2", "$f3", "$f4", "$f5", "$f6", "$f7", "$f8",
            "$f9", "$f10", "$f11", "$f12", "$f13", "$f14", "$f15", "$f16",
            "$f17", "$f18", "$f19", "$f20", "$f21", "$f22", "$f23", "$f24", "$f25",
            "$f26", "$f27", "$f28", "$f29", "$f30", "$f31"
    );

    private Stack<String> labels = new Stack<>();
    private static String dataSegment = ".data \n\ttrue: .asciiz \"true\"\n\tfalse : .asciiz \"false\"\n\n";
    private static String textSegment = "";

    public CodeGenVisitor(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void visit(ASTNode node) throws Exception {
        switch (node.getNodeType()) {
            case ADDITION:
                visitAdditionNode(node);
                break;
            case SUBTRACTION:
                visitSubtractionNode(node);
                break;
            case MULTIPLICATION:
                visitMultiplicationNode(node);
                break;
            case DIVISION:
                visitDivisionNode(node);
                break;
            case MOD:
                visitModNode(node);
                break;
            case NEGATIVE:
                visitNegative(node);
                break;
            case READ_INTEGER:
                visitReadIntegerNode(node);
                break;
            case READ_LINE:
                visitReadLine(node);
                break;
            case NEW_ARRAY:
                visitNewArrayNode(node);
                break;

            case ITOB:
                visitItoB(node);
                break;
            case ITOD:
                visitItoD(node);
                break;
            case DTOI:
                visitDtoI(node);
                break;
            case BTOI:
                visitBtoI(node);
                break;
            case LVALUE:
                visitLValueNode(node);
                break;
            case CALL:
                visitCallNode(node);
                break;
            case EMPTY_ARRAY:
                break;
            case LESS_THAN:
                visitLessThanNode(node);
                break;
            case LESS_THAN_OR_EQUAL:
                visitLessThanEqualNode(node);
                break;
            case GREATER_THAN:
                visitGreaterThanNode(node);
                break;
            case GREATER_THAN_OR_EQUAL:
                visitGreaterThanEqualNode(node);
                break;
            case EQUAL:
                visitEqualNode(node);
                break;
            case NOT_EQUAL:
                visitNotEqualNode(node);
                break;
            case BOOLEAN_AND:
                visitAndNode(node);
                break;
            case BOOLEAN_NOT:
                visitNotNode(node);
                break;
            case BOOLEAN_OR:
                visitOrNode(node);
                break;
            case BOOLEAN_TYPE:
                node.setSymbolInformation(new SymbolInfo(node, PrimitiveType.BOOL));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case DOUBLE_TYPE:
                node.setSymbolInformation(new SymbolInfo(node, PrimitiveType.DOUBLE));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case INT_TYPE:
                node.setSymbolInformation(new SymbolInfo(node, PrimitiveType.INT));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case STRING_TYPE:
                node.setSymbolInformation(new SymbolInfo(node, PrimitiveType.STRING));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case VOID:
                node.setSymbolInformation(new SymbolInfo(node, PrimitiveType.VOID));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case FIELD_DECLARATION:
                //TODO
                visitAllChildren(node);
                break;
            case VARIABLE_DECLARATION:
                visitVariableDeclaration(node);
                break;
            case METHOD_DECLARATION:
                visitMethodDeclarationNode(node);
                break;
            case CLASS :
            case Class_DECLARATION:
                visitClassDeclarationNode(node);
                break;
            case ASSIGN:
                visitAssignNode(node);
                break;
            case STATEMENT:
                visitStatementNode(node);
                break;
            case STATEMENTS:
                visitStatementsNode(node);
                break;
            case EXPRESSION_STATEMENT:
                visitExpressionNode(node);
                break;
            case BREAK_STATEMENT:
                visitBreakNode(node);
                break;
            case CONTINUE_STATEMENT:
                visitContinueNode(node);
                break;
            case RETURN_STATEMENT:
                visitReturnNode(node);
                break;
            case IF_STATEMENT:
                visitIfStatement(node);
                break;
            case FOR_STATEMENT:
                visitForNode(node);
                break;
            case WHILE_STATEMENT:
                visitWhileNode(node);
                break;
            case PRINT_STATEMENT:
                visitPrintNode(node);
                break;
            case LITERAL:
                visitLiteralNode(node);
                break;
            case ARGUMENT:
                break;
            case ARGUMENTS:
                visitArgumentsNode(node);
                break;
            case EMPTY_STATEMENT:
                break;
            case IDENTIFIER:
                IdentifierNode idNode = (IdentifierNode) node;
                SymbolInfo ss=new SymbolInfo(node, new IdentifierType(idNode.getValue()));
                ss.value1=idNode.getValue();
                node.setSymbolInformation(ss);
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case LLINE:
                LineNode lnode = (LineNode)node;
                SymbolInfo for_line=new SymbolInfo(node, new LineType(lnode.getValue()));
                for_line.value_for_line=lnode.getValue();
                node.setSymbolInformation(for_line);
                textSegment += "\t\tli " + regs.get(tempRegsNumber) + ", " + for_line.value_for_line + "\n";
                break;
            case METHOD_ACCESS:
                break;
            case PRIVATE_ACCESS:
                break;
            case PUBLIC_ACCESS:
                break;
            case PROTECTED_ACCESS:
                break;
            case VARIABLES:
                visitAllChildren(node);
                //TODO
                break;
            case ACTUALS:
                break;
            case PARAMETER:
                break;
            case PARAMETERS:
                break;
            case BLOCK:
                visitBlockNode(node);
                break;
            case VAR_USE:
                break;
            case Interface:
                break;
            case NULL_LITERAL:
                break;
            case FIELDS:
                visitAllChildren(node);
                //TODO
                break;
            case PROTOTYPES:
                break;
            case PROTOTYPE:
                break;
            case EXPRESSIONS:
                visitAllChildren(node);
                break;
            case START:
                visitStartNode(node);
                break;
            case NEW_IDENTIFIER:
                setParentSymbolInfo(node, node.getChild(0));
                break;
            default:
                visitAllChildren(node);
        }
    }

    private void visitReadLine(ASTNode node) {
        String label = "userInput_" + labelGenerator();
        dataSegment += "\t" + label + ":\t.space\t600\n";
        SymbolInfo si = new SymbolInfo(node, PrimitiveType.INPUTSTRING);
        node.setSymbolInformation(si);
        textSegment += "\t\tli $v0, 8\n\t\tla $a0, " + label + "\n\t\tli $a1, 600\n\t\tsyscall\n";
        textSegment += "\t\tmove $t0, $a0\n\n";
    }

    //**************************************************************

    private void visitContinueNode(ASTNode node) throws Exception {
        if (labels.peek().charAt(labels.peek().length() - 1) == 'F') {
            textSegment += "\t\tj " + labels.peek() + "update\n";
        } else {
            textSegment += "\t\tj " + labels.peek() + "\n";
        }
    }

    //**************************************************************

    private void visitBreakNode(ASTNode node) {
        textSegment += "\t\tj exit" + labels.peek() + "\n";
    }

    private void visitBtoI(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 1)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        node.getSymbolInfo().setType(PrimitiveType.INT);
    }

    //**************************************************************

    private void visitItoB(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 4)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        textSegment += "\t\tbeq $t0 ,0 ItoB" + DtoItoBLabel + "\n";
        textSegment += "\t\tli $t0, 1\n";
        textSegment += "\t\tj exit_ItoB" + DtoItoBLabel + "\n";
        textSegment += "ItoB" + DtoItoBLabel + ":\n";
        textSegment += "\t\tli $t0, 0\n";
        textSegment += "exit_ItoB" + (DtoItoBLabel++) + ":\n";
        node.getSymbolInfo().setType(PrimitiveType.BOOL);
    }

    //**************************************************************

    private void visitDtoI(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 8)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        node.getSymbolInfo().setType(PrimitiveType.INT);
        textSegment += "\t\ts.s $f1, 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";
        textSegment += "\t\ts.s $f2, 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";
        textSegment += "\t\tmov.s $f1, $f0\n";
        textSegment += "\t\tcvt.w.s $f1, $f1\n";
        textSegment += "\t\tmfc1 $t0 $f1\n";
        textSegment += "\t\tmtc1 $t0 $f1\n";
        textSegment += "\t\tcvt.s.w $f1 $f1\n";
        textSegment += "\t\tsub.s $f1, $f0, $f1\n";
        dataSegment += "\t" + symbolTable.getCurrentScopeName() + "_temp" + tempLiteralCounter + ": .float " + "0.5" + "\n";
        textSegment += "\t\tla $a0, " + symbolTable.getCurrentScopeName() + "_temp" + (tempLiteralCounter++) + '\n';
        textSegment += "\t\tl.s $f2, 0($a0)\n";
        textSegment += "\t\tc.eq.s $f1 $f2\n";
        textSegment += "\t\tbc1t " + "half_DtoI" + DtoItoBLabel + "\n";
        textSegment += "\t\tbc1f " + "nhalf_DtoI" + DtoItoBLabel + "\n";
        textSegment += "half_DtoI" + DtoItoBLabel + ":\n";
        textSegment += "\t\tceil.w.s $f0 $f0\n";
        textSegment += "\t\tmfc1 $t0 $f0\n";
        textSegment += "\t\tj exit_DtoI" + DtoItoBLabel + "\n";
        textSegment += "nhalf_DtoI" + DtoItoBLabel + ":\n";
        dataSegment += "\t" + symbolTable.getCurrentScopeName() + "_temp" + tempLiteralCounter + ": .float " + "-0.5" + "\n";
        textSegment += "\t\tla $a0, " + symbolTable.getCurrentScopeName() + "_temp" + (tempLiteralCounter++) + '\n';
        textSegment += "\t\tl.s $f2, 0($a0)\n";
        textSegment += "\t\tc.eq.s $f1 $f2\n";
        textSegment += "\t\tbc1f " + "else_DtoI" + DtoItoBLabel + "\n";
        textSegment += "\t\tcvt.w.s $f0 $f0\n";
        textSegment += "\t\tmfc1 $t0 $f0\n";
        textSegment += "\t\tj exit_DtoI" + DtoItoBLabel + "\n";
        textSegment += "else_DtoI" + DtoItoBLabel + ":\n";
        textSegment += "\t\tround.w.s $f0 $f0\n";
        textSegment += "\t\tmfc1 $t0 $f0\n";
        textSegment += "exit_DtoI" + (DtoItoBLabel++) + ":\n";
        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\tl.s $f2, 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\tl.s $f1, 0($sp)\n";
    }

    //**************************************************************

    private void visitItoD(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 4)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        node.getSymbolInfo().setType(PrimitiveType.DOUBLE);
        textSegment += "\t\tmtc1 $t0 $f0\n";
        textSegment += "\t\tcvt.s.w $f0 $f0\n";
    }

    //**************************************************************

    private void visitOrNode(ASTNode node) throws Exception {
        LogicalOp(node, "or");
    }

    //**************************************************************

    private void LogicalOp(ASTNode node, String op) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 1)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        textSegment += "\t\t" + "move $t1" + ", " + "$t0" + "\n";
        textSegment += "\t\t" + "sw " + "$t1" + ", 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";
        setParentSymbolInfo(node, node.getChild(1));
        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\t" + "lw " + "$t1" + ", 0($sp)\n";
        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo())) {
            textSegment += "\t\t" + op + " $t1, $t1, $t0\n";
        }
        textSegment += "\t\t" + "move $t0, $t1\n";
    }

    //**************************************************************

    private void visitNotNode(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 1)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        textSegment += "\t\t" + "xori $t0, $t0, 1\n";
    }

    //**************************************************************

    private void visitAndNode(ASTNode node) throws Exception {
        LogicalOp(node, "and");
    }

    //**************************************************************

    private void LogicalOp2(ASTNode node, String type) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo first = node.getSymbolInfo();
        int firstType = first.getType().getAlign();
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 4 || node.getChild(0).getSymbolInfo().getType().getAlign() == 8) && (!(type.equals("ne") || type.equals("eq")))) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        int tempReg = firstType != 8 ? tempRegsNumber : tempfRegsNumber;
        List<String> reg = firstType != 8 ? regs : fregs;
        String op = firstType != 8 ? "s" + type + " " : "c." + type + ".s ";
        String op2 = firstType != 8 ? "move " : "mov.s ";
        String op3 = firstType != 8 ? "sw " : "swc1 ";
        String op4 = firstType != 8 ? "lw " : "lwc1 ";
        textSegment += "\t\t" + op2 + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
        textSegment += "\t\t" + op3 + reg.get(tempReg + 1) + ", 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";
        setParentSymbolInfo(node, node.getChild(1));
        SymbolInfo second = node.getSymbolInfo();
        String secondType = second.getType().getSignature();
        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\t" + op4 + reg.get(tempReg + 1) + " 0($sp)\n";
        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo())) {
            if (node.getChild(0).getSymbolInfo().getType().getAlign() == 8) {
                switch (op) {
                    case "c.gt.s ":
                        textSegment += "\t\t" + "c.lt.s " + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
                        break;
                    case "c.ge.s ":
                        textSegment += "\t\t" + "c.le.s " + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
                        break;
                    case "c.ne.s ":
                        textSegment += "\t\t" + "c.eq.s " + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
                        break;
                    default:
                        textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
                }
                textSegment += "\t\tbc1f L_CondFalse" + tempLabelCounter + "\n";
                if (!op.equals("c.ne.s ")) {
                    textSegment += "\t\tli $t0 1\n";
                } else {
                    textSegment += "\t\tli $t0 0\n";
                }
                textSegment += "\t\tj L_CondEnd" + tempLabelCounter + "\n";
                if (!op.equals("c.ne.s ")) {
                    textSegment += "\t\tL_CondFalse" + tempLabelCounter + " : li $t0 0\n";
                } else {
                    textSegment += "\t\tL_CondFalse" + tempLabelCounter + ": li $t0 1\n";
                }
                textSegment += "\t\tL_CondEnd" + tempLabelCounter++ + ":\n";
            }else if(node.getChild(0).getSymbolInfo().getType().getAlign() == 6||node.getChild(0).getSymbolInfo().getType().getAlign() == 12){
               label_counter++;
                textSegment+="loop"+label_counter+":\n" +
                        "\t\tlb $t3($t1)  \n" +
                        "\t\tlb $t4($t0)\n" +
                        "\t\tbeqz $t3,checkt2"+label_counter+" \n" +
                        "\t\tbeqz $t4,missmatch"+label_counter+"\n" +
                        "\t\tslt $t5,$t3,$t4  \n" +
                        "\t\tbnez $t5,missmatch"+label_counter+"\n" +
                        "\t\taddi $t1,$t1,1  \n" +
                        "\t\taddi $t0,$t0,1\n" +
                        "\t\tj loop"+label_counter+"\n" +
                        "\t\tmissmatch"+label_counter+": \n" +
                        "\t\taddi $t1,$zero,1\n" +
                        "\t\tj endfunction"+label_counter+"\n" +
                        "\t\tcheckt2"+label_counter+":\n" +
                        "\t\tbnez $t4,missmatch"+label_counter+"\n" +
                        "\t\tadd $t1,$zero,$zero\n" +
                        "\t\tendfunction"+label_counter+":\n";
            }
            else {
                textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
            }
            node.getSymbolInfo().setType(PrimitiveType.BOOL);
        } else {
            throw new Exception("Type " + firstType + " & " + secondType + " are mismatched");
        }
        textSegment += "\t\t" + op2 + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
    }

    //**************************************************************

    private void visitGreaterThanEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "ge");
    }

    //**************************************************************
    private void visitGreaterThanNode(ASTNode node) throws Exception {
        LogicalOp2(node, "gt");
    }

    //**************************************************************

    private void visitLessThanEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "le");
    }
    //**************************************************************

    private void visitLessThanNode(ASTNode node) throws Exception {
        LogicalOp2(node, "lt");
    }
    //**************************************************************

    private void visitNotEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "ne");
    }
    //**************************************************************

    private void visitEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "eq");
    }

    //**************************************************************

    private void visitNegative(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        int type = 4;
        if (node.getChild(0).getChild(0).getNodeType() == NodeType.LVALUE) {
            IdentifierNode idNode = (IdentifierNode) node.getChild(0).getChild(0).getChild(0);
            String varName = idNode.getValue();
            SymbolInfo varType = (SymbolInfo) symbolTable.get(varName);
            type = varType.getType().getAlign();
        } else if (node.getChild(0).getChild(0).getNodeType() == NodeType.LITERAL) {
            Literal literalNode = (Literal) node.getChild(0).getChild(0);
            type = literalNode.getType().getAlign();
        }
        if (type == 4) {
            textSegment += "\t\tneg $t0, $t0\n";
        } else if (type == 8) {
            textSegment += "\t\tneg.s $f0, $f0\n";
        }
    }

    //**************************************************************

    private void visitNewArrayNode(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        int literalNumber = ((IntegerLiteralNode) node.getChild(0).getChild(0)).getValue();
        //int literalNumber = ((IntegerLiteralNode) node.getChild(1)).getValue();
        //setParentSymbolInfo(node, node.getChild(0));
        node.getSymbolInfo().setDimensionArray(node.getSymbolInfo().getDimensionArray() + 1);
        if (literalNumber <= 0)
            throw new Exception("array size must be greater than zero");
        String label = symbolTable.getCurrentScopeName() + "_NEW_ARRAY_" + arrayNumbers;
        arrayNumbers++;
        dataSegment += "\t" + label + ": .space " + (literalNumber + 1) * 4 + "\n";
        textSegment += "\t\tla $t0, " + label + "\n";
        textSegment += "\t\tli $t2, " + literalNumber + "\n";
        textSegment += "\t\tsw $t2, 0($t0)\n";
    }
    //**************************************************************

    private void visitLiteralNode(ASTNode node) {
        Literal literalNode = (Literal) node;
        node.setSymbolInformation(new SymbolInfo(node, literalNode.getType()));
        switch (literalNode.getType().getAlign()) {
            case 6: //string
                String str = ((StringLiteralNode) literalNode).getValue();
                str = str.replace("\\t", "\\\\t");
                str = str.replace("\\n", "\\\\n");
                String str_raw = str.substring(1, str.length() - 1);
                String label = "";
                if (!stringLiterals.keySet().contains(str_raw)) {
                    label = "StringLiteral_" + stringLiterals.keySet().size() + 1;
                    stringLiterals.put(str_raw, label);
                    stringLiterals_rev.put( label,str_raw);
                    dataSegment += "\t" + label + ": .asciiz " + str + "\n";
                } else
                    label = stringLiterals.get(str_raw);
                textSegment += "\t\tla $t0, " + label + "\n";
                SymbolInfo ssd=new SymbolInfo(node, PrimitiveType.STRING);
                ssd.value = str_raw;
                node.setSymbolInformation(ssd);
                break;
            case 1: //bool
                String bool_type = node.toString().equals("true") ? "1" : "0";
                textSegment += "\t\tli " + regs.get(tempRegsNumber) + ", " + bool_type + "\n";
                break;
            case 4: //int
                textSegment += "\t\tli " + regs.get(tempRegsNumber) + ", " + node + "\n";
                break;
            case 8: //double
                dataSegment += "\t" + symbolTable.getCurrentScopeName() + "_temp" + tempLiteralCounter + ": .float " + node + "\n";
                textSegment += "\t\tla $a0, " + symbolTable.getCurrentScopeName() + "_temp" + (tempLiteralCounter++) + '\n';
                textSegment += "\t\tl.s $f0, 0($a0)\n";
                break;
        }
    }

    //**************************************************************

    private void visitReturnNode(ASTNode node) throws Exception {
        Function method = Function.currentFunction;
        SymbolInfo returnType = method.getReturnType();
        node.getChild(0).accept(this);
        if (!isTypesEqual(returnType, node.getChild(0).getSymbolInfo()))
            throw new Exception("Return type of " + method.getName() + " is incorrect");
        textSegment += "\t\taddi $sp,$sp,-4\n";
        textSegment += "\t\tlw $ra,0($sp)\n";
        textSegment += "\t\tjr $ra\n";
    }

    //**************************************************************

    private void visitCallNode(ASTNode node) throws Exception {
        String varName;
        Function method = null;
        int argNumber = 0;
        String addition="";
        //EDIT TWO
        if (node.getChild(0).getNodeType().equals(NodeType.EXPRESSION_STATEMENT)){
            node.getChild(0).accept(this);
            String check = symbolTable.getCurrentScopeName()+"_"+node.getChild(0).getSymbolInfo().value1;
            if(new_holder.containsKey(check)){
                addition+=new_holder.get(check)+"_";
            }
        }
        for (ASTNode child : node.getChildren()) {
            if (child.getNodeType().equals(NodeType.IDENTIFIER)) {
                IdentifierNode idNode = (IdentifierNode) child;
                varName = idNode.getValue();
                varName=addition+varName;
                method = findFunction(varName);
                if (method == null)
                    throw new Exception(varName + " function doesn't exist");
            }
            if (child.getNodeType().equals(NodeType.ACTUALS)) {
                for (ASTNode childChild : child.getChild(0).getChildren()) {
                    childChild.accept(this);
                    SymbolInfo si = childChild.getSymbolInfo();
                    if (!isTypesEqual(si, method.getArgumentsType().get(argNumber)))
                        throw new Exception("types doesn't match");

                    argNumber++;
                    switch (si.getType().getAlign()) {
                        case 1: //bool
                        case 4: // int
                        case 6: //String
                        case 10:
                            //TODO
                            textSegment += "\t\tsw $t0, 0($sp)\n";
                            textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                            break;
                        case 8: // float
                            textSegment += "\t\tsw $t0, 0($sp)\n";
                            textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                            break;
                        default:
                            break;
                    }
                }

            }

        }

        if (argNumber != method.getArgumentsType().size())
            throw new Exception("expected " + method.getArgumentsType().size() + " args but " + argNumber + " passed");
        textSegment += "\t\tjal " + method.getScope().getName() + "_" + method.getName() + "\n";
        textSegment += "\t\taddi $sp, $sp, " + (argNumber) * (-4) + "\n";
        //symbolTable.enterScope(method.getScope().getName());
        node.setSymbolInformation(method.getReturnType());
    }

    //**************************************************************

    private void visitReadIntegerNode(ASTNode node) {
        SymbolInfo si = new SymbolInfo(node, PrimitiveType.INT);
        node.setSymbolInformation(si);
        textSegment += "\t\tli $v0, 5\n\t\tsyscall\n";
        textSegment += "\t\tmove $t0, $v0\n\n";
    }

    //**************************************************************

    private String labelGenerator() {
        return "L" + (++labelIndex);
    }

    //**************************************************************

    private void visitIfStatement(ASTNode node) throws Exception {
        String ifFalseLabel = labelGenerator();
        tempRegsNumber = 8; // assigning the expStmt into register $t0
        String ifType;
        if (node.getChildren().size() == 2) {
            ifType = "if";
        } else {
            ifType = node.getChildren().size() == 3 ? "if_else" : "invalid";
        }
        //it is if statement, so next child is expStmt which is the 0 child
        node.getChild(0).accept(this);
        if (node.getChild(0).getSymbolInfo().getType().getAlign() == 1) {
            textSegment += "\t\tbeq " + regs.get(tempRegsNumber) + ", 0" + ", " + ifFalseLabel + "\n";
        } else {
            throw new Exception("Invalid Expression in if_exp");
        }
        node.getChild(1).accept(this);
        textSegment += "\t\tj " + ifFalseLabel + "exit" + "\n";
        textSegment += ifFalseLabel + ":\n";
        if (ifType.equals("if_else")) {
            //it is if_else stmt, so the third child must be visited
            node.getChild(2).accept(this);
        } else if (!ifType.equals("if")) {
            throw new Exception("invalid if");
        }
        textSegment += ifFalseLabel + "exit:\n";
    }

    //**************************************************************

    private void visitWhileNode(ASTNode node) throws Exception {
        String label = labelGenerator();
        labels.push(label);
        //while Exp_stmt is the first child of the while statement so,
        textSegment += "\t\t" + label + ":" + "\n";
        node.getChild(0).accept(this);
        textSegment += "\t\tbeq $t0, $zero exit" + label + "\n";
        node.getChild(1).accept(this);
        textSegment += "\t\tj " + label + "\n";
        textSegment += "\t\texit" + label + ":\n";
        labels.pop();
    }

    //**************************************************************

    private void visitForNode(ASTNode node) throws Exception {
        String label = labelGenerator();
        label += "F";
        labels.push(label);
        int n_child = node.getChildren().size();
        if (n_child == 4) {
            node.getChild(0).accept(this);
            textSegment += "\t\t" + label + ":" + "\n";
            node.getChild(1).accept(this);
            textSegment += "\t\tbeq $t0, $zero exit" + label + "\n";
            node.getChild(2).accept(this);
            textSegment += "\t\t" + label + "update:\n";
            node.getChild(3).accept(this);
            textSegment += "\t\tj " + label + "\n";
            textSegment += "\t\texit" + label + ":\n";
        } else if (n_child == 3) {
            if (node.getChild(0).getChild(0).getNodeType().equals(NodeType.ASSIGN)) {
                node.getChild(0).accept(this);
                textSegment += "\t\t" + label + ":" + "\n";
                node.getChild(1).accept(this);
                textSegment += "\t\tbeq $t0, $zero exit" + label + "\n";
                node.getChild(2).accept(this);
                textSegment += "\t\t" + label + "update:\n";
                textSegment += "\t\tj " + label + "\n";
                textSegment += "\t\texit" + label + ":\n";
            } else {
                textSegment += "\t\t" + label + ":" + "\n";
                node.getChild(0).accept(this);
                textSegment += "\t\tbeq $t0, $zero exit" + label + "\n";
                //node.getChild(1).accept(this);
                textSegment += "\t\t" + label + "update:\n";
                node.getChild(2).accept(this);
                node.getChild(1).accept(this);
                textSegment += "\t\tj " + label + "\n";
                textSegment += "\t\texit" + label + ":\n";
            }
        } else if (n_child == 2) {
            textSegment += "\t\t" + label + ":" + "\n";
            node.getChild(0).accept(this);
            textSegment += "\t\tbeq $t0, $zero exit" + label + "\n";
            node.getChild(1).accept(this);
            textSegment += "\t\t" + label + "update:\n";
            textSegment += "\t\tj " + label + "\n";
            textSegment += "\t\texit" + label + ":\n";
        }
        labels.pop();
    }

    //**************************************************************


    private void visitPrintNode(ASTNode node) throws Exception {
        Type exprType = PrimitiveType.INPUTSTRING;
        for (ASTNode child : node.getChild(0).getChildren()) {
            child.accept(this);
            exprType = child.getSymbolInfo().getType();
            System.out.println("type here is: "+exprType.getAlign());
            switch (exprType.getAlign()) {
                case 1: //bool
                    String generatedLabel = labelGenerator();
                    textSegment += "\t\tli $v0, 1\n";
                    textSegment += "\t\tbeq $t0, $zero, printFalse" + generatedLabel + "\n";
                    textSegment +=
                            "\t\tla $t0, true\n" +
                                    "\t\tli $v0, 4\n" +
                                    "\t\tadd $a0, $t0, $zero\n" +
                                    "\t\tsyscall\n" +
                                    "\t\tb endPrintFalse" + generatedLabel + "\n" +
                                    "\tprintFalse" + generatedLabel + ":\n" +
                                    "\t\tla $t0, false\n" +
                                    "\t\tli $v0, 4\n" +
                                    "\t\tadd $a0, $t0, $zero\n" +
                                    "\t\tsyscall\n" +
                                    "\tendPrintFalse" + generatedLabel + ":\n";
                    break;
                case 4: //int
                    textSegment += "\t\tli $v0, 1\n";
                    textSegment += "\t\tadd $a0, $t0, $zero\n";
                    textSegment += "\t\tsyscall\n";
                    break;
                case 6://string
                case 12:
                    if(child.getChild(0).getNodeType()==NodeType.ADDITION){
                        String label = "StringLiteral_" + stringLiterals.keySet().size() + 1;
                        stringLiterals.put(result, label);
                        stringLiterals_rev.put( label,result);
                        dataSegment += "\t\t" + label + ":\t.asciiz\t\"" + result + "\"\n";
                        textSegment += "\tla\t$t0, " + label + '\n';
                    }
                    textSegment += "\t\tli $v0, 4\n";
                    textSegment += "\t\tadd $a0, $t0, $zero\n";
                    textSegment += "\t\tsyscall\n";
                    break;
                case 8://float
                    textSegment += "\t\tli $v0, 2\n";
                    textSegment += "\t\tmov.d\t$f12, $f0\n";
                    textSegment += "\t\tsyscall\n";
                    break;
                default:
                    break;
            }
        }
       // if (exprType.getAlign() != 12) {
        textSegment += "\t\t#print new Line\n";
        textSegment += "\t\taddi $a0, $0, 0xA\n\t\taddi $v0, $0, 0xB\n\t\tsyscall \n";
        //}
    }

    //**************************************************************

    private void visitClassDeclarationNode(ASTNode node) throws Exception {
        //TODO
        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        String className = idNode.getValue();
        System.out.println("class made "+className);
        ClassDecaf classDecaf = new ClassDecaf(className);
        ClassDecaf.currentClass = classDecaf;
        classes.add(classDecaf);
        symbolTable.enterScope(classDecaf.getName());
        if (node.getChild(node.getChildren().size() - 1).getNodeType().equals(NodeType.FIELDS)) {
            node.getChild(node.getChildren().size() - 1).accept(this);
            ClassDecaf.currentClass.setObjectSize(ClassDecaf.currentClass.getFields().size() * 4);
        }
        symbolTable.leaveCurrentScope();
        /*IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        String className = idNode.getValue();
        ClassDecaf.currentClass = findClass(className);
        symbolTable.enterScope(className);
        visitAllChildren(node);
        symbolTable.leaveCurrentScope();*/
    }

    //**************************************************************

    private void visitModNode(ASTNode node) throws Exception {
        ArithmeticOp2(node, "mod");
    }

    //**************************************************************

    private void visitDivisionNode(ASTNode node) throws Exception {
        ArithmeticOp2(node, "div");
    }

    //**************************************************************

    private void visitMultiplicationNode(ASTNode node) throws Exception {
        ArithmeticOp2(node, "mul");
    }

    //**************************************************************

    private void ArithmeticOp2(ASTNode node, String type) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo first = node.getSymbolInfo();
        int firstType = first.getType().getAlign();
        String main_type = type;
        int tempReg = firstType == 4 ? tempRegsNumber : tempfRegsNumber;
        List<String> reg = firstType == 4 ? regs : fregs;
        if (firstType != 4 && type.equals("mod")) {
            throw new Exception("bad parameters for mod operation");
        } else if (type.equals("mod")) {
            main_type = "div";
        }
        if (!(firstType == 4 || firstType == 8 || firstType==6)) {
            throw new Exception("bad parameters for type " + type);
        }
        if (true) {
            String op = firstType == 4 ? main_type + " " : main_type + ".s ";
            String op2 = firstType == 4 ? "move " : "mov.s ";
            String op3 = firstType == 4 ? "sw " : "s.s ";
            String op4 = firstType == 4 ? "lw " : "l.s ";
            textSegment += "\t\t" + op2 + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
            textSegment += "\t\t" + op3 + reg.get(tempReg + 1) + ", 0($sp)\n";
            textSegment += "\t\taddi $sp, $sp, 4\n";
            setParentSymbolInfo(node, node.getChild(1));
            SymbolInfo second = node.getSymbolInfo();
            String secondType = second.getType().getSignature();
            textSegment += "\t\taddi $sp, $sp, -4\n";
            textSegment += "\t\t" + op4 + reg.get(tempReg + 1) + " 0($sp)\n";
            if (isTypesEqual(first, second)) {
                textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
                if (type.equals("mod")) {
                    textSegment += "\t\tmfhi $t1\n";
                }
            } else {
                throw new Exception("Type " + firstType + " & " + secondType + " are mismatched");
            }
            textSegment += "\t\t" + op2 + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
        }else{
            //dataSegment += "\t\t" + stringLabel + ":\t.asciiz\t\"" + dscp.getValue() + "\"\n";
        }
    }

    //**************************************************************

    private void visitSubtractionNode(ASTNode node) throws Exception {
        ArithmeticOp1(node, "sub");
    }

    //**************************************************************

    private void visitAdditionNode(ASTNode node) throws Exception {
        ArithmeticOp1(node, "add");
    }
    //**************************************************************


    private void ArithmeticOp1(ASTNode node, String type) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo first = node.getSymbolInfo();
        int firstType = first.getType().getAlign();
        int tempReg = firstType == 4 ? tempRegsNumber : tempfRegsNumber;
        List<String> reg = firstType == 4 ? regs : fregs;
        if (!(firstType == 4 || firstType == 8||firstType == 6)) {
            throw new Exception("bad parameters for this " + type);
        }
        if(firstType!=6) {
            String op = firstType == 4 ? type + " " : type + ".s ";
            String op2 = firstType == 4 ? "move " : "mov.s ";
            String op3 = firstType == 4 ? "sw " : "s.s ";
            String op4 = firstType == 4 ? "lw " : "l.s ";
            textSegment += "\t\t" + op2 + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
            textSegment += "\t\t" + op3 + reg.get(tempReg + 1) + ", 0($sp)\n";
            textSegment += "\t\taddi $sp, $sp, 4\n";
            setParentSymbolInfo(node, node.getChild(1));
            SymbolInfo second = node.getSymbolInfo();
            String secondType = second.getType().getSignature();
            textSegment += "\t\taddi $sp, $sp, -4\n";
            textSegment += "\t\t" + op4 + reg.get(tempReg + 1) + " 0($sp)\n";
            if (isTypesEqual(first, second)) {
                textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
            } else {
                throw new Exception("Type " + firstType + " & " + secondType + " are mismatched");
            }
            textSegment += "\t\t" + op2 + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
        }else{
            wc=true;
            setParentSymbolInfo(node, node.getChild(1));
            SymbolInfo second = node.getSymbolInfo();
            String secondType = second.getType().getSignature();
            if(first.value!=null &&second.value!=null) {
                String avval = first.value;
                String dovom = second.value;
                String kol = avval + dovom;

                if(result.length()!=0){
                    String h=kol.substring(avval.length());
                    result+=h;
                    //result = result.substring(0,avval.length()-2)+result.substring(avval.length()-1);
                }else{
                    result+=kol;
                }
            }else if(first.value==null&&second.value!=null){
                String avval = stringLiterals_rev.get(which_for_which.get(first.value1));
                String dovom = second.value;
                String label = "StringLiteral_" + stringLiterals.keySet().size() + 1;
                String kol = avval + dovom;
                if(result.length()!=0){
                    String h=kol.substring(avval.length());
                    result+=h;
                }else{
                    result+=kol;
                }
                wc=true;
            }else if(second.value==null&&first.value!=null){
                String avval = stringLiterals_rev.get(which_for_which.get(second.value1));
                String dovom = first.value;
                String label = "StringLiteral_" + stringLiterals.keySet().size() + 1;
                String kol = avval + dovom;
                if(result.length()!=0){
                    String h=kol.substring(avval.length());
                    result+=h;
                    //result = result.substring(0,avval.length()-2)+result.substring(avval.length()-1);
                }else{
                    result+=kol;
                }
                wc=true;
            }else if(first.value==null&&second.value==null){
                String avval = stringLiterals_rev.get(which_for_which.get(first.value1));
                String dovom = stringLiterals_rev.get(which_for_which.get(second.value1));
                String label = "StringLiteral_" + stringLiterals.keySet().size() + 1;
                String kol = avval + dovom;
                if(result.length()!=0){
                    String h=kol.substring(avval.length());
                    result+=h;
                    //result = result.substring(0,avval.length()-2)+result.substring(avval.length()-1);
                }else{
                    result+=kol;
                }
                wc=true;
            }
        }
    }

    //**************************************************************

    private void visitLValueNode(ASTNode node) throws Exception {
        node.getChild(0).accept(this);
        if (node.getChildren().size() == 1) {
            IdentifierNode idNode = (IdentifierNode) node.getChild(0);
            String varName = idNode.getValue();
            SymbolInfo varType = (SymbolInfo) symbolTable.get(varName);
            SymbolInfo si = new SymbolInfo(node, varType.getType());
            si.setDimensionArray(varType.getDimensionArray());
            si.value1=idNode.getValue();
            node.setSymbolInformation(si);
            switch (varType.getType().getAlign()) {
                case 1: //bool
                case 4: // int
                case 6: //String
                    textSegment += "\t\tla $a0, " + findNameOfId(varName) + '\n';
                    textSegment += "\t\tlw $t0, 0($a0)\n";
                    break;
                case 8: // float
                    textSegment += "\t\tla $a0, " + findNameOfId(varName) + '\n';
                    textSegment += "\t\tl.s $f0, 0($a0)\n";
                    break;
                //todo
                default:
                    break;
            }
        } else {
            if (node.getChild(1).getNodeType().equals(NodeType.IDENTIFIER)) {
                //TODO
            } else {
                node.getChild(0).accept(this);
                textSegment += "\t\tmove $a3, $t0\n";
                textSegment += "\t\tmove $s4, $a0\n";
                SymbolInfo varType = node.getChild(0).getSymbolInfo();
                node.getChild(1).accept(this);
                SymbolInfo varType2 = node.getChild(1).getSymbolInfo();
                if (varType2.getType().getAlign() == 4) {//int
                    if (varType.getDimensionArray() > 0) {
                        textSegment += "\t\tli $t4, 4\n";
                        textSegment += "\t\taddi $t0, $t0, 1\n";
                        textSegment += "\t\tlw $t2, 0($a3)\n";
                        textSegment += "\t\tblt $t2, $t0, runtime_error\n";
                        textSegment += "\t\tmul $t0, $t0, $t4\n";
                        textSegment += "\t\tadd $a0, $a3, $t0\n";
                        textSegment += "\t\tlw $t0, 0($a0)\n";
                    } else
                        throw new Exception("error in array assign - type is not array");
                } else
                    throw new Exception("error in array assign - index array");

                SymbolInfo si = new SymbolInfo(node, varType.getType());
                si.setDimensionArray(varType.getDimensionArray() - 1);
                node.setSymbolInformation(si);
            }
        }

    }

    //**************************************************************

    private void visitAssignNode(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo varType = node.getChild(0).getSymbolInfo();
        current_id=varType.value1;
        textSegment += "\t\tla $a3, 0($a0) \n";
        //node.getChild(1).accept(this);
        setParentSymbolInfo(node, node.getChild(1));
        SymbolInfo exprType = node.getChild(1).getSymbolInfo();
        if(node.getChild(1).getChild(0).getNodeType()==NodeType.LITERAL){
            String label = "StringLiteral_" + (stringLiterals.keySet().size() -1)+ 1;
            which_for_which.put(varType.value1,label);
        }else{
            if(wc){
                String label = "StringLiteral_" + stringLiterals.keySet().size() + 1;
                stringLiterals.put(result, label);
                stringLiterals_rev.put( label,result);
                dataSegment += "\t\t" + label + ":\t.asciiz\t\"" + result + "\"\n";
                textSegment += "\tla\t$t0, " + label + '\n';
                which_for_which.put(current_id,label);
                wc=false;
            }

        }
        if (exprType == null)
            throw new Exception("Assign Error");
        //TODO
        if (isTypesEqual(varType, exprType)) {
            switch (varType.getType().getAlign()) {
                case 6: //string
                case 1: //bool
                case 4: // int
                    textSegment += "\t\tsw $t0, 0($a3)\n";
                    break;
                case 8: // float
                    textSegment += "\t\ts.s $f0, 0($a3)\n";
                    break;
                case 10:
                    //new_holder.put(symbolTable.getCurrentScopeName()+"_"+current_id,varType.for_new);
                    //todo
                    break;
                default:
                    break;
            }
        } else {
            throw new Exception("Type " + varType + " & " + exprType + " Doesnt Match"+" "+varType.value1);
        }
        current_id="";
        result="";

    }

    //**************************************************************

    private void visitExpressionNode(ASTNode node) throws Exception {
        tempRegsNumber = 8;
        setParentSymbolInfo(node, node.getChild(0));
    }

    //**************************************************************

    private void visitStatementNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    //**************************************************************

    private void visitStatementsNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    //**************************************************************

    private void visitArgumentsNode(ASTNode node) throws Exception {
        int argumentsLen = node.getChildren().size() * (-4);
        Function function = Function.currentFunction;
        if (argumentsLen < 0)
            textSegment += "\t\taddi $sp,$sp," + argumentsLen + "\n";
        for (int i = argumentsLen / (-4); i >= 1; i--) {
            ASTNode ArgumentNode = node.getChild(i - 1);
            ArgumentNode.getChild(0).accept(this);
            IdentifierNode idNode = (IdentifierNode) ArgumentNode.getChild(0).getChild(1);
            String idName = idNode.getValue();
            SymbolInfo si = function.getArgumentsType().get(i - 1);
            switch (si.getType().getAlign()) {
                case 1: //bool
                case 4: // int
                case 6: //String
                case 10:
                    //TODO
                    textSegment += "\t\tla $a1, " + findNameOfId(idName) + '\n';
                    textSegment += "\t\tlw $t1, 0($sp)\n";
                    textSegment += "\t\tsw $t1, 0($a1)\n";
                    textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                    break;
                case 8: // float
                    textSegment += "\t\tla $a1, " + findNameOfId(idName) + '\n';
                    textSegment += "\t\tl.s $f1, 0($sp)\n";
                    textSegment += "\t\ts.s $f1, 0($a1)\n";
                    textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                    break;
                default:
                    break;
            }
        }

    }

    //**************************************************************

    private void visitStartNode(ASTNode node) throws Exception {
        dataSegment += "\terror_run_time: .asciiz \"runtime ERROR\"\n";
        textSegment += ".text\n" + "\t.globl main\n\n";
        textSegment += "\tmain:\n";
        textSegment += "\t\tjal global_main\n";
        textSegment += "\t\t#END OF PROGRAM\n";
        textSegment += "\t\tli $v0,10\n\t\tsyscall\n";
        textSegment += "\truntime_error:\n";
        textSegment += "\t\tli $v0, 4\n";
        textSegment += "\t\tla $a0, error_run_time\n";
        textSegment += "\t\tsyscall\n";
        textSegment += "\t\t#END OF PROGRAM\n";
        textSegment += "\t\tli $v0,10\n\t\tsyscall\n";
        symbolTable.enterScope("global");
        visitAllChildren(node);
        stream.print(dataSegment + '\n' + textSegment);
    }
    //**************************************************************

    private void visitBlockNode(ASTNode node) throws Exception {
        if (node.getParent().getNodeType() != NodeType.METHOD_DECLARATION) {
            symbolTable.enterScope("block_" + blockIndex++);
            visitAllChildren(node);
            symbolTable.leaveCurrentScope();
        } else {
            visitAllChildren(node);
        }
    }
    //**************************************************************

    private void visitVariableDeclaration(ASTNode node) throws Exception {
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String varName = idNode.getValue();
        String label = symbolTable.getCurrentScopeName() + "_" + varName + " :";
        setParentSymbolInfo(node, node.getChild(0));
        if(node.getChild(0).getSymbolInfo().getType().getAlign()==10){
            setParentSymbolInfo(node, node.getChild(1));
        }else{
            node.getChild(1).accept(this);
        }
        node.getChild(1).getSymbolInfo().for_new=node.getChild(0).getSymbolInfo().value1;
        new_holder.put(symbolTable.getCurrentScopeName()+"_"+node.getChild(1).getSymbolInfo().value1,node.getChild(1).getSymbolInfo().for_new);
        int dimensionArray = node.getSymbolInfo().getDimensionArray();
        if (!node.getChild(0).getNodeType().equals(NodeType.IDENTIFIER)) {
            Type typePrimitive = node.getSymbolInfo().getType();
            if (dimensionArray == 0 && !typePrimitive.getSignature().equals(".ascii")) {
                dataSegment += "\t" + label + " " + typePrimitive.getSignature() + " " + typePrimitive.getPrimitive().getInitialValue() + "\n";
            }else {
                dataSegment += "\t" + label + " .word 0" + "\n";
            }
        } else {
            IdentifierNode typeNode = (IdentifierNode) node.getChild(0);
            String typeName = typeNode.getValue();
            ClassDecaf classDecaf = findClass(typeName);
            if (classDecaf == null)
                throw new Exception(typeName + " class not Declared");
            dataSegment += "\t" + label + "\t" + ".space" + "\t" + classDecaf.getObjectSize() + "\n";
        }
        if (ClassDecaf.currentClass == null || !symbolTable.getCurrentScopeName().equals(ClassDecaf.currentClass.getName())){
            symbolTable.put(varName, node.getSymbolInfo());
        }else{
            symbolTable.putsss(varName, node.getSymbolInfo());
        }


       /* if (ClassDecaf.currentClass == null || !symbolTable.getCurrentScopeName().equals(ClassDecaf.currentClass.getName())) {
            IdentifierNode idNode = (IdentifierNode) node.getChild(1);
            String varName = idNode.getValue();
            String label = symbolTable.getCurrentScopeName() + "_" + varName + " :";
            setParentSymbolInfo(node, node.getChild(0));
            if(node.getChild(0).getSymbolInfo().getType().getAlign()==10){
                setParentSymbolInfo(node, node.getChild(1));
            }else{
                node.getChild(1).accept(this);
            }
            node.getChild(1).getSymbolInfo().for_new=node.getChild(0).getSymbolInfo().value1;
            new_holder.put(symbolTable.getCurrentScopeName()+"_"+node.getChild(1).getSymbolInfo().value1,node.getChild(1).getSymbolInfo().for_new);
            int dimensionArray = node.getSymbolInfo().getDimensionArray();
            if (!node.getChild(0).getNodeType().equals(NodeType.IDENTIFIER)) {
                Type typePrimitive = node.getSymbolInfo().getType();
                if (dimensionArray == 0 && !typePrimitive.getSignature().equals(".ascii")) {
                    dataSegment += "\t" + label + " " + typePrimitive.getSignature() + " " + typePrimitive.getPrimitive().getInitialValue() + "\n";
                }else {
                    dataSegment += "\t" + label + " .word 0" + "\n";
                }
            } else {
                IdentifierNode typeNode = (IdentifierNode) node.getChild(0);
                String typeName = typeNode.getValue();
                ClassDecaf classDecaf = findClass(typeName);
                if (classDecaf == null)
                    throw new Exception(typeName + " class not Declared");
                dataSegment += "\t" + label + "\t" + ".space" + "\t" + classDecaf.getObjectSize() + "\n";
            }
            symbolTable.put(varName, node.getSymbolInfo());
        }*/

    }

    //**************************************************************
    private void visitMethodDeclarationNode(ASTNode node) throws Exception {
        node.getChild(0).accept(this);
        SymbolInfo returnType = node.getChild(0).getSymbolInfo();
        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String methodName = idNode.getValue();
        Function method_temp = new Function(methodName, returnType, symbolTable.getCurrentScope());
        for (Function function : functions) {
            if (function.equals(method_temp)) {
                Function.currentFunction = function;
                break;
            }
        }
        String label = symbolTable.getCurrentScopeName() + "_" + methodName;
        textSegment += "\t" + label + ":\n";
        symbolTable.enterScope(label);
        textSegment += "\t\tsw $ra,0($sp)\n";
        node.getChild(2).accept(this);
        textSegment += "\t\taddi $sp,$sp,4\n";
        node.getChild(3).accept(this);
        textSegment += "\t\taddi $sp,$sp,-4\n";
        textSegment += "\t\tlw $ra,0($sp)\n";
        textSegment += "\t\tjr $ra\n";
        symbolTable.leaveCurrentScope();
    }
    //**************************************************************

    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    //**************************************************************
    private boolean isTypesEqual(SymbolInfo a, SymbolInfo b) {
        if(a.getType().getSignature().equals(".ascii")&&b.getType().getSignature().equals(".space")){
            return true;
        }
        if(a.getType().getSignature().equals(".space")&&b.getType().getSignature().equals(".ascii")){
            return true;
        }
        if (a.getType().getAlign() == b.getType().getAlign()) {
            if (a.getType().getSignature().equals(b.getType().getSignature())) {
                if (a.getDimensionArray() == b.getDimensionArray())
                    if(a.getType().getPrimitive()!=null){
                        return a.getType().getPrimitive().equals(b.getType().getPrimitive());
                    }
            }else{
                if(a.getType().getAlign()==10&&b.getType().getAlign()==10){
                    if (new_holder.containsKey(symbolTable.getCurrentScopeName()+"_"+a.value1)){
                        return new_holder.get(symbolTable.getCurrentScopeName()+"_"+a.value1).equals(b.value1);
                    }
                }
            }
        }

        return false;
    }

    //**************************************************************

    private String findNameOfId(String id)throws Exception {
        return symbolTable.getScopeNameOfIdentifier(id) + "_" + id;
    }

    //**************************************************************

    private Function findFunction(String varName) {
        Function holder_method=null;
        Function method = null;
        for (Function function : functions) {
            if (function.getName().equals(varName)) {
                //////EDIT ONE
                if (function.getScope().getName().equals("global")){
                    holder_method = function;
                }else{
                    String taeen_konnade=symbolTable.getCurrentScopeName().split("_")[0];
                    if(symbolTable.getCurrentScopeName().split("_").length>2){
                        taeen_konnade +=symbolTable.getCurrentScopeName().split("_")[1];
                    }
                    if(function.getScope().getName().equals(taeen_konnade)){
                        method = function;
                    }
                }
                /* for (Scope scope : all_scopes) {
                    if (scope.equals(function.getScope())) {
                        method = function;
                        break;
                    }
                }*/
                //if (method != null)
                    //break;
            }else{
                if((function.getScope().getName()+"_"+function.getName()).equals(varName)){
                    method = function;
                }
            }
            if(method!=null){
                return method;
            }
        }
        return holder_method;
    }

    //**************************************************************

    private void setParentSymbolInfo(ASTNode node, ASTNode child) throws Exception {
        child.accept(this);
        Type type = child.getSymbolInfo().getType();
        SymbolInfo si = new SymbolInfo(node, type);
        si.setDimensionArray(child.getSymbolInfo().getDimensionArray());
        si.value = child.getSymbolInfo().value;
        si.value1=child.getSymbolInfo().value1;
        si.for_new=child.getSymbolInfo().for_new;
        si.value_for_line=child.getSymbolInfo().value_for_line;
        node.setSymbolInformation(si);
    }
    //**************************************************************

    private ClassDecaf findClass(String name) {
        for (ClassDecaf aClass : classes) {
            if (aClass.getName().equals(name))
                return aClass;
        }
        return null;
    }
}

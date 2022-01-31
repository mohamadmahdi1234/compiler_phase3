package compiler.codegen;

import compiler.AST.ASTNode;

public interface SimpleVisitor {
    void visit(ASTNode node) throws Exception;
}

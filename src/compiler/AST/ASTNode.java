package compiler.AST;


import compiler.codegen.SymbolInfo;
import compiler.codegen.SimpleVisitor;

import java.util.List;

/**
 * The Abstraction of node for ast tree
 */
public interface ASTNode {

    /**
     * this method returns type of node
     */
    NodeType getNodeType();

    /**
     * Sets the symbol info.
     */
    void setSymbolInformation(SymbolInfo sim_inf);

    /**
     * Gets the symbol info.
     */
    SymbolInfo getSymbolInfo();

    /**
     * Accepts a simple visitor.
     */
    void accept(SimpleVisitor visitor) throws Exception;

    /**
     * Adds a node to the end of the list of children.
     */
    void addChild(ASTNode node);

    /**
     * Adds a node to the list of children at the specified location.
     */
    void addChild(int index, ASTNode node);

    /**
     * Adds a list of nodes to the end of the list of children.
     */
    void addChildren(List<ASTNode> nodes);

    /**
     * Adds a list of nodes to the end of the list of children.
     */
    void addChild(ASTNode... nodes);

    void setChildren(ASTNode... nodes);
    /**
     * Returns the list of children.
     */
    List<ASTNode> getChildren();

    /**
     * Returns the child at the specified location.
     */
    ASTNode getChild(int index);

    void setParent(ASTNode parent);

    ASTNode getParent();
}
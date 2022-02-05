package compiler.AST;

public class func_node extends BaseASTNode{
    private String value;

    public func_node(String value) {
        super(NodeType.FUNC);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

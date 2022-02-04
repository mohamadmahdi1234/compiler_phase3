package compiler.AST;

public class LineNode extends BaseASTNode{
    private int value;

    public LineNode(int value) {
        super(NodeType.LLINE);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

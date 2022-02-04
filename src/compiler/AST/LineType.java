package compiler.AST;

public class LineType implements Type {
    private int vvalue;
    private int align=4;

    public LineType(int vvalue) {
        this.vvalue = vvalue;
    }

    public int getVvalue() {
        return vvalue;
    }

    @Override
    public String getSignature() {
        return ".word";
    }

    @Override
    public int getAlign() {
        return align;
    }

    @Override
    public PrimitiveType getPrimitive() {
        return PrimitiveType.INT;
    }
}

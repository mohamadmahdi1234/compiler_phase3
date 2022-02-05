package compiler.AST;

public class func_type implements Type{
    private String vvalue;
    private int align=6;

    public func_type(String vvalue) {
        this.vvalue = vvalue;
    }

    public String getVvalue() {
        return vvalue;
    }

    @Override
    public String getSignature() {
        return ".ascii";
    }

    @Override
    public int getAlign() {
        return align;
    }

    @Override
    public PrimitiveType getPrimitive() {
        return PrimitiveType.STRING;
    }
}

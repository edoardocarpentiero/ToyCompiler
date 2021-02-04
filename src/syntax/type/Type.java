package syntax.type;

import visitor.Element;
import visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ASTNode;

public class Type extends ASTNode {
    private TypesVar valueType;

    public Type(Location left, Location right,TypesVar valueType) {
        super(left, right);
        this.valueType = valueType;
    }

    public Type(TypesVar valueType) {
        this.valueType = valueType;
    }

    public TypesVar getValueType() {
        return valueType;
    }

    public void setValueType(TypesVar valueType) {
        this.valueType = valueType;
    }


    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) {
        return visitor.visit(this,arg);
    }
}

package syntax.expr.ExprValue;

import exception.CompilerException;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class ExprOpFloatConst extends Expr  {

    private float value;

    public ExprOpFloatConst(Location left, Location right,float value) {
        super(left, right);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

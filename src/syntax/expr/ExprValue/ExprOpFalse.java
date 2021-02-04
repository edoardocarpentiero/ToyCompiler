package syntax.expr.ExprValue;

import exception.CompilerException;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class ExprOpFalse extends Expr {

    private boolean value;

    public ExprOpFalse(Location left, Location right,boolean value) {
        super(left, right);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

package syntax.expr.ExprValue;

import exception.CompilerException;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class ExprOpIntConst  extends Expr  {

    private int value;

    public ExprOpIntConst(Location left, Location right,int value) {
        super(left, right);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

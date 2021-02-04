package syntax.expr.ExprValue;

import exception.CompilerException;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class ExprOpStringConst extends Expr  {

    private String value;

    public ExprOpStringConst(Location left, Location right, String value) {
        super(left, right);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }

}

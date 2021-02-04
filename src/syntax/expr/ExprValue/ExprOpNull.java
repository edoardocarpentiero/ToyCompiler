package syntax.expr.ExprValue;

import exception.CompilerException;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class ExprOpNull extends Expr {


    public ExprOpNull(Location left, Location right) {
        super(left, right);
    }



    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }


}

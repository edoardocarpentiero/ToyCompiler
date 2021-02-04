package syntax.expr.ExprRelopOp;

import exception.CompilerException;
import syntax.expr.Expr;
import syntax.expr.ExprBinaryOp;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class ExprOpRelopEQ extends ExprBinaryOp {

    public ExprOpRelopEQ(Location left, Location right, Expr value1, Expr value2) {
        super(left,right,value1, value2);
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

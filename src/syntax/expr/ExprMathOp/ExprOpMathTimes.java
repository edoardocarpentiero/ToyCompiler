package syntax.expr.ExprMathOp;

import exception.CompilerException;
import syntax.expr.ExprBinaryOp;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class ExprOpMathTimes extends ExprBinaryOp {

    public ExprOpMathTimes(Location left, Location right,Expr value1, Expr value2) {
        super(left, right, value1, value2);
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }


}

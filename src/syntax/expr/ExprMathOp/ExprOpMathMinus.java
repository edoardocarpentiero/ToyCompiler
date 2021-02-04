package syntax.expr.ExprMathOp;

import exception.CompilerException;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.Expr;
import syntax.expr.ExprUnaryOp;
import visitor.Visitor;

public class ExprOpMathMinus extends ExprUnaryOp {

    public ExprOpMathMinus(Location left, Location right,Expr expr) {
        super(left, right, expr);
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

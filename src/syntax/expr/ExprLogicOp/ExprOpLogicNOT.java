package syntax.expr.ExprLogicOp;

import exception.CompilerException;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.ExprUnaryOp;
import visitor.Visitor;

public class ExprOpLogicNOT extends ExprUnaryOp  {


    public ExprOpLogicNOT(Location left, Location right,Expr expr) {
        super(left,right,expr);
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

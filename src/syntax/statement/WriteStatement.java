package syntax.statement;

import exception.CompilerException;
import syntax.expr.Expr;

import java.util.ArrayList;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class WriteStatement extends Statement {

    private ArrayList<Expr> exprList;

    public WriteStatement(Location left, Location right, ArrayList<Expr> exprList) {
        super(left, right);
        this.exprList = exprList;
    }

    public ArrayList<Expr> getExprList() {
        return exprList;
    }

    public void setExprList(ArrayList<Expr> exprList) {
        this.exprList = exprList;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

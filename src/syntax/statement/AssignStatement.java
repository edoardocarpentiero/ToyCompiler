package syntax.statement;

import exception.CompilerException;
import visitor.Visitor;
import syntax.expr.Expr;
import syntax.expr.Identifier;

import java.util.ArrayList;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class AssignStatement extends Statement {

    private ArrayList<Identifier> idList;
    private ArrayList<Expr> exprList;

    public AssignStatement(Location left, Location right, ArrayList<Identifier> idList, ArrayList<Expr> exprList) {
        super(left,right);
        this.idList = idList;
        this.exprList = exprList;
    }

    public ArrayList<Identifier> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<Identifier> idList) {
        this.idList = idList;
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

package syntax.statement;

import exception.CompilerException;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.Expr;
import visitor.Visitor;

import java.util.ArrayList;

public class ElifStatement extends Statement {
    private Expr expr;
    private ArrayList<Statement> statList;


    public ElifStatement(Location left, Location right, Expr expr,ArrayList<Statement> statList) {
        super(left, right);
        this.statList = statList;
        this.expr = expr;

    }

    public Expr getCondition() {
        return expr;
    }

    public void setCondition(Expr expr) {
        this.expr = expr;
    }

    public ArrayList<Statement> getStatList() {
        return statList;
    }

    public void setStatList(ArrayList<Statement> statList) {
        this.statList = statList;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

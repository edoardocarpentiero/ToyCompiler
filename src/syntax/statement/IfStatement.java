package syntax.statement;
import exception.CompilerException;
import visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.Expr;

import java.util.ArrayList;

public class IfStatement extends Statement{
    private Expr exprValue;
    private ArrayList<Statement> statList;
    private ArrayList<ElifStatement>  elifListStatement;
    private ElseStatement elseStatement;


    public IfStatement(Location left, Location right, ArrayList<Statement> statList, Expr exprValue, ArrayList<ElifStatement> elifListStatement,ElseStatement elseStatement ) {
        super(left, right);
        this.statList = statList;
        this.exprValue = exprValue;
        this.elifListStatement=elifListStatement;
        this.elseStatement= elseStatement;
    }

    public Expr getCondition() {
        return exprValue;
    }

    public void setCondition(Expr exprValue) {
        this.exprValue = exprValue;
    }

    public ArrayList<Statement> getThenStatement() {
        return statList;
    }

    public void setThenStatement(ArrayList<Statement> statList) {
        this.statList = statList;
    }

    public ArrayList<ElifStatement> getElifListStatement() {
        return elifListStatement;
    }

    public void setElifListStatement(ArrayList<ElifStatement> elifListStatement) {
        this.elifListStatement = elifListStatement;
    }

    public ElseStatement getElseStatement() {
        return elseStatement;
    }

    public void setElseStatement(ElseStatement elseStatement) {
        this.elseStatement = elseStatement;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}




package syntax.statement;
import exception.CompilerException;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.Expr;
import visitor.Visitor;

import java.util.ArrayList;

public class WhileStatement extends Statement {
    private ArrayList<Statement> statList;
    private ArrayList<Statement> statListDo;
    private Expr condition;

    public WhileStatement(Location left, Location right, ArrayList<Statement> statList, Expr condition, ArrayList<Statement> statListDo) {
        super(left, right);
        this.statList = statList;
        this.statListDo = statListDo;
        this.condition = condition;
    }

    public WhileStatement(Location left, Location right, Expr condition, ArrayList<Statement> statListDo ) {
        super(left, right);
        this.statListDo = statListDo;
        this.condition = condition;
    }

    public ArrayList<Statement> getStatList() {
        return statList;
    }

    public void setStatList(ArrayList<Statement> statList) {
        this.statList = statList;
    }

    public ArrayList<Statement> getStatListDo() {
        return statListDo;
    }

    public void setStatListDo(ArrayList<Statement> statListDo) {
        this.statListDo = statListDo;
    }

    public Expr getCondition() {
        return condition;
    }

    public void setCondition(Expr expr) {
        this.condition = expr;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

package syntax.procedure;

import exception.CompilerException;
import syntax.type.TypesProcedure;
import syntax.expr.Expr;

import java.util.ArrayList;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.Identifier;
import visitor.Visitor;

public class CallProc extends Expr  {

    private ArrayList<Expr> exprList;
    private Identifier id;
    private TypesProcedure typesProcedure;

    public CallProc(Location left, Location right,Identifier id, ArrayList<Expr> exprList) {
        super(left, right);
        this.id= id;
        this.exprList = exprList;
    }

    public CallProc(Location left, Location right, Identifier id) {
        super(left, right);
        this.id = id;
    }

    public Identifier getIdValue() {
        return id;
    }

    public void setIdValue(Identifier idValue) {
        this.id = idValue;
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

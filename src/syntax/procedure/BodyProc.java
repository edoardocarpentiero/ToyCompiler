package syntax.procedure;

import exception.CompilerException;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ASTNode;
import syntax.VarDecl;
import syntax.expr.Expr;
import syntax.statement.Statement;
import visitor.Visitor;

import java.util.ArrayList;

public class BodyProc extends ASTNode  {


    private ArrayList<VarDecl> varDeclList;
    private ArrayList<Expr> returnExprs;
    private ArrayList<Statement> statList;

    public BodyProc(Location left, Location right, ArrayList<VarDecl> varDeclList, ArrayList<Statement> statList, ArrayList<Expr> returnExprs) {
        super(left, right);
        this.varDeclList=varDeclList;
        this.returnExprs=returnExprs;
        this.statList=statList;
    }

    public BodyProc(Location left, Location right, ArrayList<VarDecl> varDeclList, ArrayList<Expr> returnExprs) {
        super(left, right);
        this.varDeclList=varDeclList;
        this.returnExprs=returnExprs;
    }

    public ArrayList<VarDecl> getVarDeclList() {
        return varDeclList;
    }

    public void setVarDeclList(ArrayList<VarDecl> varDeclList) {
        this.varDeclList = varDeclList;
    }

    public ArrayList<Expr> getReturnExprs() {
        return returnExprs;
    }

    public void setReturnExprs(ArrayList<Expr> returnExprs) {
        this.returnExprs = returnExprs;
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

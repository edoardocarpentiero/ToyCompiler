package syntax;

import exception.CompilerException;
import visitor.Visitor;
import syntax.procedure.Proc;

import java.util.ArrayList;
import java_cup.runtime.ComplexSymbolFactory.Location;
public class Program extends ASTNode {

    private ArrayList<VarDecl> varDecls;
    private ArrayList<Proc> procList;

    public Program(Location left, Location right,ArrayList<VarDecl> varDecls, ArrayList<Proc> procList) {
        super(left, right);
        this.varDecls = varDecls;
        this.procList = procList;
    }

    public ArrayList<VarDecl> getVarDecls() {
        return varDecls;
    }

    public void setVarDecls(ArrayList<VarDecl> varDecls) {
        this.varDecls = varDecls;
    }

    public ArrayList<Proc> getProcList() {
        return procList;
    }

    public void setProcList(ArrayList<Proc> procList) {
        this.procList = procList;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
       return visitor.visit(this,arg);
    }
}

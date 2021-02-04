package syntax.procedure;

import exception.CompilerException;
import visitor.Element;
import visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ASTNode;
import syntax.expr.Identifier;
import syntax.type.ResultType;

import java.util.ArrayList;


public class Proc  extends ASTNode implements Element {
    private Identifier identifier;
    private ArrayList<ResultType> resultTypeList;
    private BodyProc bodyProc;
    private ArrayList<ParDecl> paramDeclList;


    public Proc(Location left, Location right, Identifier identifier, ArrayList<ParDecl> paramDeclList, ArrayList<ResultType> resultTypeList, BodyProc bodyProc) {
        super(left, right);
        this.identifier=identifier;
        this.resultTypeList=resultTypeList;
        this.paramDeclList=paramDeclList;
        this.bodyProc=bodyProc;
    }

    public Proc(Location left, Location right, Identifier identifier, ArrayList<ResultType> resultTypeList,  BodyProc bodyProc) {
        super(left, right);
        this.identifier=identifier;
        this.resultTypeList=resultTypeList;
        this.bodyProc=bodyProc;
    }


    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public ArrayList<ResultType> getResultTypeList() {
        return resultTypeList;
    }

    public BodyProc getBodyProc() {
        return bodyProc;
    }

    public void setBodyProc(BodyProc bodyProc) {
        this.bodyProc = bodyProc;
    }

    public void setResultTypeList(ArrayList<ResultType> resultTypeList) {
        this.resultTypeList = resultTypeList;
    }


    public ArrayList<ParDecl> getParamDeclList() {
        return paramDeclList;
    }

    public void setParamDeclList(ArrayList<ParDecl> paramDeclList) {
        this.paramDeclList = paramDeclList;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

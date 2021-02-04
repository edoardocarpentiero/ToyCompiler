package syntax;


import exception.CompilerException;
import visitor.Visitor;
import syntax.initId.IdInit;
import syntax.type.Type;
import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;

public class VarDecl extends ASTNode {
    private Type type;
    private ArrayList<IdInit> idListInit;

    public VarDecl(Location left, Location right,Type type, ArrayList<IdInit> idListInit) {
        super(left, right);
        this.type = type;
        this.idListInit = idListInit;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<IdInit> getIdListInit() {
        return idListInit;
    }

    public void setIdListInit(ArrayList<IdInit> idListInit) {
        this.idListInit = idListInit;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

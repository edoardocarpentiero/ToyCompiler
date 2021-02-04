package syntax.procedure;
import exception.CompilerException;
import visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ASTNode;
import syntax.expr.Identifier;
import syntax.type.Type;

import java.util.ArrayList;

public class ParDecl extends ASTNode {
    private Type type;
    private ArrayList<Identifier> identifiers;
    public ParDecl(Location left, Location right, Type type, ArrayList<Identifier> identifiers) {
        super(left, right);
        this.type = type;
        this.identifiers=identifiers;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ArrayList<Identifier> identifiers) {
        this.identifiers = identifiers;
    }



    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

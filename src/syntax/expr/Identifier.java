package syntax.expr;

import exception.CompilerException;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;

public class Identifier extends Expr {

    private String id;


    public Identifier(Location left, Location right, String id) {

        super(left, right);
        this.id = id;
    }

    public Identifier(String id){
        this.id=id;
    }


    public String getValue() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

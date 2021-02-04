package syntax.initId;
import exception.CompilerException;
import syntax.ASTNode;
import syntax.expr.Expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.Identifier;
import visitor.Element;
import visitor.Visitor;


public class IdInit extends ASTNode implements Element {
    private Identifier id;
    private Expr expr;

    public IdInit(Location left, Location right, Identifier id) {
        super(left,right);
        this.id = id;
    }

    public IdInit(Location left, Location right) {
        super(left,right);
    }

    public IdInit(Location left, Location right, Identifier id, Expr expr){
        super(left,right);
        this.id = id;
        this.expr = expr;
    }



    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }


    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

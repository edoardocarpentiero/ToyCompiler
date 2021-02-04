package syntax.type;
import visitor.Element;
import visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ASTNode;

public class ResultType extends ASTNode {

    private Type type;
    public ResultType(Location left, Location right, Type type) {
        super(left, right);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type typeVar) {
        this.type = typeVar;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) {
        return visitor.visit(this,arg);
    }
}

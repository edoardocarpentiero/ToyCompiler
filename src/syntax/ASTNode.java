package syntax;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.type.TypesVar;
import visitor.Element;

public abstract class ASTNode implements Element {

    private Location left, right;


    public ASTNode(Location left, Location right) {
        this.left = left;
        this.right = right;
    }

    public ASTNode(){ }

    public Location getLeft() {
        return left;
    }

    public void setLeft(Location left) {
        this.left = left;
    }

    public Location getRight() {
        return right;
    }

    public void setRight(Location right) {
        this.right = right;
    }


}

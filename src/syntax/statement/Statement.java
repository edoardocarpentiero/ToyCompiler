package syntax.statement;

import visitor.Element;
import visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ASTNode;

public abstract class Statement extends ASTNode {
    public Statement(Location left, Location right) {
        super(left, right);
    }

    public Statement(){};




}

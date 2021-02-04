package syntax.expr;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.type.NodeType;
import syntax.statement.Statement;


public abstract class Expr extends Statement implements NodeType {


    private NodeType nodeType;

    public Expr(Location left, Location right) {
        super(left, right);
    }

    public Expr(){}

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }



}

package syntax.initId;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ASTNode;
import syntax.expr.Expr;
import syntax.expr.Identifier;

public class IdInitAssign extends IdInit {


    public IdInitAssign(Location left, Location right, Identifier id, Expr expr) {
        super(left,right,id,expr);

    }


}

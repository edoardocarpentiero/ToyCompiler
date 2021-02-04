package syntax.expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
public abstract class ExprUnaryOp extends Expr{
    private Expr operand;

    public ExprUnaryOp(Location left, Location right, Expr operand) {
        super(left, right);
        this.operand = operand;
    }



    public Expr getOperand() {
        return operand;
    }

    public void setOperand(Expr operand) {
        this.operand = operand;
    }
}

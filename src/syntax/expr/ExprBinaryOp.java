package syntax.expr;
import java_cup.runtime.ComplexSymbolFactory.Location;
public abstract class ExprBinaryOp extends Expr {
    private Expr leftOperand;
    private Expr rightOperand;

    public ExprBinaryOp(Location left, Location right, Expr leftOperand, Expr rightOperand) {
        super(left, right);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Expr getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(Expr leftOperand) {
        this.leftOperand = leftOperand;
    }

    public Expr getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Expr rightOperand) {
        this.rightOperand = rightOperand;
    }
}

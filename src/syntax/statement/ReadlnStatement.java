package syntax.statement;
import exception.CompilerException;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.expr.Identifier;
import visitor.Visitor;

import java.util.ArrayList;

public class ReadlnStatement extends Statement {
    private ArrayList<Identifier> idList;
    public ReadlnStatement(Location left, Location right, ArrayList<Identifier> idList) {
        super(left, right);
        this.idList=idList;
    }

    public ArrayList<Identifier> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<Identifier> idList) {
        this.idList = idList;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}

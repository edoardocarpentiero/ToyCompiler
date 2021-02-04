package syntax.statement;
import exception.CompilerException;
import java_cup.runtime.ComplexSymbolFactory.Location;
import visitor.Visitor;


import java.util.ArrayList;

public class ElseStatement extends Statement {

    private ArrayList<Statement> statList;


    public ElseStatement(Location left, Location right, ArrayList<Statement> statList ) {
        super(left, right);
        this.statList = statList;

    }

    public ArrayList<Statement> getStatList() {
        return statList;
    }

    public void setStatList(ArrayList<Statement> statList) {
        this.statList = statList;
    }

    @Override
    public <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException {
        return visitor.visit(this,arg);
    }
}
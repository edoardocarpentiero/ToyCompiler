package semantic.symbolTable;

import syntax.type.NodeType;
import semantic.TypologyVariable;

public class SymbolTableEntry {


    private TypologyVariable typologyVariable;
    private NodeType typesEntry;
    private String lexeme;

    public SymbolTableEntry(String lexeme, TypologyVariable typologyVariable, NodeType typesEntry) {
        this.lexeme = lexeme;
        this.typologyVariable = typologyVariable;
        this.typesEntry = typesEntry;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public TypologyVariable getTypologyLexeme() {
        return typologyVariable;
    }

    public void setTypologyLexeme(TypologyVariable typologyVariable) {
        this.typologyVariable = typologyVariable;
    }


    public NodeType getTypesEntry() {
        return typesEntry;
    }

    public void setTypesEntry(NodeType typesEntry) {
        this.typesEntry = typesEntry;
    }


}

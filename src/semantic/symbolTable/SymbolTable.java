package semantic.symbolTable;

import lexical.StringTable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

public class SymbolTable {

    private Stack<Integer> scopeLevel;
    private StringTable stringTable;
    private int currentScope;
    //Integer key linked HashMap -> indice scope
    // Integer key hash map -> indice di posizione del lessema nella stringTable
    private LinkedHashMap<Integer, HashMap<Integer,SymbolTableEntry>> table;

    public SymbolTable(StringTable stringTable) {
        this.stringTable = stringTable;
        scopeLevel = new Stack<>();
        currentScope = -1;
        table = new LinkedHashMap<>();
    }

    //creo un nuovo scope se non esiste
    public void enterScope(){
        scopeLevel.push(++currentScope);
        if(!table.containsKey(scopeLevel.peek())){
            table.put(scopeLevel.peek(),new HashMap<>());
        }
    }

    public void addEntry(String lexeme, SymbolTableEntry symbolTableEntry){
        int position = stringTable.getPositionLexeme(lexeme);
        table.get(scopeLevel.peek()).put(position,symbolTableEntry);
    }

    public SymbolTableEntry getEntry(String lexeme){
        int position = stringTable.getPositionLexeme(lexeme);
        return table.get(scopeLevel.peek()).get(position);
    }


    /**
     * Restituisco la entry della tabella dei simboli se prensente nel type environment
     * @param lexeme
     * @return null se il lessema non è presente nel type environment
     */
    public SymbolTableEntry lookup(String lexeme){
        int position = stringTable.getPositionLexeme(lexeme);
        SymbolTableEntry entry = null;
        int sizeStack= scopeLevel.size() - 1;
            for(int i=sizeStack; i>=0 && entry== null; i--){
                int level = this.scopeLevel.elementAt(i);
                entry = table.get(level).get(position);

            }
        return entry;
    }


    /**
     * Verifico se il lessema è definito nello scope corrente
     * @param lexeme
     * @return true se il lessema è presente nello scope corrente - false, altrimenti
     */
    public boolean probe(String lexeme){
        boolean isContained = false;
        if(table.containsKey(scopeLevel.peek()))
            isContained = table.get(scopeLevel.peek()).containsKey(stringTable.getPositionLexeme(lexeme));
        return isContained;
    }

    //esco dallo scope attuale (top dello stack)
    public void exitScope(){
        scopeLevel.pop();
    }

    public void resetStackScope(){
        currentScope = -1;
    }


    public StringTable getStringTable() {
        return stringTable;
    }

    public void setStringTable(StringTable stringTable) {
        this.stringTable = stringTable;
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                ", table=" + table +
                '}';
    }
}

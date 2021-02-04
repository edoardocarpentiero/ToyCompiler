package lexical;

import java.util.ArrayList;

public class StringTable {

    private ArrayList<String> lexemes;

    public StringTable() {
        this.lexemes = new ArrayList<>();
    }

    public int getPositionLexeme(String lexeme){
        return lexemes.lastIndexOf(lexeme);
    }


    public boolean installLexeme(String lexeme){
        boolean isContained = (getPositionLexeme(lexeme)==-1) ? false : true;
        if(!isContained)
            return lexemes.add(lexeme);
        return false;
    }

    public String getLexeme(int position){
        return lexemes.get(position);
    }

    public ArrayList getStringTable(){
        return lexemes;
    }
}

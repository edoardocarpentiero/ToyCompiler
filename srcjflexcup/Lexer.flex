// Circuit.lex
//
// Description of lexer for circuit description language.
//
// Ian Stark

//import java_cup.runtime.Symbol; 		//This is how we pass tokens to the parser
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.Symbol;
import lexical.StringTable;
import java.util.LinkedHashMap;
%%

										// Declarations for JFlex
%class Lexer
%public
%cupsym ParserSym
%cup
%unicode

%line
%column


%{

    private ComplexSymbolFactory complexSymbolFactory;
    private StringTable stringTable;
    private StringBuffer string = new StringBuffer();

    Lexer(String filePath, ComplexSymbolFactory complexSymbolFactory, StringTable stringTable) {
          this.stringTable = stringTable;
          this.complexSymbolFactory=complexSymbolFactory;
          initialize(filePath);
      }
      // prepara file input per lettura e controlla errori
      public boolean initialize(String filePath) {
        try {
          this.zzReader = new java.io.FileReader(filePath);
          return true;
        } catch (java.io.FileNotFoundException e) {
          return false;
        }
      }

      private Symbol generateToken(int type) {
          String name = ParserSym.terminalNames[type];
          return complexSymbolFactory.newSymbol(name,type,new Location(yyline + 2, yycolumn),new Location(yyline + 2, yycolumn));
      }


      private Symbol generateToken(int type, Object value) {
          String name = ParserSym.terminalNames[type];
          if(name.equals("ID"))
            stringTable.installLexeme(String.valueOf(value));
          return complexSymbolFactory.newSymbol(name, type, new Location(yyline + 2, yycolumn), new Location(yyline + 2, yycolumn), value);
      }


        public StringTable getStringTable(){
            return stringTable;
        }




%}




LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]


EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

Exponent = [eE][\+\-]?[0-9]+
Identifier = [:jletter:] [:jletterdigit:]*

/*DIVIDE IL TOKEN NUM IN INTEGER E FLOAT POICHE' BISOGNA FARE LA RISPETTIVA CONVERSIONE DEL VALORE NUMERICO RESTITUITO*/
IntegerNum =  0|[1-9][0-9]*
FloatNum =  (0|[1-9][0-9]*)\.[0-9]+({Exponent})?


/* Diagramma STRING e TRADITIONAL_COMMENT*/
%state STRING
%state TRADITIONAL_COMMENT

%%


<YYINITIAL> {

   /* comments */
   "/*" { string.setLength(0); yybegin(TRADITIONAL_COMMENT); }

  /* keywords */

  /*if condition*/
  "if" { return generateToken(ParserSym.IF); }
  "then" { return generateToken(ParserSym.THEN); }
  "else" { return generateToken(ParserSym.ELSE); }
  "elif" { return generateToken(ParserSym.ELIF);}
  "fi" { return generateToken(ParserSym.FI);}

  /*while construct*/
  "while" { return generateToken(ParserSym.WHILE); }
  "do" { return generateToken(ParserSym.DO); }
  "od" { return generateToken(ParserSym.OD);}

   /* procedure */
  "proc" { return generateToken(ParserSym.PROC); }
  ":" { return generateToken(ParserSym.COLON);}
  "corp" { return generateToken(ParserSym.CORP);}


    /* tipi */
  "float" { return generateToken(ParserSym.FLOAT);  }
  "int" { return generateToken(ParserSym.INT); }
  "void" { return generateToken(ParserSym.VOID);}
  "bool" { return generateToken(ParserSym.BOOL);}
  "string" { return generateToken(ParserSym.STRING);}
    /* Input / Output  */
   "readln" { return generateToken(ParserSym.READ);}
   "write" { return generateToken(ParserSym.WRITE);}


  /* separators */
  "(" { return generateToken(ParserSym.LPAR); }
  ")" { return generateToken(ParserSym.RPAR); }
  "," { return generateToken(ParserSym.COMMA); }
  ";" { return generateToken(ParserSym.SEMI); }

  /* operators*/
  "+" { return generateToken(ParserSym.PLUS); }
  "-" { return generateToken(ParserSym.MINUS); }
  "*" { return generateToken(ParserSym.TIMES); }
  "/" { return generateToken(ParserSym.DIV); }

  /* relop */
  "<" { return generateToken(ParserSym.LT); }
  "<=" { return generateToken(ParserSym.LE); }
  "=" { return generateToken(ParserSym.EQ); }
  "<>" { return generateToken(ParserSym.NE); }
  ">" { return generateToken(ParserSym.GT); }
  ">=" { return generateToken(ParserSym.GE); }
  ":=" {return generateToken(ParserSym.ASSIGN);}
  "&&" { return generateToken(ParserSym.AND);}
  "||" { return generateToken(ParserSym.OR);}
  "!" { return generateToken(ParserSym.NOT);}
  "null" { return generateToken(ParserSym.NULL);}
  "true" { return generateToken(ParserSym.TRUE,true);}
  "false" { return generateToken(ParserSym.FALSE,false);}
  "->" { return generateToken(ParserSym.RETURN);}

  /* identifiers */
   {Identifier}          { return generateToken(ParserSym.ID, yytext()); } //yytext() restituisce il lessema -- inserire la chiamata al metodo installID per inserire il valore della tabaella delle stringhe

  /* IntegerNum*/
  {IntegerNum}   { return generateToken(ParserSym.INT_CONST, Integer.parseInt(yytext())); }

  /* FloatNum*/
    {FloatNum}   { return generateToken(ParserSym.FLOAT_CONST, Float.parseFloat(yytext())); }

  // WHEN " IS REACHED IT'S STARTING A STRING
  \" { string.setLength(0); yybegin(STRING); }


  {EndOfLineComment} {/* ignore */}

  /* whitespace */
  {WhiteSpace} { /* ignore */ }



}

<TRADITIONAL_COMMENT>{
    "*/"           { yybegin(YYINITIAL); }
    [^*]     {}
     <<EOF>> { return generateToken(ParserSym.error,"Commento non chiuso");}
}

<STRING> {
    \"          { yybegin(YYINITIAL); return generateToken(ParserSym.STRING_CONST, string.toString()); }
    [^\"]+      { string.append( yytext() ); }
    <<EOF>> { return generateToken(ParserSym.error,"Stringa costante non completata");}
}



<<EOF>> { return generateToken(ParserSym.EOF); }

/* error fallback */
[^] {
    return generateToken(ParserSym.error,"Lessema non riconosciuto! \'"+yytext()+"\'");
}

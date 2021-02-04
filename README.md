**Specifica Linguaggio Toy**
--

**1. Analisi Lessicale**
--


| Token | Pattern |
| ------ | ------ |
| SEMI | ; |
| COMMA | , |
| ID | Any sequence of letters, digits and underscores,starting with a letter or an underscore |
| INT | int |
| STRING | string |
| FLOAT | float |
| BOOL | bool |
| LPAR | ( |
| RPAR | ) |
| COLON | : |
| PROC | proc |
| CORP | corp |
| VOID | voit |
| IF | if |
| THEN | then |
| ELIF | elif |
| FI | fi |
| ELSE | else |
| WHILE | while |
| DO | do |
| OD | od |
| READ | realn |
| WRITE | write |
| ASSIGN | := |
| PLUS | + |
| MINUS | - |
| TIMES | * |
| DIV | / |
| EQ | = |
| NE | <> |
| LT | < |
| LE | <= |
| GT | \> |
| GE | >= |
| AND | && |
| OR | or |
| NOT | ! |
| NULL | null |
| TRUE | true |
| FALSE | false |
| INT_CONST | any integer number |
| FLOAT_CONST | any floating point number |
| STRING_CONST | any string between " |

**2. Analisi Sintattica**
--
**Program** ::= VarDeclList ProcList
<br><br>
**VarDeclList** ::= /* empty */ 
	| VardDecl VarDeclList
<br><br>	
**ProcList** ::= Proc  | Proc ProcList
<br>			
**VarDecl** ::= Type IdListInit SEMI
<br><br>
**Type** ::= INT | BOOL | FLOAT | STRING
<br><br>
**IdListInit** ::= ID 
	| IdListInit COMMA ID
	| ID ASSIGN Expr
	| IdListInit COMMA ID ASSIGN Expr
	<br><br>	
**Proc**        ::= PROC ID LPAR ParamDeclList RPAR ResultTypeList COLON VarDeclList StatList RETURN ReturnExprs CORP SEMI <br>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| PROC ID LPAR ParamDeclList RPAR ResultTypeList COLON VarDeclList RETURN ReturnExprs CORP SEMI<br>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| PROC ID LPAR RPAR ResultTypeList COLON VarDeclList StatList RETURN ReturnExprs CORP SEMI<br>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| PROC ID LPAR RPAR ResultTypeList COLON VarDeclList RETURN ReturnExprs CORP SEMI
<br><br>
	    
**ResultTypeList** ::= ResultType
	| ResultType COMMA ResultTypeList

**ReturnExprs** ::=  ExprList 
	| /* empty */ 

**ExprList** ::= Expr	
	| Expr COMMA ExprList
<br><br>		
**ParamDeclList** ::= ParDecl | ParamDeclList SEMI ParDecl
<br><br>
**ParDecl** ::= Type IdList
<br><br>
**IdList** ::= ID | IdList COMMA ID
<br><br>
**ResultType** ::= Type | VOID 
<br><br>
**StatList** ::= Stat  | Stat StatList
<br><br>
**Stat** ::= IfStat SEMI
	| WhileStat SEMI
	| ReadlnStat SEMI
	| WriteStat SEMI
	| AssignStat SEMI
	| CallProc SEMI
<br><br>
**IfStat** ::= IF Expr THEN StatList ElifList Else FI
<br><br>
**ElifList** ::=  empty  
	| Elif ElifList		   
<br><br>
**Elif** ::= ELIF Expr THEN StatList
<br><br>
**Else** ::= /* empty */ | ELSE StatList
<br><br>	
**WhileStat**     ::= WHILE StatList RETURN Expr DO StatList OD 
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| WHILE Expr DO StatList OD 
<br><br>                  
**ReadlnStat** ::= READ LPAR IdList RPAR
<br><br>
**WriteStat** ::=  WRITE LPAR ExprList RPAR
<br><br>
**AssignStat** ::= IdList ASSIGN  ExprList
<br><br>
**CallProc** ::= ID LPAR ExprList RPAR | ID LPAR RPAR   
<br><br>
**Expr** ::= NULL                          
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| TRUE                            
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| FALSE                           
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| INT_CONST<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| FLOAT_CONST<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| STRING_CONST<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| ID<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| CallProc<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  PLUS Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  MINUS Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  TIMES Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  DIV Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  AND Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  OR Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  GT Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  GE Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  LT Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  LE Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  EQ Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Expr  NE Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| MINUS Expr<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| NOT Expr<br>

**3. Analisi Semantica**
--

**2.1. Gestione scoping**

La gestione dello scoping avviene attraverso due visite dell'albero sintattico<br>
- Il visitor _CreateScopingVisitor.java_ consentirà di individuare i livelli di scope presenti nel codice sorgente. 
Tale attività include il popolamento della tabella dei simboli principale (Program) inserendo gli identificativi delle 
procedure e i rispettivi tipi dei parametri e tipi di ritorno<br>
- In _ScopeVisitor.java_ viene ultimato il popolamento delle tabelle dei simboli associate ai vari livelli di scope, 
inserendo le variabili e i controlli necessari.

**2.2. Type Checking**

Il type checking avviene attraverso una sola visita dell'AST
- Il visitor _TypeCheckingVisitor.java_ effettuerà il type checking. 
Quest'ultimo avviene mediante l'utilizzo di _OperationTable.java_ e delle regole fornite dal TypeSystem.

**2.2.1** Type System
- costrutto Proc, nodo _Proc_ <br>
**IF** il nodo _BodyProc_ non presenta errori di tipo<br>
**THEN** Proc non ha errori di tipo<br>
**ELSE** typeError

- costrutto Proc, nodo _BodyProc_<br>
**IF** il nodo _VarDecl_ non ha errori di tipo **AND** il nodo _Statement_ non ha errori di tipo **AND** il nodo _ReturnExp_ non ha errori di tipo<br>
**THEN** BodyProc non ha errori di tipo<br>
**ELSE** typeError<br>

- costrutto Proc, nodo _ReturnExpr_<br>
**IF** il numero dei nodi figli coincide con il numero dei nodi figli di _ResultType_ **AND** i nodi figli hanno lo stesso tipo di quelli dichiarati rispettivamente in _ResultType_<br>
**THEN** ReturnExpr non ha errori di tipo<br>
**ELSE** typeError<br>

- costrutto Proc, nodo _ReturnExpr_<br>
**IF** non ci sono nodi figli **AND** in _ResultType_ esiste solo il tipo VOID<br>
**THEN** ReturnExpr non ha errori di tipo<br>
**ELSE** typeError<br>

- costrutto CallProc, nodo _CallProc_<br>
**IF** il numero dei nodi del secondo figlio coincide con il numero dei nodi figli di _ParDecl_ **AND** i nodi figli hanno tipi compatibili(*) di quelli presenti in _ParDecl_<br>
**THEN** CallProc non ha errori di tipo<br>
**ELSE** typeError<br>

- costrutto write, nodo _WriteStatement_<br>
**IF** i nodi figli non hanno errori di tipo<br>
**THEN** WriteStatement non ha errori di tipo<br>
**ELSE** typeError<br>

_(*) ammesso il cast implicito da FLOAT a INT e viceversa._<br>

Nella dichiarazione delle variabili, se una variabile viene inizializzata con una procedura avente 0 o piu di un resulttype viene lanciato un errore semantico.
<br>L'istruzione Assignment e ReturnExpr accettano il cast implicito da FLOAT a INT e viceversa.
<br> GLi operatori di relazione da applicare alle stringhe sono: LT EQ GT, altrimenti viene lanciato un errore semantico;<br>

**3. Gestione degli errori**
--
- Per gestire gli errori derivati dall'analisi lessicale, e' stato sovrascritto il metodo _scan()_ della classe _Parser.java_ (generata da CUP) in modo
da generare correttamente il tipo di errore che si è verificato durante la fase di riconoscimento dell'analisi sintattica.
- Nell'analisi sintattica, gli errori vengono gestiti da CUP e mostrati mediante la sovrascrittura del metodo _report_error()_. Tale metodo, una volta chiamato, consente la visualizzazione del tipo di 
errore riscontrato e i relativi dettagli.
- Se tra le fasi dell'analisi semantica (Scoping e Type checking) dovessero verificarsi degli errori, la fase successiva non verrà eseguita.

**4. Generarione codice Intermedio (Codice C)**
--
**4.1** Per consentire la traduzione delle procedure aventi piu tipi/valori di ritorno sono state utilizzate le _struct_.<br>





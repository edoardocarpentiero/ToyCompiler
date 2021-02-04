import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import lexical.StringTable;
import org.w3c.dom.Document;
import semantic.symbolTable.SymbolTable;
import syntax.Program;


import syntax.type.TypesVar;
import utils.CreateSourceFileC;
import visitor.concreteVisitor.IntermediateCodeGenerationVisitor.CodeGenerationVisitor;
import visitor.concreteVisitor.SemanticVisitor.CreateScopingVisitor;
import visitor.concreteVisitor.SemanticVisitor.ScopeVisitor;
import visitor.concreteVisitor.SemanticVisitor.TypeCheckingVisitor;
import visitor.concreteVisitor.SyntaxVisitor.XmlVisitor;
import utils.XMLTool;


public class Main {

    private static StringTable stringTable;

    private static ComplexSymbolFactory complexSymbolFactory;
    private static SymbolTable symbolTable;

    public static void main (String[] args) throws Exception {
        stringTable = new StringTable();
        symbolTable = new SymbolTable(stringTable);
        complexSymbolFactory = new ComplexSymbolFactory();

        Lexer lexer=new Lexer(args[0],complexSymbolFactory, stringTable);

        System.out.println("-> Syntax Analysis START");
            Parser parser=new Parser(lexer,complexSymbolFactory);
            Program program= (Program) parser.parse().value;


            System.out.println("-- CREATION AST --");
                XmlVisitor xmlVisitor=new XmlVisitor();
                Document xmlTree = XMLTool.createDocument();
                program.accept(xmlVisitor,xmlTree);
                XMLTool.writeDocument(xmlTree,args[0]);


        System.out.println("Syntax Analysis END <-\n");

        boolean isOk = false;

        System.out.println("-> Semantic Analysis START ");
            System.out.println("\n-> Scope Checking START");
                CreateScopingVisitor nameProcCheckVisitor =new CreateScopingVisitor();
                isOk = program.accept(nameProcCheckVisitor,symbolTable);
                symbolTable.resetStackScope();

                ScopeVisitor scopeVisitor = new ScopeVisitor();
                if(isOk)
                    isOk = program.accept(scopeVisitor,symbolTable);

                symbolTable.resetStackScope();
                System.out.println("Scope Checking END <-\n");
        if(isOk) {

            System.out.println("-> Type Checking START");
                isOk = program.accept(new TypeCheckingVisitor(), symbolTable).equals(TypesVar.VOID);
            System.out.println(" Type Checking END <-\n");

            System.out.println("Semantic Analysis END <-");
        }

        if(isOk){
            System.out.println("-> Intermediate Code Generation START ");
            CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
            String codice ="";
            try {
                codice = program.accept(codeGenerationVisitor,null);
                CreateSourceFileC.createSourceCodeFile(args[0],codice);
                System.out.println("Intermediate Code Generation END <-");
                System.out.printf("Compiled!");
            }catch (Exception e){
                System.err.println("Errore generazione codice sogente "+ e.getCause().getMessage());
            }
        }
    }
}

package visitor.concreteVisitor.SemanticVisitor;


import exception.MainException;
import exception.ProcedureDeclarationException;
import exception.CompilerException;
import syntax.type.TypesProcedure;
import semantic.TypologyVariable;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.SymbolTableEntry;
import syntax.ASTNode;
import syntax.Program;
import syntax.VarDecl;
import syntax.expr.ExprLogicOp.ExprOpLogicAND;
import syntax.expr.ExprLogicOp.ExprOpLogicNOT;
import syntax.expr.ExprLogicOp.ExprOpLogicOR;
import syntax.expr.ExprMathOp.*;
import syntax.expr.ExprRelopOp.*;
import syntax.expr.ExprValue.*;
import syntax.expr.Identifier;
import syntax.initId.IdInit;
import syntax.procedure.BodyProc;
import syntax.procedure.CallProc;
import syntax.procedure.ParDecl;
import syntax.procedure.Proc;
import syntax.statement.*;
import syntax.type.ResultType;
import syntax.type.Type;
import syntax.type.TypesVar;
import visitor.Visitor;

import java.util.ArrayList;

/**
 * Questo visitor consente di individuare le procedure utilizzate nel file sorgente e di inserirle nello scope di Program
 */
public class CreateScopingVisitor implements Visitor<Boolean, SymbolTable> {

    private TypesVar typeVar;
    private String messageError;

    private int countProcMain=0;
    private ArrayList<TypesVar> tipiParamentri = new ArrayList<>();
    private ArrayList<TypesVar> resultTypes = new ArrayList<>();
    private ArrayList<String> errorReport = new ArrayList<>();


    private boolean acceptList(ArrayList<? extends ASTNode> nodes, SymbolTable table)  {
        if(nodes==null)
            return true;
        if(nodes.isEmpty()){
            return true;
        }
        for (ASTNode node : nodes){
            try {
                if(!node.accept(this,table))
                    return false;
            } catch (CompilerException e) {
                errorReport.add(e.getMessage()+"\n"+e.getCause());
                return false;
            }
        }
        return true;
    }

    private boolean errorsReport(){
        if(errorReport.size()==0)
            return false;
        for(String error: errorReport)
            System.err.println(error);
        return true;
    }



    @Override
    public Boolean visit(Program program, SymbolTable arg)  {
        arg.enterScope();
        boolean result = acceptList(program.getProcList(),arg);
        try {
            if (countProcMain == 0 )
                    throw new CompilerException("Scoping creation error!", new MainException("MAIN procedure not declared!"));
            else if (countProcMain > 1)
                throw new CompilerException("Scoping creation error!", new MainException("MAIN procedure declared " + countProcMain + " times!"));
        } catch (CompilerException e) {
            errorReport.add(e.getMessage()+"\n"+e.getCause());
        }
        if(!errorsReport())
            result=true;
        arg.exitScope();
        return result;
    }

    @Override
    public Boolean visit(Proc proc, SymbolTable arg) throws CompilerException {
        String nameProcedure = proc.getIdentifier().getValue();
        if(nameProcedure.equals("main"))
            countProcMain++;

        boolean isExist = proc.getIdentifier().accept(this, arg);
        if (isExist) {

                throw new CompilerException("Scoping creation error!", new ProcedureDeclarationException("The procedure " +
                        "\"" + nameProcedure + "()\" " +
                        "has already been declared! Error in Line" + (proc.getLeft().getLine() - 1) + "" +
                        " - Col " + proc.getRight().getColumn()));
        }

        acceptList(proc.getParamDeclList(),arg);
        acceptList(proc.getResultTypeList(),arg);

        arg.addEntry(nameProcedure, new SymbolTableEntry(nameProcedure, TypologyVariable.PROC,new TypesProcedure((ArrayList<TypesVar>)tipiParamentri.clone(),(ArrayList<TypesVar>)resultTypes.clone())));

        tipiParamentri.removeAll(tipiParamentri);
        resultTypes.removeAll(resultTypes);
        return true;
    }


    @Override
    public Boolean visit(Identifier identifier, SymbolTable arg) {
        return arg.probe(identifier.getValue());
    }

    @Override
    public Boolean visit(Type type, SymbolTable arg) {
        this.typeVar = type.getValueType();
        return true;
    }

    @Override
    public Boolean visit(ParDecl parDecl, SymbolTable arg) {
        parDecl.getType().accept(this,arg);
        boolean result = true;
        int size = parDecl.getIdentifiers().size();
        for(int i=0;i<size;i++)
            result &= tipiParamentri.add(typeVar);
        return result;
    }

    @Override
    public Boolean visit(ResultType resultType, SymbolTable arg) {
        return resultTypes.add(resultType.getType().getValueType());
    }

    @Override
    public Boolean visit(VarDecl varDecl, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(IdInit idInit, SymbolTable arg) {
        return true;
    }


    @Override
    public Boolean visit(BodyProc bodyProc, SymbolTable arg) {
        return true;
    }



    @Override
    public Boolean visit(IfStatement statement, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ElifStatement elifStatement, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ElseStatement elseStatement, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(WhileStatement whileStatement, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ReadlnStatement readlnStatement, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(WriteStatement writeStatement, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(AssignStatement assignStatement, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(CallProc callProc, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpNull exprOpNull, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpTrue exprOpTrue, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpFalse exprOpFalse, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpIntConst exprOpIntConst, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpFloatConst exprOpFloatConst, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpStringConst exprOpStringConst, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpMathPlus exprOpMathPlus, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpMathSub exprOpMathSub, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpMathTimes exprOpMathTimes, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpMathDiv exprOpMathDiv, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpMathMinus exprOpMathMinus, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpLogicAND exprOpLogicAND, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpLogicOR exprOpLogicOR, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpLogicNOT exprOpLogicNOT, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpRelopGT exprOpRelopGT, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpRelopGE exprOpRelopGE, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpRelopLT exprOpRelopLT, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpRelopLE exprOpRelopLE, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpRelopEQ exprOpRelopEQ, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(ExprOpRelopNE exprOpRelopNE, SymbolTable arg) {
        return true;
    }
}

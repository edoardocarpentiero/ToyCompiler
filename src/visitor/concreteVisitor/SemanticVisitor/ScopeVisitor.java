package visitor.concreteVisitor.SemanticVisitor;

import exception.CompilerException;
import exception.VariableDeclarationException;
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
 * In questo Visitor viene effettuata l'analisi degli scope e il popolamento delle rispettive tabelle dei simboli
 */
public class ScopeVisitor implements Visitor<Boolean, SymbolTable> {

    private TypesVar typeVar;
    private boolean isDeclaration =false;
    private String messageError="\n";
    private ArrayList<String> errorReport = new ArrayList<>();
    private boolean isProcedure = false;

    private boolean acceptList(ArrayList<? extends ASTNode> nodes, SymbolTable table)  {
        if( nodes==null)
            return true;
        if(nodes.isEmpty()){
            return true;
        }
        for (ASTNode node : nodes){

            try {
                node.accept(this,table);
            } catch (CompilerException e) {
                errorReport.add(e.getMessage()+"\n"+e.getCause());
                return false;
            }
        }
        return true;
    }
    private boolean errorsReport(){
        if(errorReport.size()==0)
            return true;
        for(String error: errorReport)
            System.err.println(error);
        return false;
    }

    @Override
    public Boolean visit(Program program, SymbolTable arg) throws CompilerException {
        arg.enterScope();

        boolean result=false;
        result = acceptList(program.getVarDecls(),arg);
        result &= acceptList(program.getProcList(),arg);
        result &= errorsReport();
        arg.exitScope();
        return result;
    }

    @Override
    public Boolean visit(VarDecl varDecl, SymbolTable arg) throws CompilerException {
        boolean isOk = varDecl.getType().accept(this,arg);
        isDeclaration =true;
        isOk &= acceptList(varDecl.getIdListInit(), arg);
        isDeclaration =false;
        return isOk;
    }

    @Override
    public Boolean visit(IdInit idInit, SymbolTable arg) throws CompilerException {
        boolean result = idInit.getId().accept(this,arg);
        if(idInit.getExpr()!=null) {
            isDeclaration =false;
            result &= idInit.getExpr().accept(this, arg);
            isDeclaration =true;
        }
        return result;
    }

    @Override
    public Boolean visit(Type type, SymbolTable arg) {
        this.typeVar = type.getValueType();
        return true;
    }

    @Override
    public Boolean visit(Proc proc, SymbolTable arg) throws CompilerException {
        arg.enterScope();
        boolean result = acceptList(proc.getParamDeclList(), arg);
        result &= proc.getBodyProc().accept(this, arg);
        arg.exitScope();
        return result;
    }

    /**
     * In questa visita alla classe Identifier verifico la presenza di una variabile nel type environment
     * @param identifier
     * @param arg
     * @return
     */
    @Override
    public Boolean visit(Identifier identifier, SymbolTable arg) throws CompilerException {
        String variable = identifier.getValue();
        boolean result = true;

        if(isDeclaration){
            if(!arg.probe(variable))
                arg.addEntry(variable, new SymbolTableEntry(variable, TypologyVariable.VAR, typeVar));
            else{
                result = false;
                throw new CompilerException("Scoping creation error!",new VariableDeclarationException("The variable \"" + variable + "\" " +
                        " already been declared! Error in Line " + (identifier.getLeft().getLine()-1) + "" +
                        " - Col " + identifier.getRight().getColumn()));
            }
        }else{
            SymbolTableEntry symbolTableEntry = arg.lookup(variable);
            if(symbolTableEntry==null) {
                isProcedure = false;
                result = false;
                throw new CompilerException("Scoping creation error!",new VariableDeclarationException("The variable \"" + variable + "\" " +
                        "has not been defined! Error in Line " + (identifier.getLeft().getLine()-1) + " " +
                        "- Col " + identifier.getRight().getColumn()));
            }
            else if(isProcedure){
                isProcedure = false;
                if(symbolTableEntry.getTypologyLexeme().equals(TypologyVariable.VAR)) {
                    result = false;
                    throw new CompilerException("Scoping creation error!", new VariableDeclarationException("Unable to call procedure \"" + variable + "\" " +
                            "! The procedure \'" + variable + "()\' conflicts with the variable \'" + variable + "\'!" + (identifier.getLeft().getLine() - 1) + " " +
                            "- Col " + identifier.getRight().getColumn()));
                }
            }
        }
        return result;
    }

    @Override
    public Boolean visit(ParDecl parDecl, SymbolTable arg) throws CompilerException {
        boolean result=parDecl.getType().accept(this,arg);
        isDeclaration =true;
        result &= acceptList(parDecl.getIdentifiers(),arg);
        isDeclaration =false;

        return result;
    }

    @Override
    public Boolean visit(ResultType resultType, SymbolTable arg) {
        return true;
    }

    @Override
    public Boolean visit(BodyProc bodyProc, SymbolTable arg) throws CompilerException {
        boolean result = false;
        if(bodyProc.getVarDeclList()!=null) {
            isDeclaration = true;
            result = acceptList(bodyProc.getVarDeclList(), arg);
            isDeclaration =false;
        }else
            result = true;

        result &= acceptList(bodyProc.getStatList(),arg);

        result &= acceptList(bodyProc.getReturnExprs(),arg);
        return result;
    }

    @Override
    public Boolean visit(IfStatement statement, SymbolTable arg) throws CompilerException {
        boolean result = statement.getCondition().accept(this,arg);
        result &= acceptList(statement.getThenStatement(),arg);
        result &= acceptList(statement.getElifListStatement(),arg);
        if(statement.getElseStatement()!=null)
            result &= statement.getElseStatement().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ElifStatement elifStatement, SymbolTable arg) throws CompilerException {
        boolean result = elifStatement.getCondition().accept(this,arg);
        result &= acceptList(elifStatement.getStatList(),arg);
        return result;
    }

    @Override
    public Boolean visit(ElseStatement elseStatement, SymbolTable arg) throws CompilerException {
        boolean result = acceptList(elseStatement.getStatList(),arg);
        return result;
    }

    @Override
    public Boolean visit(WhileStatement whileStatement, SymbolTable arg) throws CompilerException {
        boolean result = whileStatement.getCondition().accept(this,arg);
        result &= acceptList(whileStatement.getStatList(),arg);
        result &= acceptList(whileStatement.getStatListDo(),arg);
        return result;
    }

    @Override
    public Boolean visit(ReadlnStatement readlnStatement, SymbolTable arg) throws CompilerException {
        boolean result = acceptList(readlnStatement.getIdList(),arg);
        return result;
    }

    @Override
    public Boolean visit(WriteStatement writeStatement, SymbolTable arg) throws CompilerException {
        boolean result = acceptList(writeStatement.getExprList(),arg);
        return result;
    }

    @Override
    public Boolean visit(AssignStatement assignStatement, SymbolTable arg) throws CompilerException {
        boolean result = acceptList(assignStatement.getIdList(),arg);
        result &= acceptList(assignStatement.getExprList(),arg);
        return result;
    }

    @Override
    public Boolean visit(CallProc callProc, SymbolTable arg) throws CompilerException {

        boolean result=acceptList(callProc.getExprList(),arg);
        isProcedure=true;
        result &= callProc.getIdValue().accept(this,arg);
        isProcedure=false;
        return result;
    }

    @Override
    public Boolean visit(ExprOpNull exprOpNull, SymbolTable arg) throws CompilerException {
        return true;
    }

    @Override
    public Boolean visit(ExprOpTrue exprOpTrue, SymbolTable arg) throws CompilerException {
        return true;
    }

    @Override
    public Boolean visit(ExprOpFalse exprOpFalse, SymbolTable arg) throws CompilerException {
        return true;
    }

    @Override
    public Boolean visit(ExprOpIntConst exprOpIntConst, SymbolTable arg) throws CompilerException {

        return true;
    }

    @Override
    public Boolean visit(ExprOpFloatConst exprOpFloatConst, SymbolTable arg) throws CompilerException {
        return true;
    }

    @Override
    public Boolean visit(ExprOpStringConst exprOpStringConst, SymbolTable arg) throws CompilerException {
        return true;
    }



    @Override
    public Boolean visit(ExprOpMathPlus exprOpMathPlus, SymbolTable arg) throws CompilerException {

        boolean result = exprOpMathPlus.getLeftOperand().accept(this,arg);
        result &= exprOpMathPlus.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpMathSub exprOpMathSub, SymbolTable arg) throws CompilerException {

        boolean result = exprOpMathSub.getLeftOperand().accept(this,arg);
        result &= exprOpMathSub.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpMathTimes exprOpMathTimes, SymbolTable arg) throws CompilerException {

        boolean result = exprOpMathTimes.getLeftOperand().accept(this,arg);
        result &= exprOpMathTimes.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpMathDiv exprOpMathDiv, SymbolTable arg) throws CompilerException {

        boolean result = exprOpMathDiv.getLeftOperand().accept(this,arg);
        result &= exprOpMathDiv.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpMathMinus exprOpMathMinus, SymbolTable arg) throws CompilerException {
        boolean result = exprOpMathMinus.getOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpLogicAND exprOpLogicAND, SymbolTable arg) throws CompilerException {
        boolean result = exprOpLogicAND.getLeftOperand().accept(this,arg);
        result &= exprOpLogicAND.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpLogicOR exprOpLogicOR, SymbolTable arg) throws CompilerException {
        boolean result = exprOpLogicOR.getLeftOperand().accept(this,arg);
        result &= exprOpLogicOR.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpLogicNOT exprOpLogicNOT, SymbolTable arg) throws CompilerException {
        boolean result = exprOpLogicNOT.getOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpRelopGT exprOpRelopGT, SymbolTable arg) throws CompilerException {
        boolean result = exprOpRelopGT.getLeftOperand().accept(this,arg);
        result &= exprOpRelopGT.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpRelopGE exprOpRelopGE, SymbolTable arg) throws CompilerException {
        boolean result = exprOpRelopGE.getLeftOperand().accept(this,arg);
        result &= exprOpRelopGE.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpRelopLT exprOpRelopLT, SymbolTable arg) throws CompilerException {
        boolean result = exprOpRelopLT.getLeftOperand().accept(this,arg);
        result &= exprOpRelopLT.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpRelopLE exprOpRelopLE, SymbolTable arg) throws CompilerException {
        boolean result = exprOpRelopLE.getLeftOperand().accept(this,arg);
        result &= exprOpRelopLE.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpRelopEQ exprOpRelopEQ, SymbolTable arg) throws CompilerException {
        boolean result = exprOpRelopEQ.getLeftOperand().accept(this,arg);
        result &= exprOpRelopEQ.getRightOperand().accept(this,arg);
        return result;
    }

    @Override
    public Boolean visit(ExprOpRelopNE exprOpRelopNE, SymbolTable arg) throws CompilerException {

        boolean result = exprOpRelopNE.getLeftOperand().accept(this,arg);
        result &= exprOpRelopNE.getRightOperand().accept(this,arg);
        return result;
    }
}

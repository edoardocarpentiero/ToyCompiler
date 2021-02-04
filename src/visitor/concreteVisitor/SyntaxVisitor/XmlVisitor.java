package visitor.concreteVisitor.SyntaxVisitor;

import exception.CompilerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import syntax.initId.IdInitAssign;
import syntax.procedure.BodyProc;
import syntax.procedure.CallProc;
import syntax.procedure.ParDecl;
import syntax.procedure.Proc;
import syntax.statement.*;
import syntax.type.ResultType;
import syntax.type.Type;
import visitor.Visitor;

import java.util.function.Consumer;

public class XmlVisitor implements Visitor<Element, Document> {

    @Override
    public Element visit(Program program, Document arg) {

        Element element = arg.createElement(program.getClass().getSimpleName());
        if(program.getVarDecls()!=null)
            program.getVarDecls().forEach(appendChild(element,arg));
        program.getProcList().forEach(appendChild(element,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(VarDecl varDecl, Document arg) {
        Element element = arg.createElement(varDecl.getClass().getSimpleName());
        //Visito le variabili dichiarate
        element.appendChild(varDecl.getType().accept(this,arg));
        varDecl.getIdListInit().forEach(appendChild(element,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(Type type, Document arg) {
        Element element = arg.createElement(type.getClass().getSimpleName());
        element.appendChild(arg.createTextNode("("+type.getValueType().toString()+")"));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(Proc proc, Document arg) throws CompilerException {
        Element element = arg.createElement(proc.getClass().getSimpleName());
        element.appendChild(proc.getIdentifier().accept(this,arg));
        if(proc.getParamDeclList()!=null) {
            Element node=arg.createElement("ParDeclList");
            proc.getParamDeclList().forEach(appendChild(node, arg));
            element.appendChild(node);
        }
        if(proc.getResultTypeList()!=null) {
            Element node=arg.createElement("ResultTypeList");
            proc.getResultTypeList().forEach(appendChild(node, arg));
            element.appendChild(node);
        }
        element.appendChild(proc.getBodyProc().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ParDecl parDecl, Document arg) {
        Element element = arg.createElement(parDecl.getClass().getSimpleName());
        element.appendChild(parDecl.getType().accept(this,arg));
        parDecl.getIdentifiers().forEach(appendChild(element,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ResultType resultType, Document arg) {
        Element element = arg.createElement(resultType.getClass().getSimpleName());
        element.appendChild(resultType.getType().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(BodyProc bodyProc, Document arg) {
        Element element = arg.createElement(bodyProc.getClass().getSimpleName());
        if(bodyProc.getVarDeclList()!=null)
            bodyProc.getVarDeclList().forEach(appendChild(element,arg));

        if( bodyProc.getStatList()!=null)
            bodyProc.getStatList().forEach(appendChild(element,arg));

        if(bodyProc.getReturnExprs()!=null) {
            Element node=arg.createElement("ReturnExpr");
            bodyProc.getReturnExprs().forEach(appendChild(node, arg));
            element.appendChild(node);
        }
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(IdInit idInit, Document arg) throws CompilerException {
        Element element = arg.createElement(idInit.getClass().getSimpleName());

        element.appendChild(idInit.getId().accept(this,arg));
        if(idInit instanceof IdInitAssign)
            element.appendChild(idInit.getExpr().accept(this,arg));
        arg.appendChild(element);
        return element;
    }


    @Override
    public Element visit(IfStatement ifStatement, Document arg) throws CompilerException {
        Element element = arg.createElement(ifStatement.getClass().getSimpleName());

        element.appendChild(ifStatement.getCondition().accept(this,arg));
        ifStatement.getThenStatement().forEach(appendChild(element,arg));

        if(ifStatement.getElifListStatement()!=null)
        ifStatement.getElifListStatement().forEach(appendChild(element,arg));

        if(ifStatement.getElseStatement()!=null)
            element.appendChild(ifStatement.getElseStatement().accept(this,arg));

        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ElifStatement elifStatement, Document arg) throws CompilerException {
        Element element = arg.createElement(elifStatement.getClass().getSimpleName());
        element.appendChild(elifStatement.getCondition().accept(this,arg));
        elifStatement.getStatList().forEach(appendChild(element,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ElseStatement elseStatement, Document arg) {
        Element element = arg.createElement(elseStatement.getClass().getSimpleName());
        elseStatement.getStatList().forEach(appendChild(element,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(WhileStatement whileStatement, Document arg) throws CompilerException {
        Element element = arg.createElement(whileStatement.getClass().getSimpleName());
        if( whileStatement.getStatList()!=null)
            whileStatement.getStatList().forEach(appendChild(element,arg));
        if(whileStatement.getCondition()!=null)
            element.appendChild(whileStatement.getCondition().accept(this,arg));

        whileStatement.getStatListDo().forEach(appendChild(element,arg));

        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ReadlnStatement readlnStatement, Document arg) {
        Element element = arg.createElement(readlnStatement.getClass().getSimpleName());

        readlnStatement.getIdList().forEach(appendChild(element,arg));

        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(WriteStatement writeStatement, Document arg) {
        Element element = arg.createElement(writeStatement.getClass().getSimpleName());

        writeStatement.getExprList().forEach(appendChild(element,arg));

        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(AssignStatement assignStatement, Document arg) {
        Element element = arg.createElement(assignStatement.getClass().getSimpleName());
        assignStatement.getIdList().forEach(appendChild(element,arg));
        assignStatement.getExprList().forEach(appendChild(element,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(CallProc callProc, Document arg) throws CompilerException {
        Element element = arg.createElement(callProc.getClass().getSimpleName());

        element.appendChild(callProc.getIdValue().accept(this,arg));
        if(callProc.getExprList()!=null) {
            Element node=arg.createElement("ArgumentsProc");
            callProc.getExprList().forEach(appendChild(node, arg));
            element.appendChild(node);
        }
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpNull exprOpNull, Document arg) {
        Element element = arg.createElement(exprOpNull.getClass().getSimpleName());

        element.appendChild(arg.createTextNode("(NULL)"));

        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpTrue exprOpTrue, Document arg) {
        Element element = arg.createElement(exprOpTrue.getClass().getSimpleName());
        element.appendChild(arg.createTextNode("(TRUE)"));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpFalse exprOpFalse, Document arg) {
        Element element = arg.createElement(exprOpFalse.getClass().getSimpleName());
        element.appendChild(arg.createTextNode("(FALSE)"));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpIntConst exprOpIntConst, Document arg) {
        Element element = arg.createElement(exprOpIntConst.getClass().getSimpleName());
        element.appendChild(arg.createTextNode("(INT_CONST, "+exprOpIntConst.getValue()+")"));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpFloatConst exprOpFloatConst, Document arg) {
        Element element = arg.createElement(exprOpFloatConst.getClass().getSimpleName());
        element.appendChild(arg.createTextNode("(FLOAT_CONST, "+exprOpFloatConst.getValue()+")"));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpStringConst exprOpStringConst, Document arg) {
        Element element = arg.createElement(exprOpStringConst.getClass().getSimpleName());
        element.appendChild(arg.createTextNode("(STRING_CONST, "+exprOpStringConst.getValue()+")"));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(Identifier identifier, Document arg) {
        Element element = arg.createElement(identifier.getClass().getSimpleName());
        element.appendChild(arg.createTextNode("(ID ,"+identifier.getValue()+")"));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpMathPlus exprOpMathPlus, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpMathPlus.getClass().getSimpleName());
        element.appendChild(exprOpMathPlus.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpMathPlus.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpMathSub exprOpMathSub, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpMathSub.getClass().getSimpleName());
        element.appendChild(exprOpMathSub.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpMathSub.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpMathTimes exprOpMathTimes, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpMathTimes.getClass().getSimpleName());
        element.appendChild(exprOpMathTimes.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpMathTimes.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpMathDiv exprOpMathDiv, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpMathDiv.getClass().getSimpleName());
        element.appendChild(exprOpMathDiv.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpMathDiv.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpMathMinus exprOpMathMinus, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpMathMinus.getClass().getSimpleName());
        element.appendChild(exprOpMathMinus.getOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpLogicAND exprOpLogicAND, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpLogicAND.getClass().getSimpleName());
        element.appendChild(exprOpLogicAND.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpLogicAND.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpLogicOR exprOpLogicOR, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpLogicOR.getClass().getSimpleName());
        element.appendChild(exprOpLogicOR.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpLogicOR.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpLogicNOT exprOpLogicNOT, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpLogicNOT.getClass().getSimpleName());
        element.appendChild(exprOpLogicNOT.getOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpRelopGT exprOpRelopGT, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpRelopGT.getClass().getSimpleName());
        element.appendChild(exprOpRelopGT.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpRelopGT.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpRelopGE exprOpRelopGE, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpRelopGE.getClass().getSimpleName());
        element.appendChild(exprOpRelopGE.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpRelopGE.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpRelopLT exprOpRelopLT, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpRelopLT.getClass().getSimpleName());
        element.appendChild(exprOpRelopLT.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpRelopLT.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpRelopLE exprOpRelopLE, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpRelopLE.getClass().getSimpleName());
        element.appendChild(exprOpRelopLE.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpRelopLE.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpRelopEQ exprOpRelopEQ, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpRelopEQ.getClass().getSimpleName());
        element.appendChild(exprOpRelopEQ.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpRelopEQ.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    @Override
    public Element visit(ExprOpRelopNE exprOpRelopNE, Document arg) throws CompilerException {
        Element element = arg.createElement(exprOpRelopNE.getClass().getSimpleName());
        element.appendChild(exprOpRelopNE.getLeftOperand().accept(this,arg));
        element.appendChild(exprOpRelopNE.getRightOperand().accept(this,arg));
        arg.appendChild(element);
        return element;
    }

    private Consumer<? super ASTNode> appendChild(Element parent, Document arg){
        return (ASTNode astNode) -> {
            try {
                parent.appendChild(astNode.accept(this, arg));
            } catch (CompilerException e) {
                e.printStackTrace();
            }
        };
    }
}

package visitor;

import exception.CompilerException;
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

public interface Visitor <T,P> {

    T visit(Program program, P arg) throws CompilerException;

    T visit(VarDecl varDecl, P arg) throws CompilerException;

    T visit(Type type, P arg);

    T visit(Proc proc, P arg) throws CompilerException;

    T visit(ParDecl parDecl, P arg) throws CompilerException;

    T visit(ResultType resultType, P arg);

    T visit(BodyProc bodyProc, P arg) throws CompilerException;

    T visit(IdInit idInit, P arg) throws CompilerException;

    // STATEMENT

    T visit(IfStatement statement, P arg) throws CompilerException;

    T visit(ElifStatement elifStatement, P arg) throws CompilerException;

    T visit(ElseStatement elseStatement, P arg) throws CompilerException;

    T visit(WhileStatement whileStatement, P arg) throws CompilerException;

    T visit(ReadlnStatement readlnStatement, P arg) throws CompilerException;

    T visit(WriteStatement writeStatement, P arg) throws CompilerException;

    T visit(AssignStatement assignStatement, P arg) throws CompilerException;

    T visit(CallProc callProc, P arg) throws CompilerException;

    //CONST
    T visit(ExprOpNull exprOpNull, P arg) throws CompilerException;

    T visit(ExprOpTrue exprOpTrue, P arg) throws CompilerException;

    T visit(ExprOpFalse exprOpFalse, P arg) throws CompilerException;

    T visit(ExprOpIntConst exprOpIntConst, P arg) throws CompilerException;

    T visit(ExprOpFloatConst exprOpFloatConst, P arg) throws CompilerException;

    T visit(ExprOpStringConst exprOpStringConst, P arg) throws CompilerException;

    T visit(Identifier identifier, P arg) throws CompilerException;

    // Math Expression

    T visit(ExprOpMathPlus exprOpMathPlus, P arg) throws CompilerException;

    T visit(ExprOpMathSub exprOpMathSub, P arg) throws CompilerException;

    T visit(ExprOpMathTimes exprOpMathTimes, P arg) throws CompilerException;

    T visit(ExprOpMathDiv exprOpMathDiv, P arg) throws CompilerException;

    T visit(ExprOpMathMinus exprOpMathMinus, P arg) throws CompilerException;

    //Logic Expression

    T visit(ExprOpLogicAND exprOpLogicAND, P arg) throws CompilerException;

    T visit(ExprOpLogicOR exprOpLogicOR, P arg) throws CompilerException;

    T visit(ExprOpLogicNOT exprOpLogicNOT, P arg) throws CompilerException;

    //Relop

    T visit(ExprOpRelopGT exprOpRelopGT, P arg) throws CompilerException;

    T visit(ExprOpRelopGE exprOpRelopGE, P arg) throws CompilerException;

    T visit(ExprOpRelopLT exprOpRelopLT, P arg) throws CompilerException;

    T visit(ExprOpRelopLE exprOpRelopLE, P arg) throws CompilerException;

    T visit(ExprOpRelopEQ exprOpRelopEQ, P arg) throws CompilerException;

    T visit(ExprOpRelopNE exprOpRelopNE, P arg) throws CompilerException;
}

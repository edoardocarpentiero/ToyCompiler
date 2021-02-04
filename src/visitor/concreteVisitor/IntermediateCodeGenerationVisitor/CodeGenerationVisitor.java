package visitor.concreteVisitor.IntermediateCodeGenerationVisitor;

import exception.CompilerException;
import syntax.type.*;
import syntax.ASTNode;
import syntax.Program;
import syntax.VarDecl;
import syntax.expr.Expr;
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
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CodeGenerationVisitor implements Visitor<String, Object>{

    private StringJoiner declarationStruct = new StringJoiner(";\n");
    private String idProc="";
    private String librerie="#include <stdio.h>\n#include <stdbool.h>";
    private ArrayList<TypesVar> returnTypes;
    private Proc mainProcedure=null;
    private boolean hasMoreTypes =false;
    private String idCallProcName;
    private int countStruct =0;
    private StringJoiner initStruct;


    private void check(List<? extends ASTNode> nodes, StringJoiner joiner, Object arg){
        nodes.forEach(node -> {
            try {
                joiner.add(node.accept(this, arg));
            } catch (CompilerException e) {
                e.printStackTrace();
            }
        });

    }

    private String varDeclType(TypesVar typesVar){
        switch (typesVar){
            case INT: return "int";
            case BOOL: return "bool";
            case FLOAT: return "float";
            case VOID: return "void";
            case STRING: return "char *";
            default:
                return "";
        }
    }

    private String varDeclFormat(TypesVar typesVar){
        switch (typesVar){
            case INT: case BOOL: return "%d";
            case FLOAT: return "%f";
            case STRING: return "%s";
            default:
                return "";
        }
    }


    @Override
    public String visit(Program program, Object arg) throws CompilerException {

        StringJoiner variableDeclaration = new StringJoiner("\n ");
        if(program.getVarDecls()!=null){
            check(program.getVarDecls(),variableDeclaration,arg);
        }
        StringJoiner procedure = new StringJoiner("\n");
        check(program.getProcList(),procedure,arg);
        String sourceCode = librerie+"\n\n"+(declarationStruct.toString().length()==0?"\n": declarationStruct.toString()+";\n")+""+variableDeclaration.toString()+"\n\n"+procedure.toString();
        sourceCode+="\n"+mainProcedure.accept(this,arg);

        return sourceCode;
    }

    @Override
    public String visit(VarDecl varDecl, Object arg) throws CompilerException {
        StringJoiner stringJoiner= new StringJoiner(",");
        String type = varDecl.getType().accept(this,arg);
        check(varDecl.getIdListInit(),stringJoiner,arg);
        return type+" "+stringJoiner.toString()+";";
    }

    @Override
    public String visit(Type type, Object arg) {
        return varDeclType(type.getValueType());
    }

    @Override
    public String visit(IdInit idInit, Object arg) throws CompilerException {
        String id = idInit.getId().accept(this,arg);
        if(idInit.getExpr() != null)
            return id+"="+idInit.getExpr().accept(this,arg).replace(";","");
        return id;
    }

    @Override
    public String visit(Identifier identifier, Object arg) throws CompilerException {

        return identifier.getValue();
    }

    @Override
    public String visit(Proc proc, Object arg) throws CompilerException {
        String procedureTemplate= "{returnType} {idProcedure}({parameterList}){\n" +
                "{body}" +
                "}";
        idProc=proc.getIdentifier().accept(this,arg);
        returnTypes = ((TypesProcedure)proc.getIdentifier().getNodeType()).getResultTypes();

        StringJoiner parameterList = new StringJoiner(",");

        procedureTemplate=procedureTemplate.replace("{idProcedure}", idProc);
        if(idProc.equals("main")) {
            if(mainProcedure==null) {
                mainProcedure = proc;
                procedureTemplate = "";
            }
            else {
                procedureTemplate = procedureTemplate.replace("{returnType}", "int");
                procedureTemplate = procedureTemplate.replace("{parameterList}", "");
                procedureTemplate = procedureTemplate.replace("{body}", "\t"+proc.getBodyProc().accept(this, arg)+"return 0;\n");
            }
        }else{
            if(proc.getParamDeclList()!=null) {
                check(proc.getParamDeclList(), parameterList, arg);
                procedureTemplate=procedureTemplate.replace("{parameterList}",parameterList.toString());
            }else
                procedureTemplate=procedureTemplate.replace("{parameterList}","");

            if(returnTypes.size()==1 && returnTypes.get(0).equals(TypesVar.VOID))
                procedureTemplate=procedureTemplate.replace("{returnType}", "void");
            else if(returnTypes.size()==1)
                procedureTemplate=procedureTemplate.replace("{returnType}",varDeclType(returnTypes.get(0)));
            else{
                    String patternMoreTypes="typedef struct{\n{variabili}}type"+idProc;
                    hasMoreTypes =true;
                    String var="";
                    for(int i=0;i<returnTypes.size();i++)
                        var+= varDeclType(returnTypes.get(i))+" variable"+i+";\n";

                    patternMoreTypes = patternMoreTypes.replace("{variabili}", var);
                    declarationStruct.add(patternMoreTypes);
                procedureTemplate=procedureTemplate.replace("{returnType}", "type"+idProc);
            }
            procedureTemplate=procedureTemplate.replace("{body}", proc.getBodyProc().accept(this,arg));
            hasMoreTypes =false;
        }

        return procedureTemplate;
    }

    @Override
    public String visit(ParDecl parDecl, Object arg) throws CompilerException {
        String type = parDecl.getType().accept(this,arg);
        StringJoiner parameterId= new StringJoiner(",");
        for(Identifier result: parDecl.getIdentifiers())
            parameterId.add(type + " " + result.accept(this, arg));

        return parameterId.toString();
    }

    @Override
    public String visit(ResultType resultType, Object arg) {
        return null;
    }

    @Override
    public String visit(BodyProc bodyProc, Object arg) throws CompilerException {
        String bodyProcPattern = "" +
                "{declationStruct}" +
                "{varDecl}" +
                "{statementList}" +
                "{returnExpr}\n";
        StringJoiner varDeclList = new StringJoiner("\n");
        StringJoiner statementList = new StringJoiner("\n");
        initStruct = new StringJoiner(";\n");
        if(bodyProc.getVarDeclList()!=null) {
            check(bodyProc.getVarDeclList(), varDeclList, arg);
            bodyProcPattern = bodyProcPattern.replace("{varDecl}","\n"+varDeclList.toString());
        }
        else
            bodyProcPattern = bodyProcPattern.replace("{varDecl}","");
        if(bodyProc.getStatList()!=null) {
            check(bodyProc.getStatList(), statementList, arg);
            bodyProcPattern = bodyProcPattern.replace("{statementList}","\n\n"+statementList.toString());
        }
        else
            bodyProcPattern = bodyProcPattern.replace("{statementList}","");
        if(bodyProc.getReturnExprs()!=null){
            if(hasMoreTypes) {
                StringJoiner returnExpr = new StringJoiner(";\n");
                String assign="";
                for(int i=0;i<bodyProc.getReturnExprs().size();i++){
                    if(bodyProc.getReturnExprs().get(i) instanceof CallProc){
                        CallProc callProc = (CallProc) bodyProc.getReturnExprs().get(i);
                        String idCallProc = callProc.accept(this, arg).replace(";","");
                        int size = ((TypesProcedure)callProc.getNodeType()).getResultTypes().size();
                        if(size>1) {
                            String assignStruct = "type"+idCallProcName+" returnVal"+(++countStruct)+" = "+idCallProc;
                            initStruct.add(assignStruct);
                            for (int j = 0; j < size; j++) {
                                assign = "returnVal.variable" + (j+i) + "= returnVal" + countStruct + ".variable" + (j);
                                returnExpr.add(assign);
                            }
                        }else {
                            assign = "returnVal.variable" + i + "=" + callProc.accept(this, arg);
                            returnExpr.add(assign);
                        }
                    }else {
                        assign = "returnVal.variable" + i + "=" + bodyProc.getReturnExprs().get(i).accept(this,arg);
                        returnExpr.add(assign);
                    }
                }
                bodyProcPattern = bodyProcPattern.replace("{returnExpr}", "\n\n"+returnExpr.toString()+";\nreturn returnVal;");
            }
            else
                bodyProcPattern = bodyProcPattern.replace("{returnExpr}", "\n\nreturn " + bodyProc.getReturnExprs().get(0).accept(this,arg) + ";");
        }
        else
            bodyProcPattern = bodyProcPattern.replace("{returnExpr}","\n");

        if(hasMoreTypes || initStruct.length()!=0)
            bodyProcPattern = bodyProcPattern.replace("{declationStruct}",(!idProc.equals("main") && hasMoreTypes?"type"+idProc+" returnVal;\n":"")+ initStruct.toString()+((initStruct.length()>1)?";":""));//vvariabile della struttura
        else
            bodyProcPattern = bodyProcPattern.replace("{declationStruct}","");

        return bodyProcPattern;
    }

    @Override
    public String visit(IfStatement statement, Object arg) throws CompilerException {
        String ifStatementPattern="\n" +
                "if({condition}){\n" +
                "\tthenStatement\n" +
                "}{elifStatement}" +
                "{elseStatement}";
        StringJoiner statementsIf = new StringJoiner("\n\t");
        StringJoiner statementelif = new StringJoiner("\n\t");

        ifStatementPattern = ifStatementPattern.replace("{condition}",statement.getCondition().accept(this,arg));


        check(statement.getThenStatement(),statementsIf,arg);
        ifStatementPattern=ifStatementPattern.replace("thenStatement",statementsIf.toString());


        if(statement.getElifListStatement()!=null){
            check(statement.getElifListStatement(),statementelif,arg);
            ifStatementPattern=ifStatementPattern.replace("{elifStatement}",statementelif.toString());
        }else
            ifStatementPattern=ifStatementPattern.replace("{elifStatement}","");

        if(statement.getElseStatement()!=null)
            ifStatementPattern=ifStatementPattern.replace("{elseStatement}",statement.getElseStatement().accept(this,arg));
        else
            ifStatementPattern=ifStatementPattern.replace("{elseStatement}","");

        return ifStatementPattern;
    }

    @Override
    public String visit(ElifStatement elifStatement, Object arg) throws CompilerException {
        StringJoiner statementelif = new StringJoiner("\n\t");
        String elifStatementPattern="" +
                "else if({condition}){\n" +
                "\tthenStatement" +
                "\n}";
        elifStatementPattern = elifStatementPattern.replace("{condition}",elifStatement.getCondition().accept(this,arg));

        check(elifStatement.getStatList(), statementelif, arg);
        elifStatementPattern = elifStatementPattern.replace("thenStatement",statementelif.toString());


        return elifStatementPattern;
    }

    @Override
    public String visit(ElseStatement elseStatement, Object arg) throws CompilerException {
        StringJoiner statementelse = new StringJoiner("\n\t");
        String elseStatementPattern="" +
                "else {\n" +
                "\tthenStatement" +
                "\n}";

        check(elseStatement.getStatList(),statementelse,arg);
        elseStatementPattern = elseStatementPattern.replace("thenStatement",statementelse.toString());

        return elseStatementPattern;
    }

    @Override
    public String visit(WhileStatement whileStatement, Object arg) throws CompilerException {
        String whileStatementPatter = "" +
                "{statement}" +
                "\nwhile({condition}){\n" +
                "{statementDo}\n" +
                "{statement}" +
                "\n}";
        StringJoiner statementDo = new StringJoiner("\n");
        StringJoiner statement = new StringJoiner("\n");
        whileStatementPatter = whileStatementPatter.replace("{condition}",whileStatement.getCondition().accept(this,arg));

        if(whileStatement.getStatList()!=null){
            check(whileStatement.getStatList(),statement,arg);
            whileStatementPatter = whileStatementPatter.replace("{statement}",statement.toString());
        }
        else
            whileStatementPatter = whileStatementPatter.replace("{statement}\n","");
        check(whileStatement.getStatListDo(),statementDo,arg);
        whileStatementPatter = whileStatementPatter.replace("{statementDo}",statementDo.toString());
        return whileStatementPatter;
    }

    @Override
    public String visit(ReadlnStatement readlnStatement, Object arg) throws CompilerException {
        String readlnStatementPattern = "scanf(\"{formato}\",{variables});";
        StringJoiner variable = new StringJoiner(",");
        String formato ="";
        for(Expr expr : readlnStatement.getIdList()){
                formato += varDeclFormat((TypesVar) expr.getNodeType());
                variable.add("&"+expr.accept(this,arg));
        }
        readlnStatementPattern = readlnStatementPattern.replace("{formato}",formato);
        readlnStatementPattern = readlnStatementPattern.replace("{variables}",variable.toString());

        return readlnStatementPattern;
    }

    @Override
    public String visit(WriteStatement writeStatement, Object arg) throws CompilerException {
        String writeStatementPattern = "printf({messaggio}{variabiliOutput});";
        StringJoiner variable = new StringJoiner(",");
        String message="\"";
        for(Expr expr: writeStatement.getExprList()){
            if(expr instanceof ExprOpStringConst)
                message += expr.accept(this,arg).replace("\"","");
            else {
                if(expr instanceof CallProc){//verificare gestione
                    CallProc callP = (CallProc)expr;
                        ArrayList<TypesVar> typesVars = ((TypesProcedure)callP.getIdValue().getNodeType()).getResultTypes();
                        String callNameProc = callP.accept(this,arg).replace(";","");
                        if(typesVars.size()>1) {
                            String assignStruct = "type"+idCallProcName+" returnVal"+(++countStruct)+" = "+callNameProc;
                            initStruct.add(assignStruct);
                            for (int i = 0; i < typesVars.size(); i++) {
                                message += varDeclFormat(typesVars.get(i));
                                variable.add("returnVal"+countStruct+".variable" + i);
                            }
                        }else{
                            message += varDeclFormat(typesVars.get(0));
                            variable.add(callNameProc);
                        }
                }else {

                    message += varDeclFormat((TypesVar) expr.getNodeType());
                    variable.add(expr.accept(this, arg));
                }
            }
        }


        writeStatementPattern = writeStatementPattern.replace("{messaggio}",message+"\"");
        if(variable.length()==0)
            writeStatementPattern = writeStatementPattern.replace("{variabiliOutput}","");
        else
            writeStatementPattern = writeStatementPattern.replace("{variabiliOutput}",", "+variable.toString());

        return writeStatementPattern;
    }

    @Override
    public String visit(AssignStatement assignStatement, Object arg) throws CompilerException {
        StringJoiner assignment = new StringJoiner(";\n");
        String assign="";
        for(int i=0;i<assignStatement.getExprList().size();i++){
            if(assignStatement.getExprList().get(i) instanceof CallProc){
                CallProc callProc = (CallProc) assignStatement.getExprList().get(i);
                int size = ((TypesProcedure)callProc.getNodeType()).getResultTypes().size();
                String callNameProc = callProc.accept(this,arg).replace(";","");
                if(size>1) {
                    assign = "type"+idCallProcName+" returnVal"+(++countStruct)+" = "+callNameProc;
                    initStruct.add(assign);
                    for (int j = 0; j < size; j++) {
                        assign=assignStatement.getIdList().get(j+i).accept(this,arg)+" = returnVal"+countStruct+".variable"+j;
                        assignment.add(assign);
                    }
                }else {
                    assign = assignStatement.getIdList().get(i).accept(this, arg) + " = " + callProc.accept(this, arg).replace(";","");
                    assignment.add(assign);
                }
            }else {
                assign = assignStatement.getIdList().get(i).accept(this,arg)+"=" + assignStatement.getExprList().get(i).accept(this,arg);
                assignment.add(assign);
            }
        }

        return assignment.toString()+";\n";
    }

    @Override
    public String visit(CallProc callProc, Object arg) throws CompilerException {
        String patternCallProc = "{id}({listaParametri});";
        StringJoiner param = new StringJoiner(",");

        String id = callProc.getIdValue().accept(this,arg);
        idCallProcName = id;
        patternCallProc = patternCallProc.replace("{id}",id);

        if(callProc.getExprList()!=null) {
            for(Expr expr : callProc.getExprList()){
                if(expr instanceof CallProc){
                    CallProc callP = (CallProc) expr;
                    int size = ((TypesProcedure)callP.getNodeType()).getResultTypes().size();
                    String callNameProc = callP.accept(this,arg).replace(";","");
                    if(size>1) {
                        initStruct.add("type"+idCallProcName+" returnVal"+(++countStruct)+" = "+callNameProc);
                        for (int i = 0; i < size; i++)
                            param.add("returnVal"+countStruct+".variable" + i);
                    }else
                        param.add(callNameProc);
                }else
                    param.add(expr.accept(this,arg));
            }
            patternCallProc = patternCallProc.replace("{listaParametri}", param.toString());
        }else
            patternCallProc = patternCallProc.replace("{listaParametri}", "");


        return patternCallProc;
    }

    @Override
    public String visit(ExprOpNull exprOpNull, Object arg) throws CompilerException {
        return "null";
    }

    @Override
    public String visit(ExprOpTrue exprOpTrue, Object arg) throws CompilerException {
        return String.valueOf(exprOpTrue.getValue());
    }

    @Override
    public String visit(ExprOpFalse exprOpFalse, Object arg) throws CompilerException {
        return String.valueOf(exprOpFalse.getValue());
    }

    @Override
    public String visit(ExprOpIntConst exprOpIntConst, Object arg) throws CompilerException {
        return String.valueOf(exprOpIntConst.getValue());
    }

    @Override
    public String visit(ExprOpFloatConst exprOpFloatConst, Object arg) throws CompilerException {
        return String.valueOf(exprOpFloatConst.getValue());
    }

    @Override
    public String visit(ExprOpStringConst exprOpStringConst, Object arg) throws CompilerException {
        return "\""+exprOpStringConst.getValue()+"\"";//VERIFICARE
    }

    @Override
    public String visit(ExprOpMathPlus exprOpMathPlus, Object arg) throws CompilerException {
        String expr = exprOpMathPlus.getLeftOperand().accept(this,arg).replace(";","");
        expr +="+"+exprOpMathPlus.getRightOperand().accept(this,arg).replace(";","");
        return expr;
    }

    @Override
    public String visit(ExprOpMathSub exprOpMathSub, Object arg) throws CompilerException {
        String expr = exprOpMathSub.getLeftOperand().accept(this,arg).replace(";","");;
        expr +="-"+exprOpMathSub.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpMathTimes exprOpMathTimes, Object arg) throws CompilerException {
        String expr = exprOpMathTimes.getLeftOperand().accept(this,arg).replace(";","");;
        expr +="*"+exprOpMathTimes.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpMathDiv exprOpMathDiv, Object arg) throws CompilerException {
        String expr = exprOpMathDiv.getLeftOperand().accept(this,arg).replace(";","");;
        expr +="/"+exprOpMathDiv.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpMathMinus exprOpMathMinus, Object arg) throws CompilerException {
        String expr = "-"+exprOpMathMinus.getOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpLogicAND exprOpLogicAND, Object arg) throws CompilerException {
        String expr = exprOpLogicAND.getLeftOperand().accept(this,arg).replace(";","");;
        expr +="&&"+exprOpLogicAND.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpLogicOR exprOpLogicOR, Object arg) throws CompilerException {
        String expr = exprOpLogicOR.getLeftOperand().accept(this,arg).replace(";","");;
        expr +="||"+exprOpLogicOR.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpLogicNOT exprOpLogicNOT, Object arg) throws CompilerException {
        String expr = "!"+exprOpLogicNOT.getOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpRelopGT exprOpRelopGT, Object arg) throws CompilerException {
        TypesVar left = typeCheckOperation(exprOpRelopGT.getLeftOperand().getNodeType());
        TypesVar right = typeCheckOperation(exprOpRelopGT.getRightOperand().getNodeType());
        String expr1 = exprOpRelopGT.getLeftOperand().accept(this,arg).replace(";","");
        String expr2 = exprOpRelopGT.getRightOperand().accept(this,arg).replace(";","");

        String confronto = "";
        if((left!=null && right!=null)) {
            if (left.equals(TypesVar.STRING) && right.equals(TypesVar.STRING)) {
                confronto = "strcmp(" + expr1 + "," + expr2 + ")>0";
            } else
                confronto = expr1 + ">" + expr2;
        }

        return confronto;
    }

    @Override
    public String visit(ExprOpRelopGE exprOpRelopGE, Object arg) throws CompilerException {
        String expr = exprOpRelopGE.getLeftOperand().accept(this,arg).replace(";","");;
        expr +=">="+exprOpRelopGE.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }


    @Override
    public String visit(ExprOpRelopLT exprOpRelopLT, Object arg) throws CompilerException {
        TypesVar left = typeCheckOperation(exprOpRelopLT.getLeftOperand().getNodeType());
        TypesVar right = typeCheckOperation(exprOpRelopLT.getRightOperand().getNodeType());


            String expr1 = exprOpRelopLT.getLeftOperand().accept(this, arg).replace(";","");
            String expr2 = exprOpRelopLT.getRightOperand().accept(this, arg).replace(";","");

            String confronto = "";
            if((left!=null && right!=null)) {

            if (left.equals(TypesVar.STRING) && right.equals(TypesVar.STRING)) {
                confronto = "strcmp(" + expr1 + "," + expr2 + ")<==>0";
            } else
                confronto = expr1 + "<" + expr2;
        }

        return confronto;
    }

    @Override
    public String visit(ExprOpRelopLE exprOpRelopLE, Object arg) throws CompilerException {
        String expr = exprOpRelopLE.getLeftOperand().accept(this,arg).replace(";","");;
        expr +="<="+exprOpRelopLE.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    @Override
    public String visit(ExprOpRelopEQ exprOpRelopEQ, Object arg) throws CompilerException {
        TypesVar left = typeCheckOperation(exprOpRelopEQ.getLeftOperand().getNodeType());
        TypesVar right = typeCheckOperation(exprOpRelopEQ.getRightOperand().getNodeType());
        String expr1 = exprOpRelopEQ.getLeftOperand().accept(this,arg).replace(";","");;
        String expr2 = exprOpRelopEQ.getRightOperand().accept(this,arg).replace(";","");;

        String confronto = "";
        if((left!=null && right!=null)) {
            if (left.equals(TypesVar.STRING) && right.equals(TypesVar.STRING)) {
                confronto = "strcmp(" + expr1 + "," + expr2 + ")==0";
            } else
                confronto = expr1 + "==" + expr2;
        }

        return confronto;
    }

    @Override
    public String visit(ExprOpRelopNE exprOpRelopNE, Object arg) throws CompilerException {
        String expr = exprOpRelopNE.getLeftOperand().accept(this,arg).replace(";","");
        expr +="!="+exprOpRelopNE.getRightOperand().accept(this,arg).replace(";","");;
        return expr;
    }

    private TypesVar typeCheckOperation(NodeType nodeType) {
        if (nodeType instanceof TypesProcedure) {
            if (((TypesProcedure) nodeType).getResultTypes().size() != 1)
                return null;
            else
                return ((TypesProcedure) nodeType).getResultTypes().get(0);
        } else
            return (TypesVar) nodeType;
    }




}

package visitor.concreteVisitor.SemanticVisitor;

import exception.*;
import syntax.type.NodeType;
import syntax.type.TypesProcedure;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.SymbolTableEntry;
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
import syntax.type.ResultType;
import syntax.type.Type;
import syntax.type.TypesVar;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TypeCheckingVisitor implements Visitor<NodeType, SymbolTable> {



    private ArrayList<TypesVar> resultTypes;
    private String nameProc;
    private ArrayList<String> errorReport = new ArrayList<>();

    private Consumer<? super ASTNode> typeCheck(SymbolTable arg)  {
        return (ASTNode node) -> {
            try {
                node.accept(this, arg);
            } catch (CompilerException e) {
                errorReport.add(e.getMessage()+"\n"+e.getCause());
            }
        };
    }

    private void visitList(ArrayList<Expr> exprList, ArrayList<TypesVar> variables){
        int size=exprList.size();

        for (int i=0;i<size;i++){
            if(exprList.get(i) instanceof CallProc){
                CallProc callP = (CallProc)exprList.get(i);
                int size1 = ((TypesProcedure)callP.getNodeType()).getResultTypes().size();
                for(int j=0; j<size1;j++)
                    variables.add(((TypesProcedure)callP.getNodeType()).getResultTypes().get(j));
            }else
                variables.add((TypesVar) exprList.get(i).getNodeType());
        }
    }

    private TypesVar typeCheckOperation(NodeType nodeType){
        if(nodeType instanceof TypesProcedure){

            if(((TypesProcedure)nodeType).getResultTypes().size()!=1)
                return null;
            else
                return  ((TypesProcedure)nodeType).getResultTypes().get(0);
        }
        else
            return (TypesVar) nodeType;
    }





    private boolean errorsReport(){
        if(errorReport.size()==0)
            return false;
        for(String error: errorReport)
            System.err.println(error);
        return true;
    }

    @Override
    public NodeType visit(Program program, SymbolTable arg) {
        arg.enterScope();
        if(program.getVarDecls()!=null)
            program.getVarDecls().forEach(typeCheck(arg));
        program.getProcList().forEach(typeCheck(arg));
        if(!errorsReport())
            return TypesVar.VOID;
        arg.exitScope();
        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(VarDecl varDecl, SymbolTable arg)  {

        varDecl.getIdListInit().forEach(typeCheck(arg));

        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(IdInit idInit, SymbolTable arg) throws CompilerException {
        TypesVar typeVarId = (TypesVar) idInit.getId().accept(this,arg);

        if(idInit.getExpr()!=null) {
            NodeType nodeType = idInit.getExpr().accept(this, arg);
            TypesVar typeVarExp = null;
            //se l'espressione è una call proc e non ha un numero di ResultType maggiore di uno 1
            if(nodeType instanceof TypesProcedure) {
                if(((TypesProcedure) nodeType).getResultTypes().size()==1)
                    typeVarExp = ((TypesProcedure) nodeType).getResultTypes().get(0);
                else
                    throw new CompilerException("Type Checking Error "+idInit.getClass().getSimpleName()+"! Line: "+(idInit.getLeft().getLine()-1)+" - Col:"+idInit.getRight().getColumn(),
                            new TypeMismatchException("The type of the variable is incompatible! CallProc has 0 or more (2+) result types!" ));
            }
            else
                typeVarExp = (TypesVar) nodeType;
            if(typeVarId.verifyAssign(typeVarExp)==TypesVar.NULL) {
                throw new CompilerException("Type Checking Error "+idInit.getClass().getSimpleName()+"! Line: "+(idInit.getLeft().getLine()-1)+" - Col:"+idInit.getRight().getColumn(),
                        new TypeMismatchException("The type of the variable is incompatible! The variable \""+idInit.getId().getValue()+"\" expected " +
                                "" +typeVarId+ ", actual " +typeVarExp+ "."));
            }
        }

        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(AssignStatement assignStatement, SymbolTable arg) throws CompilerException {

        assignStatement.getIdList().forEach(this.typeCheck(arg));
        ArrayList<Identifier> identifiers=assignStatement.getIdList();

        assignStatement.getExprList().forEach(this.typeCheck(arg));
        ArrayList<TypesVar> variables = new ArrayList<>();

        visitList(assignStatement.getExprList(), variables);

        int size=identifiers.size();

        if(size != variables.size()){
            throw new CompilerException("Type Checking Error "+assignStatement.getClass().getSimpleName()+"! Line: "+(assignStatement.getLeft().getLine()-1)+" - Col:"+assignStatement.getRight().getColumn(),
                    new VariableMismatchException("The number of variables is incompatible! Expected " +size+ ", actual " +variables.size()+ "."));
        }

        for(int i=0;i<size;i++){
            if(((TypesVar)identifiers.get(i).getNodeType()).verifyAssign(variables.get(i)).equals(TypesVar.NULL))
                throw new CompilerException("Type Checking Error "+assignStatement.getClass().getSimpleName()+"! Line: "+(assignStatement.getLeft().getLine()-1)+" - Col:"+assignStatement.getRight().getColumn(),
                        new TypeMismatchException("The type of the variable is incompatible! The variable \""+identifiers.get(i).getValue()+ "\" expected " +
                        "" +identifiers.get(i).getNodeType()+ ", actual " +variables.get(i)+ "."));

        }

        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(CallProc callProc, SymbolTable arg) throws CompilerException {
        NodeType nodeType = callProc.getIdValue().accept(this,arg);
        callProc.setNodeType(nodeType);

        ArrayList<TypesVar> variables = new ArrayList<>();
        int size;

        if(callProc.getExprList()!=null ){
            callProc.getExprList().forEach(typeCheck(arg));
            visitList(callProc.getExprList(),variables);
            size = variables.size();
        }
        else
            size = 0;

        ArrayList<TypesVar> typeParamProc = ((TypesProcedure)nodeType).getParamTypes();

        if(typeParamProc.size() != size){
            throw new CompilerException("Type Checking Error "+callProc.getClass().getSimpleName()+" - "+callProc.getIdValue().getValue()+"! Line: "+(callProc.getLeft().getLine()-1)+" - Col:"+callProc.getRight().getColumn(),
                    new VariableMismatchException("The number of variables is incompatible! Expected " +typeParamProc.size()+ ", actual " +size+ "."));
        }else {
            for (int i = 0; i < size; i++) {
                if(typeParamProc.get(i).verifyAssign(variables.get(i)).equals(TypesVar.NULL))
                    throw new CompilerException("Type Checking Error "+callProc.getClass().getSimpleName()+" - "+callProc.getIdValue().getValue()+"! Line: "+(callProc.getLeft().getLine()-1)+" - Col:"+callProc.getRight().getColumn(),
                            new TypeMismatchException("The type of the variable is incompatible! Expected " +
                                    "" +typeParamProc.get(i)+ ", actual " +variables.get(i)+ "."));
            }
        }
        return ((TypesProcedure)nodeType);
    }


    @Override
    public NodeType visit(Identifier identifier, SymbolTable arg) {
        String variable = identifier.getValue();
        SymbolTableEntry entry = arg.lookup(variable);
        NodeType typeNode = entry.getTypesEntry();

        identifier.setNodeType(typeNode);
        return typeNode;
    }


    @Override
    public NodeType visit(Type type, SymbolTable arg) {
        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(Proc proc, SymbolTable arg) throws CompilerException {
        arg.enterScope();

        resultTypes = ((TypesProcedure)proc.getIdentifier().accept(this,arg)).getResultTypes(); //Restituico l'elenco dei tipi da restituire da utilizzare nella verifica in BodyProc
        nameProc = proc.getIdentifier().getValue();

        if(nameProc.equals("main")) {
            if(proc.getParamDeclList()!=null)
                throw new CompilerException("Type Checking Error " + proc.getClass().getSimpleName() + " - " + nameProc + "! Line: " + (proc.getLeft().getLine() - 1) + " - " +
                        "Col:" + proc.getRight().getColumn(),
                        new ProcedureDeclarationException("The parameter list of \'main\' procedure must be empty!"));
            else if((resultTypes.size()!=1) || !resultTypes.get(0).equals(TypesVar.VOID))
                throw new CompilerException("Type Checking Error " + proc.getClass().getSimpleName() + " - " + nameProc + "! Line: " + (proc.getLeft().getLine() - 1) + " - " +
                        "Col:" + proc.getRight().getColumn(),
                        new ProcedureDeclarationException("The return type of \'main\' procedure must be type \'void\'!"));
        }
        if(proc.getParamDeclList()!=null)
            proc.getParamDeclList().forEach(typeCheck(arg));


        proc.getBodyProc().accept(this,arg);

        arg.exitScope();
        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(ParDecl parDecl, SymbolTable arg) {

        parDecl.getIdentifiers().forEach(typeCheck(arg));

        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(ResultType resultType, SymbolTable arg) {
        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(BodyProc bodyProc, SymbolTable arg) throws CompilerException {

        if(bodyProc.getVarDeclList()!=null)
            bodyProc.getVarDeclList().forEach(typeCheck(arg));

        if(bodyProc.getStatList()!=null)
            bodyProc.getStatList().forEach(typeCheck(arg));

        if(bodyProc.getReturnExprs()!=null) {
            int size=0;
            bodyProc.getReturnExprs().forEach(typeCheck(arg));

            ArrayList<TypesVar> variables = new ArrayList<>();
            visitList(bodyProc.getReturnExprs(),variables);

            size = variables.size();


            if(typeCheckingVoid(resultTypes)>1){
                throw new CompilerException("Type Checking Error "+bodyProc.getClass().getSimpleName()+" - "+nameProc+"! Line: " +(bodyProc.getLeft().getLine()-1)+" - " +
                        "Col:"+bodyProc.getRight().getColumn(),
                        new VariableMismatchException("Multiple type VOID!"));
            }

            if(size == resultTypes.size()){
                for(int i=0;i<size;i++) {
                    if (resultTypes.get(i).verifyAssign(variables.get(i))==TypesVar.NULL)
                        throw new CompilerException("Type Checking Error "+bodyProc.getClass().getSimpleName()+" - "+nameProc+"! Line: " +(bodyProc.getLeft().getLine()-1)+" - " +
                                "Col:"+bodyProc.getRight().getColumn(),
                                new TypeMismatchException("The type of the variable is incompatible! Expected " +
                                        "" + resultTypes.get(i)+ ", actual " +variables.get(i)+ "."));

                }
            }else
                throw new CompilerException("Type Checking Error "+bodyProc.getClass().getSimpleName()+" - "+nameProc+"! Line: " +(bodyProc.getLeft().getLine()-1)+" - " +
                        "Col:"+bodyProc.getRight().getColumn(),
                        new VariableMismatchException("The number of variables is incompatible! Expected " +resultTypes.size()+ ", actual " +size+ "."));
        }
        else if(resultTypes.size()!=0 && !resultTypes.get(0).equals(TypesVar.VOID))
            throw new CompilerException("Type Checking Error "+bodyProc.getClass().getSimpleName()+" - "+nameProc+"! Line: " +(bodyProc.getLeft().getLine()-1)+" - " +
                    "Col:"+bodyProc.getRight().getColumn(),
                    new VariableMismatchException("The number of variables is incompatible! Expected " +resultTypes.size()+ ", actual 0."));
        return TypesVar.NULL;
    }


    @Override
    public NodeType visit(IfStatement statement, SymbolTable arg) throws CompilerException {
        NodeType typeCondition = statement.getCondition().accept(this,arg);
        TypesVar typeVar = typeCheckOperation(typeCondition);

        if(typeVar!=null && !typeCondition.equals(TypesVar.NULL)) {
            statement.getThenStatement().forEach(typeCheck(arg));
            if(statement.getElifListStatement()!=null)
                statement.getElifListStatement().forEach(typeCheck(arg));

            if(statement.getElseStatement()!=null)
                statement.getElseStatement().accept(this,arg);
        }
        else
            throw new CompilerException("Type Checking Error "+statement.getClass().getSimpleName()+"! Line: "+(statement.getLeft().getLine()-1)+" " +
                    "- Col:"+statement.getRight().getColumn(),
                    new ConditionException(("The type of the condition is incompatible! Expected " +
                            "" +TypesVar.BOOL+ ", actual " +typeCondition+ ".")));

        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(ElifStatement elifStatement, SymbolTable arg) throws CompilerException {

        NodeType typeCondition = elifStatement.getCondition().accept(this,arg);
        TypesVar typeVar = typeCheckOperation(typeCondition);
        if(typeVar!=null && !typeCondition.equals(TypesVar.NULL))
            elifStatement.getStatList().forEach(typeCheck(arg));
        else
            throw new CompilerException("Type Checking Error "+elifStatement.getClass().getSimpleName()+"! Line: "+(elifStatement.getLeft().getLine()-1)+" " +
                    "- Col:"+elifStatement.getRight().getColumn(),
                    new ConditionException(("The type of the condition is incompatible! Expected " +
                            "" +TypesVar.BOOL+ ", actual " +typeCondition+ ".")));

        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(ElseStatement elseStatement, SymbolTable arg) {
        elseStatement.getStatList().forEach(typeCheck(arg));
        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(WhileStatement whileStatement, SymbolTable arg) throws CompilerException {
        NodeType typeCondition =  whileStatement.getCondition().accept(this,arg);
        TypesVar typeVar = typeCheckOperation(typeCondition);
        if(typeVar!=null && !typeCondition.equals(TypesVar.NULL)){
            if(whileStatement.getStatList()!=null)
                whileStatement.getStatList().forEach(typeCheck(arg));
            whileStatement.getStatListDo().forEach(typeCheck(arg));
        }
        else
            throw new CompilerException("Type Checking Error "+whileStatement.getClass().getSimpleName()+"! Line: "+(whileStatement.getLeft().getLine()-1)+" " +
                    "- Col:"+whileStatement.getRight().getColumn(),
                    new ConditionException(("The type of the condition is incompatible! Expected " +
                            "" +TypesVar.BOOL+ ", actual " +typeCondition+ ".")));

        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(ReadlnStatement readlnStatement, SymbolTable arg) {
        readlnStatement.getIdList().forEach(typeCheck(arg));
        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(WriteStatement writeStatement, SymbolTable arg) {
        writeStatement.getExprList().forEach(typeCheck(arg));
        return TypesVar.NULL;
    }

    @Override
    public NodeType visit(ExprOpNull exprOpNull, SymbolTable arg) {
        exprOpNull.setNodeType(TypesVar.NULL);
        return exprOpNull.getNodeType();
    }

    @Override
    public NodeType visit(ExprOpTrue exprOpTrue, SymbolTable arg) {
        exprOpTrue.setNodeType(TypesVar.BOOL);
        return exprOpTrue.getNodeType();
    }

    @Override
    public NodeType visit(ExprOpFalse exprOpFalse, SymbolTable arg) {
        exprOpFalse.setNodeType(TypesVar.BOOL);
        return exprOpFalse.getNodeType();
    }

    @Override
    public NodeType visit(ExprOpIntConst exprOpIntConst, SymbolTable arg) {
        exprOpIntConst.setNodeType(TypesVar.INT);
        return exprOpIntConst.getNodeType();
    }

    @Override
    public NodeType visit(ExprOpFloatConst exprOpFloatConst, SymbolTable arg) {
        exprOpFloatConst.setNodeType(TypesVar.FLOAT);
        return exprOpFloatConst.getNodeType();
    }

    @Override
    public NodeType visit(ExprOpStringConst exprOpStringConst, SymbolTable arg) {
        exprOpStringConst.setNodeType(TypesVar.STRING);
        return exprOpStringConst.getNodeType();
    }



    @Override
    public NodeType visit(ExprOpMathPlus exprOpMathPlus, SymbolTable arg) throws CompilerException {
        NodeType nodeLeft = exprOpMathPlus.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpMathPlus.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpMathPlus.getClass().getSimpleName()+"! Line: " + (exprOpMathPlus.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathPlus.getRight().getColumn(),
                    new ProcedureDeclarationException("The SUM operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyAddition(nodeTypeR);
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpMathPlus.getClass().getSimpleName()+"! Line: " + (exprOpMathPlus.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathPlus.getRight().getColumn(),
                    new TypeMismatchException(("The SUM operation cannot be applied!")));
        }
        exprOpMathPlus.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpMathSub exprOpMathSub, SymbolTable arg) throws CompilerException {
        NodeType nodeLeft = exprOpMathSub.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpMathSub.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpMathSub.getClass().getSimpleName()+"! Line: " + (exprOpMathSub.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathSub.getRight().getColumn(),
                    new ProcedureDeclarationException("The SUB operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifySubtraction(nodeTypeR);
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpMathSub.getClass().getSimpleName()+"! Line: " + (exprOpMathSub.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathSub.getRight().getColumn(),
                    new TypeMismatchException(("The SUB operation cannot be applied!")));
        }
        exprOpMathSub.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpMathTimes exprOpMathTimes, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpMathTimes.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpMathTimes.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpMathTimes.getClass().getSimpleName()+"! Line: " + (exprOpMathTimes.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathTimes.getRight().getColumn(),
                    new ProcedureDeclarationException("The TIMES operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyMultiplication(nodeTypeR);
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpMathTimes.getClass().getSimpleName()+"! Line: " + (exprOpMathTimes.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathTimes.getRight().getColumn(),
                    new TypeMismatchException(("The TIMES operation cannot be applied!")));
        }
        exprOpMathTimes.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpMathDiv exprOpMathDiv, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpMathDiv.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpMathDiv.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpMathDiv.getClass().getSimpleName()+"! Line: " + (exprOpMathDiv.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathDiv.getRight().getColumn(),
                    new ProcedureDeclarationException("The DIV operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyDivision(nodeTypeR);
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpMathDiv.getClass().getSimpleName()+"! Line: " + (exprOpMathDiv.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathDiv.getRight().getColumn(),
                    new TypeMismatchException(("The DIV operation cannot be applied!")));
        }
        exprOpMathDiv.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpMathMinus exprOpMathMinus, SymbolTable arg) throws CompilerException {

        NodeType node = exprOpMathMinus.getOperand().accept(this,arg);
        TypesVar nodeType = typeCheckOperation(node);

        if(nodeType == null)
            throw new CompilerException("Type Checking Error "+exprOpMathMinus.getClass().getSimpleName()+"! Line: " + (exprOpMathMinus.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathMinus.getRight().getColumn(),
                    new ProcedureDeclarationException("MINUS operation cannot be applied!"));


        if(nodeType.verifyOneOperator().equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpMathMinus.getClass().getSimpleName()+"! Line: " + (exprOpMathMinus.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpMathMinus.getRight().getColumn(),
                    new TypeMismatchException(("MINUS operation cannot be applied! Expeted:"+TypesVar.INT+" Actual: "+node )));
        }
        exprOpMathMinus.setNodeType(nodeType);
        return nodeType;
    }

    @Override
    public NodeType visit(ExprOpLogicAND exprOpLogicAND, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpLogicAND.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpLogicAND.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpLogicAND.getClass().getSimpleName()+"! Line: " + (exprOpLogicAND.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpLogicAND.getRight().getColumn(),
                    new ProcedureDeclarationException("The AND operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,null);
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpLogicAND.getClass().getSimpleName()+"! Line: " + (exprOpLogicAND.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpLogicAND.getRight().getColumn(),
                    new TypeMismatchException(("AND operation cannot be applied!")));
        }
        exprOpLogicAND.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpLogicOR exprOpLogicOR, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpLogicOR.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpLogicOR.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpLogicOR.getClass().getSimpleName()+"! Line: " + (exprOpLogicOR.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpLogicOR.getRight().getColumn(),
                    new ProcedureDeclarationException("The OR operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,null);
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpLogicOR.getClass().getSimpleName()+"! Line: " + (exprOpLogicOR.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpLogicOR.getRight().getColumn(),
                    new TypeMismatchException(("OR operation cannot be applied!")));
        }
        exprOpLogicOR.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpLogicNOT exprOpLogicNOT, SymbolTable arg) throws CompilerException {
        NodeType node = exprOpLogicNOT.getOperand().accept(this,arg);
        TypesVar nodeType = typeCheckOperation(node);

        if(nodeType == null)
            throw new CompilerException("Type Checking Error "+exprOpLogicNOT.getClass().getSimpleName()+"! Line: " + (exprOpLogicNOT.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpLogicNOT.getRight().getColumn(),
                    new ProcedureDeclarationException("NOT operation cannot be applied!"));


        if(nodeType.verifyOneOperator().equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpLogicNOT.getClass().getSimpleName()+"! Line: " + (exprOpLogicNOT.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpLogicNOT.getRight().getColumn(),
                    new TypeMismatchException(("NOT operation cannot be applied! Expected:"+TypesVar.BOOL+" Actual: "+node )));
        }
        exprOpLogicNOT.setNodeType(nodeType);
        return nodeType;

    }

    @Override
    public NodeType visit(ExprOpRelopGT exprOpRelopGT, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpRelopGT.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpRelopGT.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpRelopGT.getClass().getSimpleName()+"! Line: " + (exprOpRelopGT.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopGT.getRight().getColumn(),
                    new ProcedureDeclarationException("GT operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,"GT");
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpRelopGT.getClass().getSimpleName()+"! Line: " + (exprOpRelopGT.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopGT.getRight().getColumn(),
                    new TypeMismatchException(("GT operation cannot be applied!")));
        }
        exprOpRelopGT.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpRelopGE exprOpRelopGE, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpRelopGE.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpRelopGE.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpRelopGE.getClass().getSimpleName()+"! Line: " + (exprOpRelopGE.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopGE.getRight().getColumn(),
                    new ProcedureDeclarationException("GE operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,"GE");
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpRelopGE.getClass().getSimpleName()+"! Line: " + (exprOpRelopGE.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopGE.getRight().getColumn(),
                    new TypeMismatchException(("GE operation cannot be applied!")));
        }
        exprOpRelopGE.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpRelopLT exprOpRelopLT, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpRelopLT.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpRelopLT.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpRelopLT.getClass().getSimpleName()+"! Line: " + (exprOpRelopLT.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopLT.getRight().getColumn(),
                    new ProcedureDeclarationException("LT operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,"LT");
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpRelopLT.getClass().getSimpleName()+"! Line: " + (exprOpRelopLT.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopLT.getRight().getColumn(),
                    new TypeMismatchException(("LT operation cannot be applied!")));
        }
        exprOpRelopLT.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpRelopLE exprOpRelopLE, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpRelopLE.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpRelopLE.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpRelopLE.getClass().getSimpleName()+"! Line: " + (exprOpRelopLE.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopLE.getRight().getColumn(),
                    new ProcedureDeclarationException("LE operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,"LE");
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpRelopLE.getClass().getSimpleName()+"! Line: " + (exprOpRelopLE.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopLE.getRight().getColumn(),
                    new TypeMismatchException(("LE operation cannot be applied!")));
        }
        exprOpRelopLE.setNodeType(resultType);
        return resultType;
    }



    @Override
    public NodeType visit(ExprOpRelopEQ exprOpRelopEQ, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpRelopEQ.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpRelopEQ.getRightOperand().accept(this,arg);


        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpRelopEQ.getClass().getSimpleName()+"! Line: " + (exprOpRelopEQ.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopEQ.getRight().getColumn(),
                    new ProcedureDeclarationException("EQ operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,"EQ");
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpRelopEQ.getClass().getSimpleName()+"! Line: " + (exprOpRelopEQ.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopEQ.getRight().getColumn(),
                    new TypeMismatchException(("EQ operation cannot be applied!")));
        }
        exprOpRelopEQ.setNodeType(resultType);
        return resultType;
    }

    @Override
    public NodeType visit(ExprOpRelopNE exprOpRelopNE, SymbolTable arg) throws CompilerException {

        NodeType nodeLeft = exprOpRelopNE.getLeftOperand().accept(this,arg);
        NodeType nodeRight = exprOpRelopNE.getRightOperand().accept(this,arg);

        TypesVar nodeTypeL = typeCheckOperation(nodeLeft);
        TypesVar nodeTypeR = typeCheckOperation(nodeRight);

        if(nodeTypeL == null || nodeTypeR == null)
            throw new CompilerException("Type Checking Error "+exprOpRelopNE.getClass().getSimpleName()+"! Line: " + (exprOpRelopNE.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopNE.getRight().getColumn(),
                    new ProcedureDeclarationException("NE operation cannot be applied!"));

        TypesVar resultType = nodeTypeL.verifyRelation(nodeTypeR,"NE");
        if(resultType.equals(TypesVar.NULL)){
            throw new CompilerException("Type Checking Error "+exprOpRelopNE.getClass().getSimpleName()+"! Line: " + (exprOpRelopNE.getLeft().getLine() - 1) + " " +
                    "- Col:" + exprOpRelopNE.getRight().getColumn(),
                    new TypeMismatchException(("NE operation cannot be applied!")));
        }
        exprOpRelopNE.setNodeType(resultType);
        return resultType;
    }

    private int typeCheckingVoid(ArrayList<TypesVar> list){
        int numVoid=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(TypesVar.VOID))
                numVoid++;
        }
        return numVoid;

    }
}

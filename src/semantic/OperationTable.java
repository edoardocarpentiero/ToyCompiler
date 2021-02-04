package semantic;

import syntax.type.TypesVar;

public interface OperationTable {

    TypesVar verifyAddition(TypesVar type);
    TypesVar verifySubtraction(TypesVar type);
    TypesVar verifyMultiplication(TypesVar type);
    TypesVar verifyAssign(TypesVar type);
    TypesVar verifyDivision(TypesVar type);
    TypesVar verifyRelation(TypesVar type,String typeOp);
    TypesVar verifyOneOperator();
}

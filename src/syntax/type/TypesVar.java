package syntax.type;


import semantic.OperationTable;

public enum TypesVar implements OperationTable, NodeType {

        BOOL{
                @Override
                public TypesVar verifyAddition(TypesVar type) {
                       return NULL;
                }

                @Override
                public TypesVar verifySubtraction(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyMultiplication(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyAssign(TypesVar type) {
                        switch (type){
                                case BOOL:return BOOL;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyDivision(TypesVar type) { return NULL; }

                @Override
                public TypesVar verifyRelation(TypesVar type, String typeOp) {
                        switch (type){
                                case BOOL:return BOOL;
                                default: return NULL;
                        }

                }
                @Override
                public TypesVar verifyOneOperator() { return BOOL; }
        },
        STRING{
                @Override
                public TypesVar verifyAddition(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifySubtraction(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyMultiplication(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyAssign(TypesVar type) {
                        switch (type){
                                case STRING:return STRING;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyDivision(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyRelation(TypesVar type, String typeOp) {
                        switch (typeOp){
                                case "LT": case "GT": case "EQ":
                                        switch (type){
                                                case STRING:return BOOL;
                                                default: return NULL;
                                        }
                                default: return NULL;
                        }

                }

                @Override
                public TypesVar verifyOneOperator() {
                        return NULL;
                }
        },
        INT{
                @Override
                public TypesVar verifyAddition(TypesVar type) {
                        switch (type){
                                case INT: return INT;
                                case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifySubtraction(TypesVar type) {
                        switch (type){
                                case INT: return INT;
                                case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyMultiplication(TypesVar type) {
                        switch (type){
                                case INT: return INT;
                                case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyAssign(TypesVar type) {

                        switch (type){
                                case INT: return INT;
                                case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyDivision(TypesVar type) {
                        switch (type){
                                case INT: return INT;
                                case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyRelation(TypesVar type,String typeOp) {
                        switch (type){
                                case INT: case FLOAT: return BOOL;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyOneOperator() {
                        return INT;
                }
        },
        FLOAT{
                @Override
                public TypesVar verifyAddition(TypesVar type) {
                        switch (type){
                                case INT: case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifySubtraction(TypesVar type) {
                        switch (type){
                                case INT: case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyMultiplication(TypesVar type) {
                        switch (type){
                                case INT: case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyAssign(TypesVar type) {
                        switch (type){
                                case INT: case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyDivision(TypesVar type) {
                        switch (type){
                                case INT: case FLOAT: return FLOAT;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyRelation(TypesVar type,String typeOp) {
                        switch (type){
                                case INT: case FLOAT: return BOOL;
                                default: return NULL;
                        }
                }

                @Override
                public TypesVar verifyOneOperator() {
                        return FLOAT;
                }
        },
        VOID{
                @Override
                public TypesVar verifyAddition(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifySubtraction(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyMultiplication(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyAssign(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyDivision(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyRelation(TypesVar type,String typeOp) {
                        return NULL;
                }

                @Override
                public TypesVar verifyOneOperator() {
                        return NULL;
                }
        },
        NULL {
                @Override
                public TypesVar verifyAddition(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifySubtraction(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyMultiplication(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyAssign(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyDivision(TypesVar type) {
                        return NULL;
                }

                @Override
                public TypesVar verifyRelation(TypesVar type,String typeOp) {
                        return NULL;
                }

                @Override
                public TypesVar verifyOneOperator() {
                        return NULL;
                }
        };
}

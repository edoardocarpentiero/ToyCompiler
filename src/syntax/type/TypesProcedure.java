package syntax.type;

import java.util.ArrayList;

/**
 * Questa classe rappresenta il concetto di tipo all'interno di un record della Symbol Table
 */
public class TypesProcedure implements NodeType{
    private ArrayList<TypesVar> paramTypes;
    private ArrayList<TypesVar> resultTypes;
    private TypesVar typesVar;

    /**
     * Nel caso in cui, durante la visita dell'AST, vi è una dichiarazione di una procedura, viene utilizzato questo costruttore in cui
     * @param paramTypes
     * @param resultTypes
     */
    public TypesProcedure(ArrayList<TypesVar> paramTypes, ArrayList<TypesVar> resultTypes) {
        this.paramTypes = paramTypes;
        this.resultTypes = resultTypes;
    }


    public ArrayList<TypesVar> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(ArrayList<TypesVar> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public ArrayList<TypesVar> getResultTypes() {
        return resultTypes;
    }

    public void setResultTypes(ArrayList<TypesVar> resultTypes) {
        this.resultTypes = resultTypes;
    }
}

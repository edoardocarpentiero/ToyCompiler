package visitor;

import exception.CompilerException;

public interface Element {
    <T, P> T accept(Visitor<T, P> visitor, P arg) throws CompilerException;
}

package org.compiler.Exceptions;

public class LeftRecursionException extends SyntaxAnalyzerException {
    public LeftRecursionException(String message) {
        super(message);
    }
}

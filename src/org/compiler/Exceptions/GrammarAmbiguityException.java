package org.compiler.Exceptions;

/**
 * Created by augusto on 10/29/15.
 */
public class GrammarAmbiguityException extends SyntaxAnalyzerException {
    public GrammarAmbiguityException(String message){
        super(message);
    }
}

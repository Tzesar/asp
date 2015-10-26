package org.compiler;

public interface BodyArtifact {
    boolean isTerminal();
    boolean isNonTerminal();
    String toString();
}

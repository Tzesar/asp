package org.compiler;

/**
 * Created by augusto on 10/23/15.
 */
public interface BodyArtifact {
    public boolean isTerminal();
    public boolean isNonTerminal();
    public String toString();
}

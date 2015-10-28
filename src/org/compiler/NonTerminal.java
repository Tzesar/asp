package org.compiler;

public class NonTerminal extends BodyArtifact {
    public NonTerminal(Character bodyArtifactString) {
        name = bodyArtifactString;
    }

    public boolean isTerminal(){
        return false;
    }

    public boolean isNonTerminal(){
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NonTerminal that = (NonTerminal) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString(){
        return name.toString();
    }
}

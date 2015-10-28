package org.compiler;

public class Terminal extends BodyArtifact {
    private boolean emptyTerminal;
    private boolean endMarker;

    public Terminal(Character bodyArtifactString) {
        name = bodyArtifactString;
        emptyTerminal = false;
        endMarker = false;
    }

    public boolean isEmptyTerminal(){
        return emptyTerminal;
    }

    public void setAsEmptyTerminal() {
        emptyTerminal = true;
    }

    public boolean isTerminal(){
        return true;
    }

    public boolean isNonTerminal(){
        return false;
    }

    public boolean isEndMarker() {
        return endMarker;
    }

    public void setEndMarker(boolean endMarker) {
        this.endMarker = endMarker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Terminal terminal = (Terminal) o;

        return name.equals(terminal.name);

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

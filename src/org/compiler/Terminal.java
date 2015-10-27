package org.compiler;

public class Terminal extends BodyArtifact {
    private boolean emptyTerminal;

    public Terminal(Character bodyArtifactString) {
        name = bodyArtifactString;
        emptyTerminal = false;
    }

    public boolean isEmptyTerminal(){
        return emptyTerminal;
    }

    public void setAsEmptyTerminal() {
        emptyTerminal = true;
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

package org.compiler;

public class Terminal implements BodyArtifact {
    private Character name;

    public Terminal(Character bodyArtifactString) {
        name = bodyArtifactString;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
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
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isNonTerminal() {
        return false;
    }

    @Override
    public String toString(){
        return name.toString();
    }
}

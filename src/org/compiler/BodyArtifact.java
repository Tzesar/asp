package org.compiler;

public abstract class BodyArtifact {
    protected Character name;

    public Character getName(){
        return name;
    }

    public void setName(Character name){
        this.name = name;
    }

    public abstract boolean isNonTerminal();

    public abstract boolean isTerminal();

    public abstract String toString();
}

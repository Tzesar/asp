package org.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetFirst {
    private List<Terminal> terminals;
    private boolean containsEmpty;

    public SetFirst() {
        terminals = new ArrayList<>();
        containsEmpty = false;
    }

    public void add(Terminal terminal) {
        terminals.add(terminal);
    }

    public List<Terminal> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<Terminal> terminals) {
        this.terminals = terminals;
    }

    public boolean containsEmpty() {
        return containsEmpty;
    }

    public void setContainsEmpty(boolean containsEmpty) {
        this.containsEmpty = containsEmpty;
    }

    public void addAll(SetFirst subSetFirst) {
        terminals.addAll(subSetFirst.getTerminals().stream().filter( terminal -> !terminals.contains(terminal) ).collect(Collectors.toList()));
        containsEmpty = subSetFirst.containsEmpty();
    }

    public void addAllWithoutEmptyTerminal(SetFirst subSetFirst) {
        this.terminals.addAll(subSetFirst.terminals.stream().filter(terminal -> !terminal.isEmptyTerminal()).collect(Collectors.toList()));
    }

    public void removeEmptyArtifact(){
        if ( containsEmpty ) {
            terminals.remove(new Terminal('@'));
        }
    }

    @Override
    public String toString(){
        return String.join(", ", terminals.stream().map(Terminal::toString).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetFirst setFirst = (SetFirst) o;

        return containsEmpty == setFirst.containsEmpty && terminals.equals(setFirst.terminals);

    }

    @Override
    public int hashCode() {
        int result = terminals.hashCode();
        result = 31 * result + (containsEmpty ? 1 : 0);
        return result;
    }
}

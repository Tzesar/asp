package org.compiler;

import org.compiler.Util.Constants;

import javax.sound.midi.Track;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingTable {
    private Map<NonTerminal, Map<Terminal, Body>> table;

    public ParsingTable(List<NonTerminal> nonTerminalList, List<Terminal> terminalList){
        table = new HashMap<>();
        Terminal endMarker = new Terminal(Constants.END_MARK_STRING);

        nonTerminalList.forEach( nonTerminal -> {
            Map<Terminal, Body> tableRow = new HashMap<>();

            terminalList.forEach( terminal -> tableRow.put(terminal, null));
            if ( !terminalList.contains(endMarker) ){
                tableRow.put(endMarker, null);
            }

            table.put(nonTerminal, tableRow);
        });
    }

    public boolean putBodyProduction(NonTerminal nonTerminal, Terminal terminal, Body body){
        Map<Terminal, Body> tableRow = table.get(nonTerminal);

        if ( tableRow.get(terminal) == null ){
            tableRow.put(terminal, body);
            return true;
        } else {
            return false;
        }
    }

    public Body getBodyProduction(NonTerminal nonTerminal, Terminal terminal){
        if ( table.containsKey(nonTerminal) ){
            Map<Terminal, Body> tableRow = table.get(nonTerminal);
            if ( tableRow.containsKey(terminal) ){
                return tableRow.get(terminal);
            }
        }

        return null;
    }

    public Body getBodyProduction(NonTerminal nonTerminal, Character terminalCharacter){
        return getBodyProduction(nonTerminal, new Terminal(terminalCharacter));
    }
}

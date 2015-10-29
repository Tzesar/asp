package org.compiler;

import javax.sound.midi.Track;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by augusto on 10/28/15.
 */
public class ParsingTable {
    private Map<NonTerminal, Map<Terminal, Body>> table;

    public ParsingTable(List<NonTerminal> nonTerminalList, List<Terminal> terminalList){
        table = new HashMap<>();

        nonTerminalList.forEach( nonTerminal -> {
            Map<Terminal, Body> tableRow = new HashMap<>();

            terminalList.forEach( terminal -> tableRow.put(terminal, null));

            table.put(nonTerminal, tableRow);
        });
    }

    public boolean setBodyProduction(NonTerminal nonTerminal, Terminal terminal, Body body){
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
}

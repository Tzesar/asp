package org.compiler;

import java.util.*;

public class SyntaxAnalyzer {
    private Map<NonTerminal, Production> productions;
    private Map<BodyArtifact, SetFirst> setsFirst;

    public SyntaxAnalyzer(List<Production> productionList) {
        productions = new HashMap<>();
        productionList.stream().forEach( production -> productions.put(production.getHead(), production));

        setsFirst = new HashMap<>();
        productionList.stream().forEach( production -> setsFirst.put(production.getHead(), constructSetFirst(production.getHead())));
    }

    public SetFirst getSetFirst(NonTerminal nonTerminal){
        if ( setsFirst.containsKey(nonTerminal) ){
            return setsFirst.get(nonTerminal);
        } else {
            SetFirst setFirst = constructSetFirst(nonTerminal);
            setsFirst.put(nonTerminal, setFirst);
            return setFirst;
        }
    }

    public SetFirst constructSetFirst(NonTerminal nonTerminal) {
        Production production = productions.get(nonTerminal);
        SetFirst setFirst = new SetFirst();

        for ( Body body : production.getBodies() ) {
            for ( BodyArtifact artifact : body.getArtifacts() ) {
                if (artifact instanceof Terminal) {
                    Terminal terminal = (Terminal) artifact;
                    if ( terminal.isEmptyTerminal() ){
                        setFirst.setContainsEmpty(true);
                    }
                    setFirst.add(terminal);
                    break;
                } else {
                    Production prod = productions.get((NonTerminal) artifact);

                    SetFirst subSetFirst = getSetFirst(prod.getHead());
                    subSetFirst.removeEmptyArtifact();
                    setFirst.addAll(subSetFirst);
                    if (!subSetFirst.containsEmpty()) {
                        break;
                    }
                }
            }
        }

        return setFirst;
    }


    public String printSetFirst(NonTerminal nonTerminal) {
        if ( setsFirst.containsKey(nonTerminal) ) {
            return "FIRST(" + nonTerminal.toString() + ") = {" + setsFirst.get(nonTerminal).toString() + "}";
        } else {
            return "This is not a processed production.";
        }
    }
}

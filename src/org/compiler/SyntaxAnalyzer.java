package org.compiler;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SyntaxAnalyzer {
    private Map<NonTerminal, Production> productions;
    private Map<BodyArtifact, SetFirst> setsFirst;

    public SyntaxAnalyzer(List<Production> productionList) {
        productions = new HashMap<>();
        productionList.stream().forEach( production -> productions.put(production.getHead(), production));

        setsFirst = new HashMap<>();
        productionList.stream()
                .collect(Collectors.toCollection(ArrayDeque::new))
                .descendingIterator()
                .forEachRemaining( production -> setsFirst.put(production.getHead(), constructSetFirst(production.getHead())) );
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
            SetFirst bodySetFirst = new SetFirst();
            for ( BodyArtifact artifact : body.getArtifacts() ) {
                if (artifact instanceof Terminal) {
                    Terminal terminal = (Terminal) artifact;
                    if ( terminal.isEmptyTerminal() ){
                        bodySetFirst.setContainsEmpty(true);
                    }
                    bodySetFirst.add(terminal);
                    break;
                } else {
                    Production prod = productions.get((NonTerminal) artifact);

                    SetFirst subSetFirst = getSetFirst(prod.getHead());
                    if ( body.getArtifacts().indexOf(artifact) == (body.getArtifacts().size() - 1) ){
                        bodySetFirst.addAll(subSetFirst);
                    } else {
                        bodySetFirst.addAllWithoutEmptyTerminal(subSetFirst);
                    }
                    if (!subSetFirst.containsEmpty()) {
                        break;
                    }
                }
            }

            body.setSetFirst(bodySetFirst);
            setFirst.addAll(bodySetFirst);
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

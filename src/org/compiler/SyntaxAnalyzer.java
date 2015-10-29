package org.compiler;

import org.compiler.Exceptions.GrammarAmbiguityException;
import org.compiler.Exceptions.LeftRecursionException;
import org.compiler.Exceptions.SyntaxAnalyzerException;
import org.compiler.Util.Constants;

import java.util.*;
import java.util.stream.Collectors;

import static org.compiler.Util.Wrappers.rethrow;

public class SyntaxAnalyzer {
    private Map<NonTerminal, Production> productions;
    private List<Production> productionList;
    private Map<BodyArtifact, SetFirst> setsFirst;
    private Map<BodyArtifact, List<Terminal>> setsFollow;
    private List<NonTerminal> nonTerminals;
    private List<Terminal> terminals;
    private ParsingTable parsingTable;

    public SyntaxAnalyzer(List<Production> productionList) throws SyntaxAnalyzerException{
        productions = new HashMap<>();
        nonTerminals = new ArrayListUnique<>();
        terminals = new ArrayListUnique<>();
        setsFirst = new HashMap<>();
        setsFollow = new HashMap<>();

        this.productionList = productionList;
        productionList.stream().forEach( production -> productions.put(production.getHead(), production));

        nonTerminals.addAll(retrieveAllNonTerminals(productionList));
        terminals.addAll(retrieveAllTerminals(productionList));
        parsingTable = new ParsingTable(nonTerminals, terminals);

        try {
            constructSetFirst();
        } catch (StackOverflowError e){
            throw new LeftRecursionException("A direct Left Recursion is present in the grammar provided.");
        }

        try {
            constructSetFollow();
        } catch (StackOverflowError e){
            throw new LeftRecursionException("An indirect Left Recursion is present in the grammar provided.");
        }

        try {
            constructParsingTable();
        } catch (RuntimeException e){
            throw new GrammarAmbiguityException(e.getMessage());
        }
    }

    private void constructParsingTable() throws GrammarAmbiguityException{
        productionList.stream().forEachOrdered(production -> production.getBodies().stream().forEachOrdered(body -> {
            SetFirst setFirst = body.getSetFirst();
                setFirst.getTerminals().stream()
                        .forEachOrdered(terminal -> {
                            if ( !terminal.isEmptyTerminal() ) {
                                parsingTable.putBodyProduction(production.getHead(), terminal, body);
                            }
                            if (setFirst.containsEmpty()) {
                                List<Terminal> setFollow = setsFollow.get(production.getHead());
                                setFollow.stream().forEachOrdered(rethrow(terminal1 -> {
                                            boolean duplicatedEntry = parsingTable.putBodyProduction(production.getHead(), terminal1, body);

                                            if (!duplicatedEntry) {
                                                Body firstBody = parsingTable.getBodyProduction(production.getHead(), terminal1);
                                                throw new GrammarAmbiguityException("The Non-Terminal " + production.getHead().toString()
                                                        + " has more than one productions for the Terminal " + terminal.toString()
                                                        + ". Productions { [" + firstBody.toString() + "], [" + body.toString() + "] }."
                                                );
                                            }
                                        })
                                );
                            }
                        });
        }));
    }

    private List<NonTerminal> retrieveAllNonTerminals(List<Production> productionList) {
        List<NonTerminal> nonTerminalList = new ArrayListUnique<>();

        productionList.stream().forEach( production -> {
            nonTerminalList.add(production.getHead());
            production.getBodies().stream()
                    .filter(body -> !body.isEmpty() && !body.containsEmpty())
                    .forEach(body1 -> body1.getArtifacts().stream()
                            .filter(BodyArtifact::isNonTerminal)
                            .forEach(bodyArtifact -> nonTerminalList.add((NonTerminal) bodyArtifact)));
        });
        return nonTerminalList;
    }

    private List<Terminal> retrieveAllTerminals(List<Production> productionList) {
        List<Terminal> terminalList = new ArrayListUnique<>();

        productionList.stream().forEach( production -> {
            production.getBodies().stream()
                    .filter(body -> !body.isEmpty() && !body.containsEmpty())
                    .forEach(body1 -> body1.getArtifacts().stream()
                            .filter(BodyArtifact::isTerminal)
                            .forEach(bodyArtifact -> terminalList.add((Terminal) bodyArtifact)));
        });
        return terminalList;
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

    public void constructSetFirst(){
        productionList.stream()
                .collect(Collectors.toCollection(ArrayDeque::new))
                .descendingIterator()
                .forEachRemaining( production -> setsFirst.put(production.getHead(), constructSetFirst(production.getHead())) );
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

    public List<Terminal> getSetFollow(NonTerminal nonTerminal){
        if ( setsFollow.containsKey(nonTerminal) ){
            return setsFollow.get(nonTerminal);
        } else {
            List<Terminal> setFollow = constructSetFollow(nonTerminal);
            setsFollow.put(nonTerminal, setFollow);
            return setFollow;
        }
    }

    public void constructSetFollow(){
        nonTerminals.forEach(nonTerminal -> {
            List<Terminal> setFollow = new ArrayListUnique<>();
            setFollow.add(new Terminal(Constants.END_MARK_STRING));
            setFollow.addAll(constructSetFollow(nonTerminal));

            setsFollow.put(nonTerminal, setFollow);
        });
    }

    private List<Terminal> constructSetFollow(NonTerminal nonTerminal) {
        List<Terminal> setFollow = new ArrayListUnique<>();

        for ( Production production : productionList ){
            production.getBodies().stream()
                    .filter(body -> body.getArtifacts().contains(nonTerminal))
                    .forEach(body -> {
                        int index = body.getArtifacts().indexOf(nonTerminal);
                        int lastArtifactIndex = body.getArtifacts().size() - 1;

                        if (index < lastArtifactIndex) {
                            BodyArtifact nextArtifact = body.getArtifacts().get(index + 1);
                            if (nextArtifact instanceof Terminal) {
                                setFollow.add((Terminal) nextArtifact);
                            } else {
                                SetFirst setFirstNextArtifact = setsFirst.get((NonTerminal) nextArtifact);

                                setFollow.addAll(setFirstNextArtifact.getTerminals().stream()
                                                .filter(terminal -> !terminal.isEmptyTerminal())
                                                .collect(Collectors.toList())
                                );
                                if (setFirstNextArtifact.containsEmpty() && !production.getHead().equals(nonTerminal)) {
                                    setFollow.addAll(getSetFollow(production.getHead()));
                                }
                            }
                        } else {
                            if ( !production.getHead().equals(nonTerminal) ) {
                                setFollow.addAll(getSetFollow(production.getHead()));
                            }
                        }
                    });
        }

        return setFollow;
    }

    public List<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(List<NonTerminal> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public List<Terminal> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<Terminal> terminals) {
        this.terminals = terminals;
    }

    public String printSetFirst(NonTerminal nonTerminal) {
        if ( setsFirst.containsKey(nonTerminal) ) {
            return "FIRST(" + nonTerminal.toString() + ") = {" + setsFirst.get(nonTerminal).toString() + "}";
        } else {
            return "This is not a processed production.";
        }
    }

    public String printSetFollow(NonTerminal nonTerminal) {
        if ( setsFollow.containsKey(nonTerminal) ){
            return "FOLLOW(" + nonTerminal.toString() + ") = {"
                    + String.join(", ", setsFollow.get(nonTerminal).stream().map(Terminal::toString).collect(Collectors.toList())) + "}";
        } else {
            return "This is not a processed Non-Terminal.";
        }
    }
}

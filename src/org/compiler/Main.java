package org.compiler;

import org.compiler.Exceptions.MalformedProductionException;
import org.compiler.Util.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
//        String input = "E\t-> T H\n;" +
//                "H\t-> a T H | @\n;" +
//                "T\t-> F G\n;" +
//                "G\t-> b F G | @\n;" +
//                "F\t-> d E d | i;";

//        String input = "T->a|@;E->Tc|b;";

//        String input = "E\t-> J K;\n" +
//                "K\t-> a T K | @;\n" +
//                "T\t-> F J;\n" +
//                "J\t-> b F J | @;\n" +
//                "F\t-> m E n | i;";

        String input = "E\t-> T K;\n" +
                "K\t-> + T K | @;\n" +
                "T\t-> F J;\n" +
                "J\t-> * F J | @;\n" +
                "F\t-> ( E ) | i;";

        List<Production> productionList = createProductions(input);
        productionList.forEach( production -> System.out.println(production.toString()) );
        System.out.println(Constants.DIVIDING_BAR);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(productionList);
        productionList.forEach( production -> System.out.println(syntaxAnalyzer.printSetFirst(production.getHead())));
        System.out.println(Constants.DIVIDING_BAR);
        syntaxAnalyzer.getNonTerminals().forEach(nonTerminal -> System.out.println(syntaxAnalyzer.printSetFollow(nonTerminal)));
    }

    private static List<Production> createProductions(String input) {
        return Arrays.asList(input.split(";")).stream().map( line -> {
            line = Production.cleanProduction(line);

            try {
                return new Production(line);
            } catch (MalformedProductionException malformedException){
                System.out.println("ERROR: " + malformedException.getMessage());
                return null;
            }
        }).collect(Collectors.toList());
    }
}
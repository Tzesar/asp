package org.compiler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String input = "E\t-> T H\n;" +
                "H\t-> a T H | @\n;" +
                "T\t-> F G\n;" +
                "G\t-> b F G | @\n;" +
                "F\t-> d E d | i;";

//        String input = "T->a|@;E->Tc|b;";

        List<Production> productionList = createProductions(input);
        productionList.forEach( production1 -> System.out.println(production1.toString()) );
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(productionList);
        productionList.forEach( production -> System.out.println(syntaxAnalyzer.printSetFirst(production.getHead())));
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
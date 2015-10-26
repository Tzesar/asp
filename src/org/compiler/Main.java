package org.compiler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String input = "S ->ABc|cd; P -> Ssa";

        List<Production> productionList = createProductions(input);
    }

    private static List<Production> createProductions(String input) {
        return Arrays.asList(input.split(";")).stream().map( line -> {
            line = Production.cleanProduction(line);

            try {
                Production production = new Production(line);
                System.out.println(production.toString());
                return production;
            } catch (MalformedProductionException malformedException){
                System.out.println(malformedException.getMessage());
                return null;
            }
        }).collect(Collectors.toList());
    }
}

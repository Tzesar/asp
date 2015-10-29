package org.compiler;

import org.compiler.Config.Configurations;
import org.compiler.Exceptions.MalformedProductionException;
import org.compiler.Exceptions.SyntaxAnalyzerException;
import org.compiler.Util.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
//        String grammar = "E\t-> T H\n;" +
//                "H\t-> a T H | @\n;" +
//                "T\t-> F G\n;" +
//                "G\t-> b F G | @\n;" +
//                "F\t-> d E d | i;";

//        String grammar = "T->a|@;E->Tc|b;";

//        String grammar = "S->iEtSK | a; K -> eS | @; E ->b;";

//        String grammar = "S->Sa;R->d | e;";

//        String grammar = "E\t-> T K;\n" +
//                "K\t-> + T K | @;\n" +
//                "T\t-> F J;\n" +
//                "J\t-> * F J | @;\n" +
//                "F\t-> ( E ) | i;";
//
//        String input = "i+i";

        String grammar = getFileContent(Configurations.grammarPath);
        String input = getFileContent(Configurations.inputPath);

        try {
            List<Production> productionList = createProductions(grammar);
            productionList.forEach(production -> System.out.println(production.toString()));
            System.out.println(Constants.DIVIDING_BAR);
            SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(productionList);
            productionList.forEach(production -> System.out.println(syntaxAnalyzer.printSetFirst(production.getHead())));
            System.out.println(Constants.DIVIDING_BAR);
            syntaxAnalyzer.getNonTerminals().forEach(nonTerminal -> System.out.println(syntaxAnalyzer.printSetFollow(nonTerminal)));
            System.out.println(Constants.DIVIDING_BAR);
            Deque<String> derivationStack = syntaxAnalyzer.deriveInput(input);
            derivationStack.stream()
                    .collect(Collectors.toCollection(ArrayDeque::new))
                    .descendingIterator()
                    .forEachRemaining(System.out::println);
        } catch (SyntaxAnalyzerException saEx){
            System.out.println(saEx.getMessage());
        }
    }

    private static String getFileContent(String path) {
        List<String> content = new ArrayList<>();

        Path grammarFilePath = Paths.get(path);
        try {
            Scanner scanner = new Scanner(grammarFilePath, StandardCharsets.UTF_8.name());

            scanner.forEachRemaining(content::add);
        } catch (IOException e){
            System.out.println("Working directory: "+ System.getProperty("user.dir") );
        }

        return String.join("", content);
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
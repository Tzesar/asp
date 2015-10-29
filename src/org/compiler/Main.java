package org.compiler;

import org.compiler.Config.Configurations;
import org.compiler.Exceptions.MalformedProductionException;
import org.compiler.Exceptions.SyntaxAnalyzerException;
import org.compiler.Util.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
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

        Path filePath = Paths.get(path);
        try {
            Files.lines(filePath, Charset.defaultCharset()).forEach( line -> {
                if (!line.startsWith("//")) {
                    content.add(line);
                }
            });
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
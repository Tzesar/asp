package org.compiler;

public class Main {

    public static void main(String[] args) {
        String testInputLine = "S ->ABc|cd";

        try {
            Production production = new Production(testInputLine);
            System.out.println(production.toString());
        } catch (MalformedProductionException malformedException){
            System.out.println(malformedException.getMessage());
        }
    }
}

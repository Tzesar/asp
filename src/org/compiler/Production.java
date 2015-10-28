package org.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Production {

    private NonTerminal head;
    private List<Body> bodies;
    private boolean derivesToEmpty;

    public Production(String inputLine) throws MalformedProductionException{
        bodies = new ArrayList<>();
        derivesToEmpty = false;
        inputLine = Production.cleanProduction(inputLine);

        if (checkLineComposition(inputLine)) {
            int arrowIndex = inputLine.indexOf("->");
            Character headString = inputLine.charAt(0);
            String bodiesString = inputLine.substring(arrowIndex + 2);

            head = new NonTerminal(headString);
            Arrays.asList(bodiesString.split("\\|")).stream().forEach(bodyString -> {
                Body body = new Body(bodyString);
                if ( body.containsEmpty() ){
                    derivesToEmpty = true;
                }

                bodies.add(body);
            });
        }
    }

    private static boolean checkLineComposition(String inputLine) throws MalformedProductionException{

        if ( !inputLine.contains("->") ){
            throw new MalformedProductionException("The production must have a head and a body arranged like this" +
                    " 'A -> bCd', found " + inputLine);
        } else if ( !inputLine.matches("[A-Za-z]->[A-Za-z_\\+\\*\\-/\\(\\)]+?(\\|[A-Za-z_\\+\\*\\-/\\(\\)]+?)*?(\\|@)??") ){
            throw new MalformedProductionException("The production must have a head and a body arranged like this" +
                    " 'A -> bCd', found " + inputLine);
        }

        return true;
    }

    public NonTerminal getHead() {
        return head;
    }

    public void setHead(NonTerminal head) {
        this.head = head;
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void setBodies(List<Body> bodies) {
        this.bodies = bodies;
    }

    public boolean derivesToEmpty() {
        return derivesToEmpty;
    }

    public void setDerivesToEmpty(boolean derivesToEmpty) {
        this.derivesToEmpty = derivesToEmpty;
    }

    public static String cleanProduction(String inputLine){
        inputLine = inputLine.trim();
        inputLine = inputLine.replace(" ", "");
        inputLine = inputLine.replace("\t", "");

        return inputLine;
    }

    @Override
    public String toString(){
        return head.toString() + " -> " + String.join(" | ", bodies.stream().map(Body::toString).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Production that = (Production) o;

        return head.equals(that.head) && bodies.equals(that.bodies);
    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + bodies.hashCode();
        return result;
    }
}

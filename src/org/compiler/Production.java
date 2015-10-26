package org.compiler;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

/**
 * Created by augusto on 10/23/15.
 */
public class Production {

    private Terminal head;
    private Deque<Deque<BodyArtifact>> bodies;

    public Production(String inputLine) throws MalformedProductionException{
        bodies = new ArrayDeque<>();
        inputLine = Production.cleanProduction(inputLine);

        if (checkLineComposition(inputLine)) {
            int arrowIndex = inputLine.indexOf("->");
            Character headString = inputLine.charAt(0);
            String bodyString = inputLine.substring(arrowIndex + 2);

            head = new Terminal(headString);
            createBodies(bodyString);
        }
    }

    private void createBodies(String bodyString) {
        Deque<BodyArtifact> body = new ArrayDeque<>();

        for( int i = 0; i < bodyString.length(); i++ ){
            Character bodyArtifactChar = bodyString.charAt(i);

            if ( isUpperCase(bodyArtifactChar) ){
                body.add(new NonTerminal(bodyArtifactChar));
            } else if ( isLowerCase(bodyArtifactChar) ){
                body.add(new Terminal(bodyArtifactChar));
            } else {
                bodies.add(body);
                body = new ArrayDeque<>();
            }
        }

        bodies.add(body);
    }

    private static boolean checkLineComposition(String inputLine) throws MalformedProductionException{

        if ( !inputLine.contains("->") ){
            throw new MalformedProductionException("The production must have a head and a body arranged like this 'A -> bCd'.");
        } else if ( !inputLine.matches("[A-Za-z]->[A-Za-z]+?(\\|[A-Za-z]+?)*?(\\|@)??") ){
            throw new MalformedProductionException("The production must have a head and a body arranged like this 'A -> bCd'.");
        }

        return true;
    }

    public Terminal getHead() {
        return head;
    }

    public void setHead(Terminal head) {
        this.head = head;
    }

    public Deque<Deque<BodyArtifact>> getBodies() {
        return bodies;
    }

    public void setBodies(Deque<Deque<BodyArtifact>> bodies) {
        this.bodies = bodies;
    }

    public static String cleanProduction(String inputLine){
        inputLine = inputLine.trim();
        inputLine = inputLine.replace(" ", "");

        return inputLine;
    }

    @Override
    public String toString(){
        return head.toString() + " -> " + bodies.stream().map(body -> body
                    .stream()
                    .map(BodyArtifact::toString)
                    .reduce("", (bodyString, artifactString) -> bodyString + artifactString))
                .reduce("", (bodiesString, bodyString) -> {
                    String joinLexeme = "";
                    if ( !"".equals(bodiesString) ){
                        joinLexeme = " | ";
                    }
                    if ( "".equals(bodyString) ){
                        return bodiesString + joinLexeme + " @ ";
                    } else {
                        return bodiesString + joinLexeme + bodyString;
                    }
                });
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

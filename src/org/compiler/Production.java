package org.compiler;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

/**
 * Created by augusto on 10/23/15.
 */
public class Production {

    private Terminal head;
    private List<BodyArtifact> body;

    public Production(String inputLine) throws MalformedProductionException{
        inputLine = Production.cleanProduction(inputLine);

        if (checkLineComposition(inputLine)) {
            int arrowIndex = inputLine.indexOf("->");
            Character headString = inputLine.charAt(0);
            String bodyString = inputLine.substring(arrowIndex + 1);

            head = new Terminal(headString);
            body = createBody(bodyString);
        }
    }

    private List<BodyArtifact> createBody(String bodyString) {
        List<BodyArtifact> bodyArtifacts = new ArrayList<>();

        for( int i = 0; i < bodyString.length(); i++ ){
            Character bodyArtifactString = bodyString.charAt(i);

            if ( isUpperCase(bodyArtifactString) ){
                bodyArtifacts.add(new NonTerminal(bodyArtifactString));
            } else if ( isLowerCase(bodyArtifactString) ){
                bodyArtifacts.add(new Terminal(bodyArtifactString));
            }
        }

        return bodyArtifacts;
    }

    private static boolean checkLineComposition(String inputLine) throws MalformedProductionException{

        if ( !inputLine.contains("->") ){
            throw new MalformedProductionException("The production must have a head and a body arranged like this 'A -> bCd'.");
        } else if ( !inputLine.matches("[A-Za-z]->[A-Za-z]+?(\\|[A-Za-z]+?)*?") ){
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

    public List<BodyArtifact> getBody() {
        return body;
    }

    public void setBody(List<BodyArtifact> body) {
        this.body = body;
    }

    public static String cleanProduction(String inputLine){
        inputLine = inputLine.trim();
        inputLine = inputLine.replace(" ", "");

        return inputLine;
    }

    @Override
    public String toString(){
        return head.toString() + " -> " + body.stream().map(BodyArtifact::toString).reduce("", (bodyString, artifactString) -> bodyString + artifactString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Production that = (Production) o;

        return head.equals(that.head) && body.equals(that.body);

    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }
}

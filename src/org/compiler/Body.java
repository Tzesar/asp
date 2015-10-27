package org.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

public class Body {
    public static final Character EMPTY_STRING = '@';
    private List<BodyArtifact> bodyArtifacts;
    private boolean containsEmpty;

    public Body(String bodyString){
        bodyArtifacts = new ArrayList<>();
        containsEmpty = false;

        for( int i = 0; i < bodyString.length(); i++ ){
            Character bodyArtifactChar = bodyString.charAt(i);

            if ( isUpperCase(bodyArtifactChar) ){
                bodyArtifacts.add(new NonTerminal(bodyArtifactChar));
            } else if ( isLowerCase(bodyArtifactChar) ){
                bodyArtifacts.add(new Terminal(bodyArtifactChar));
            } else if ( EMPTY_STRING == bodyArtifactChar ){
                containsEmpty = true;

                Terminal emptyTerminal = new Terminal(bodyArtifactChar);
                emptyTerminal.setAsEmptyTerminal();
                bodyArtifacts.add(emptyTerminal);
            }
        }
    }

    public boolean containsEmpty() {
        return containsEmpty;
    }

    public BodyArtifact getFirst(){
        return bodyArtifacts.get(0);
    }

    public int size(){
        return bodyArtifacts.size();
    }

    public boolean isEmpty(){
        return ! ( size() > 0 );
    }

    @Override
    public String toString(){
        return String.join(" ", bodyArtifacts.stream().map(BodyArtifact::toString).collect(Collectors.toList()));
    }
}

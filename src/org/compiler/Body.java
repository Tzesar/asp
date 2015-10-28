package org.compiler;

import org.compiler.Util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Character.isUpperCase;

public class Body {
    private List<BodyArtifact> bodyArtifacts;
    private SetFirst setFirst;
    private boolean containsEmpty;

    public Body(String bodyString){
        bodyArtifacts = new ArrayList<>();
        setFirst = new SetFirst();
        containsEmpty = false;

        for( int i = 0; i < bodyString.length(); i++ ){
            Character bodyArtifactChar = bodyString.charAt(i);

            if ( isUpperCase(bodyArtifactChar) ){
                bodyArtifacts.add(new NonTerminal(bodyArtifactChar));
            } else if ( Constants.EMPTY_STRING == bodyArtifactChar ){
                containsEmpty = true;

                Terminal emptyTerminal = new Terminal(bodyArtifactChar);
                emptyTerminal.setAsEmptyTerminal();
                bodyArtifacts.add(emptyTerminal);
            } else {
                bodyArtifacts.add(new Terminal(bodyArtifactChar));
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

    public SetFirst getSetFirst() {
        return setFirst;
    }

    public void setSetFirst(SetFirst setFirst) {
        this.setFirst = setFirst;
    }

    @Override
    public String toString(){
        return String.join(" ", bodyArtifacts.stream().map(BodyArtifact::toString).collect(Collectors.toList()));
    }

    public List<BodyArtifact> getArtifacts() {
        return bodyArtifacts;
    }
}

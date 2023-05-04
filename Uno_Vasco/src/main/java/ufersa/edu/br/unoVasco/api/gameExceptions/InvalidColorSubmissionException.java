package ufersa.edu.br.unoVasco.api.gameExceptions;

import ufersa.edu.br.unoVasco.model.cards.Cards;

public class InvalidColorSubmissionException extends Exception{
    private Cards.Color expected;
    private Cards.Color actual;
    
    public InvalidColorSubmissionException(String message, Cards.Color actual, Cards.Color expected){
        this.actual = actual;
        this.expected = expected;
    }
}

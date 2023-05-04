package ufersa.edu.br.unoVasco.api.gameExceptions;

import ufersa.edu.br.unoVasco.model.cards.Cards;

public class InvalidValueSubmissionException extends Exception{
    private Cards.Value expected;
    private Cards.Value actual;
    
    public InvalidValueSubmissionException(String message, Cards.Value expected, Cards.Value actual){
        this.actual = actual;
        this.expected = expected;
    }
}

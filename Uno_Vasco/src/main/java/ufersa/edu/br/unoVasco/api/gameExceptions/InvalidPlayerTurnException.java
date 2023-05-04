package ufersa.edu.br.unoVasco.api.gameExceptions;

public class InvalidPlayerTurnException extends Exception{
    String playerId;
    
    public InvalidPlayerTurnException(String message, String pid){
        super(message);
        playerId = pid;
    }
    
    public String getPid(){
        return playerId;
    }
}

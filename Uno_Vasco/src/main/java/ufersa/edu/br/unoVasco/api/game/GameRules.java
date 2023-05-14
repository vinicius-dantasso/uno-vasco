package ufersa.edu.br.unoVasco.api.game;

import java.awt.Font;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import ufersa.edu.br.unoVasco.api.gameExceptions.InvalidColorSubmissionException;
import ufersa.edu.br.unoVasco.api.gameExceptions.InvalidPlayerTurnException;
import ufersa.edu.br.unoVasco.api.gameExceptions.InvalidValueSubmissionException;
import ufersa.edu.br.unoVasco.model.cards.Cards;
import ufersa.edu.br.unoVasco.model.cards.CardsDeck;
import ufersa.edu.br.unoVasco.model.dataStructures.MyArrayList;

public class GameRules {
    private int currentPlayer;
    private String[] playerIds;
    
    private CardsDeck deck;
    private MyArrayList<MyArrayList<Cards>> playerHand;
    private MyArrayList<Cards> stockpile;
    
    private Cards.Color validColor;
    private Cards.Value validValue;
    
    boolean gameDirection;
    
    public GameRules(String[] pids){
        deck = new CardsDeck();
        deck.resetDeck();
        deck.shuffle();
        stockpile = new MyArrayList<>();
        
        playerIds = pids;
        currentPlayer = 0;
        gameDirection = false;
        
        playerHand = new MyArrayList<>();
        
        //INSERIR AS CARTAS INICIAIS DE CADA JOGADOR EM SUAS MÃOS
        for(int i=0; i<pids.length; i++){
            MyArrayList<Cards> hand = new MyArrayList<>();
            hand = hand.addList(Arrays.asList(deck.drawManyCards(7)));
            playerHand.add(hand);
        }
    }
    
    public void checkGameDirection(){
        if(!gameDirection){
            currentPlayer = (currentPlayer+1)%playerIds.length;
        }
        else{
            currentPlayer = (currentPlayer-1)%playerIds.length;
            if(currentPlayer == -1){
                currentPlayer = playerIds.length - 1;
            }
        }
    }
    
    public void start(GameRules game){
        Cards card = deck.drawCard();
        validColor = card.getColor();
        validValue = card.getValue();
        
        if(card.getValue() == Cards.Value.Wild){
            start(game);
        }
        
        if(card.getValue() == Cards.Value.WildFour || card.getValue() == Cards.Value.DrawTwo){
            start(game);
        }
        
        if(card.getValue() == Cards.Value.Block){
            JLabel message = new JLabel(playerIds[currentPlayer] + " foi bloqueado!");
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
            
            checkGameDirection();
        }
        
        if(card.getValue() == Cards.Value.Reverse){
            JLabel message = new JLabel(playerIds[currentPlayer] + " a direção do jogo foi mudada");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null, message);
            gameDirection ^= true;
            currentPlayer = playerIds.length - 1;
        }
        
        stockpile.add(card);
    }
    
    public Cards getTopCard(){
        return new Cards(validColor, validValue);
    }
    
    public ImageIcon getTopCardImage(){
        return new ImageIcon(validColor + "_" + validValue + ".png");
    }
    
    public boolean isGameOver(){
        for(String player : this.playerIds){
            if(hasEmptyHand(player)){
                return true;
            }
        }
        return false;
    }
    
    public String getCurrentPlayer(){
        return this.playerIds[this.currentPlayer];
    }
    
    public String getPreviousPlayer(int i){
        int index = this.currentPlayer - i;
        if(index == -1){
            index = playerIds.length - 1;
        }
        return this.playerIds[index];
    }
    
    public String[] getPlayers(){
        return this.playerIds;
    }
    
    public MyArrayList<Cards> getPlayerHand(String pid){
        int index = Arrays.asList(playerIds).indexOf(pid);
        return playerHand.get(index);
    }
    
    public int getPlayerHandSize(String pid){
        return getPlayerHand(pid).size();
    }
    
    public Cards getPlayerCard(String pid, int choice){
        MyArrayList<Cards> hand = getPlayerHand(pid);
        return hand.get(choice);
    }
    
    public boolean hasEmptyHand(String pid){
        return getPlayerHand(pid).isEmpty();
    }
    
    public boolean validCardPlay(Cards card){
        return card.getColor() == validColor || card.getValue() == validValue;
    }
    
    public void checkPlayerTurn(String pid) throws InvalidPlayerTurnException{
        if(!playerIds[this.currentPlayer].equals(pid)){
            throw new InvalidPlayerTurnException("Este não é o seu turno " + pid, pid);
        }
    }
    
    public void submitDraw(String pid) throws InvalidPlayerTurnException{
        checkPlayerTurn(pid);
        if(deck.isEmpty()){
            deck.resetDeck();
            deck.shuffle();
        }
        
        getPlayerHand(pid).add(deck.drawCard());
        checkGameDirection();
    }
    
    public void setCardColor(Cards.Color color){
        validColor = color;
    }
    
    public void submitPlayerCard(String pid, Cards card, Cards.Color declaredColor) 
    throws InvalidPlayerTurnException, InvalidValueSubmissionException, InvalidColorSubmissionException
    {
        checkPlayerTurn(pid);
        
        MyArrayList<Cards> pHand = getPlayerHand(pid);
        
        if(!validCardPlay(card)){
            if(card.getColor() == Cards.Color.Wild){
                validColor = card.getColor();
                validValue = card.getValue();
            }
            
            if(card.getColor() != validColor){
                JLabel message = new JLabel("Cor inválida! Esperava: " + validColor);
                message.setFont(new Font("Arial",Font.BOLD,48));
                JOptionPane.showMessageDialog(null, message);
                throw new InvalidColorSubmissionException("Cor inválida!", card.getColor(), validColor);
            }
            else if(card.getValue() != validValue){
                JLabel message = new JLabel("Valor inválido! Esperava: " + validValue);
                message.setFont(new Font("Arial",Font.BOLD,48));
                JOptionPane.showMessageDialog(null, message);
                throw new InvalidValueSubmissionException("Valor inválido!", validValue, card.getValue());
            }
        }
        
        pHand.remove(card);
        
        if(hasEmptyHand(this.playerIds[currentPlayer])){
            JLabel message = new JLabel(this.playerIds[currentPlayer] + " ganhou o jogo!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null, message);

            //CRIA UM ARQUIVO PARA SALVAR O HISTÓRICO DE VENCEDORES
            try{
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                FileWriter writer = new FileWriter("historico_de_jogo.txt", true);

                writer.write(now.format(formatter) + "\nVencedor: " + getCurrentPlayer());
                writer.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }

            System.exit(0);
        }
        
        validColor = card.getColor();
        validValue = card.getValue();
        stockpile.add(card);
        
        checkGameDirection();
        
        if(card.getColor() == Cards.Color.Wild){
            validColor = declaredColor;
        }
        
        if(card.getValue() == Cards.Value.DrawTwo){
            pid = playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            JLabel message = new JLabel(pid + " puxou 2 cartas!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null, message);
        }
        
        if(card.getValue() == Cards.Value.WildFour){
            pid = playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            JLabel message = new JLabel(pid + " puxou 4 cartas!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null, message);
        }
        
        if(card.getValue() == Cards.Value.Block){
            JLabel message = new JLabel(this.playerIds[currentPlayer] + " foi bloqueado!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null, message);

            checkGameDirection();
        }
        
        if(card.getValue() == Cards.Value.Reverse){
            JLabel message = new JLabel(pid + " mudou a direção do jogo!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null, message);

            gameDirection ^= true;
            if(gameDirection){
                currentPlayer = (currentPlayer - 2) % playerIds.length;
                if(currentPlayer == -1){
                    currentPlayer = playerIds.length - 1;
                }

                if(currentPlayer == -2){
                    currentPlayer = playerIds.length - 2;
                }
            }
            else{
                currentPlayer = (currentPlayer + 2) % playerIds.length;
            }
        }
    }
}

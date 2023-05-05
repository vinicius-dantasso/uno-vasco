package ufersa.edu.br.unoVasco.model.cards;

import javax.swing.ImageIcon;
import ufersa.edu.br.unoVasco.model.dataStructures.MyStack;

public class CardsDeck {
    private MyStack<Cards> deck;
    
    public CardsDeck(){
        this.deck = new MyStack<>(108);
    }
    
    //MÉTODO PARA RESETAR TODO O BARALHO
    public void resetDeck(){
        Cards.Color[] colors = Cards.Color.values();

        for(int i=0;i<colors.length-1;i++){
            Cards.Color color = colors[i];
            deck.push(new Cards(color, Cards.Value.getValue(0))); //ADICIONAR A CARTA 0

            //ADICIONAR DUAS DE CADA UMA DAS OUTRAS CARTAS
            for(int j=1;j<10;j++){
                deck.push(new Cards(color, Cards.Value.getValue(j))); 
                deck.push(new Cards(color, Cards.Value.getValue(j)));
            }

            //ADICIONAR AS CARTAS REVERSO,BLOQUEIO E +2
            Cards.Value[] values = new Cards.Value[]{Cards.Value.DrawTwo,Cards.Value.Block,Cards.Value.Reverse};
            for(Cards.Value value : values){
                deck.push(new Cards(color, value)); 
                deck.push(new Cards(color, value));
            }

        }

        // ADICIONAR AS CARTAS CORINGA E +4
        Cards.Value[] wilds = new Cards.Value[]{Cards.Value.Wild,Cards.Value.WildFour};
        for(Cards.Value wild : wilds){
            for(int m=0;m<4;m++){
                deck.push(new Cards(Cards.Color.Wild, wild));
            }
        }
    }
    
    public boolean isEmpty(){
        return deck.isEmpty();
    }
    
    //MÉTODO PARA EMBARALHAR AS CARTAS DE FORMA ALEATÓRIA
    public void shuffle(){
        this.deck.shuffle();
    }
    
    //MÉTODO PARA PUXAR UMA CARTA DO BARALHO
    public Cards drawCard(){
        if(deck.isEmpty()){
            throw new IllegalArgumentException("O baralho está vazio");
        }
        return deck.pop();
    }
    
    public ImageIcon drawCardImage(){
        if(deck.isEmpty()){
            throw new IllegalArgumentException("O baralho está vazio");
        }
        return new ImageIcon(deck.peek().toString()+".png");
    }
    
    //MÉTODO PARA PUXAR MAIS DE UMA CARTA DO BARALHO
    public Cards[] drawManyCards(int n){
        if(n < 0){
            throw new IllegalArgumentException("deve puxar um valor positivo de cartas");
        }
        else if(n > deck.size()){
            throw new IllegalArgumentException("não pode puxar todas as cartas");
        }

        Cards[] ret = new Cards[n];
        for(int i=0;i<n;i++){
            ret[i] = deck.pop();
        }

        return ret;
    }
}

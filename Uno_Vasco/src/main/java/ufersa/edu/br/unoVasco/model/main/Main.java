package ufersa.edu.br.unoVasco.model.main;

import ufersa.edu.br.unoVasco.api.game.Sound;
import ufersa.edu.br.unoVasco.view.Menu;

public class Main {
    public static void main(String args[]) throws Exception{
        Sound.playMusic("musics\\hino.wav");
        new Menu().setVisible(true);
    }
}

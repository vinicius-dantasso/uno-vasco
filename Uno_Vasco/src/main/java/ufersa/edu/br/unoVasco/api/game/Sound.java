package ufersa.edu.br.unoVasco.api.game;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    public static void playMusic(String location){
        try{
            File musicPath = new File(location);
            
            if(musicPath.exists()){
                AudioInputStream audioInput  = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
            else{
                System.out.println("NÃO ACHOU!!");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
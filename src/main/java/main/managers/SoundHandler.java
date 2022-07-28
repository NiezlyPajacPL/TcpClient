package main.managers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundHandler {

     public static String MESSAGE_INBOUND = "src/main/java/main/sounds/chat_message_inbound.wav";

    public static void playSound(String voiceLocation) {
        try {
            File voicePath = new File(voiceLocation);
            if (voicePath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(voicePath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();

            } else {
                System.out.println("Can't find file" + voiceLocation);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

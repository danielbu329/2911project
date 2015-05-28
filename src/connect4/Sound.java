package connect4;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sound
{
    Clip sound;

    public Sound(String filename)
    {
        try
        {
            sound = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    new File("src/connect4/" + filename));
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            sound = (Clip) AudioSystem.getLine(info);
            sound.open(inputStream);
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void play()
    {
        sound.setFramePosition(0);
        sound.start();
    }
}

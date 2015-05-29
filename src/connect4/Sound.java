package connect4;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sound
{
    Clip sound;

    /**
     * Constructor for Sound
     * @param filename the name of the audio file
     */
    public Sound(String filename)
    {
        retrieveSound(filename);
    }
    
    /**
     * Retreives the audio file
     * @param filename name of the audio file
     */
    public void retrieveSound(String filename)
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

	/**
	 * Plays the sound
	 */
    public void play()
    {
        sound.setFramePosition(0);
        sound.start();
    }
}

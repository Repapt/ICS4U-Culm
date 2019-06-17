package tools;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Resource accessing class. Loads all the audio/font/midi/png files for other classes to use.
 * Has a load method that initializes all font files and png files, has a loadSong and loadAudio
 * method that loads a specific audio file or midi file
 */
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class Resources {
	
	//image variables
	public static Image title;
	public static ImageView titleV;
	
	//font variables
	public static InputStream fontStream, fontStream2, fontStream3, fontStream4, fontStream5;
	public static Font font, font2, font3, font4, font5;
	
	//midi track variables
	public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static ArrayList<String> beats = new ArrayList<String>();
    public static int resolution;
    
    static Sequence sequence;
    
    //music file variables
	public static Media[] songs;
	
	//Highscore variables

	public static void load() {

		//separate font streams must be created because the stream appears to be consumed
		//when it is used to load a font
		fontStream = Resources.class.getResourceAsStream("res/Prototype.ttf");
		fontStream2 = Resources.class.getResourceAsStream("res/Prototype.ttf");
		fontStream3 = Resources.class.getResourceAsStream("res/Prototype.ttf");
		fontStream4 = Resources.class.getResourceAsStream("res/Prototype.ttf");
		fontStream5 = Resources.class.getResourceAsStream("res/Prototype.ttf");
		
		font2 = Font.loadFont(fontStream2, 30);
		font = Font.loadFont(fontStream, 40);
		font3 = Font.loadFont(fontStream3, 100);
		font4 = Font.loadFont(fontStream4, 150);
		font5 = Font.loadFont(fontStream5, 25);
		
		//loads images
		title = new Image(Resources.class.getResourceAsStream("res/Impulse3.png"));
		titleV = new ImageView(title);
		
		//loads highscore writer
		
	}
	
	//loads and formats the midi tracks for my program to use
	//updates an arraylist called 'beats' that holds all beats, including lane & time
	public static void loadSong(int songNum) throws InvalidMidiDataException, IOException {
    	
    	sequence = MidiSystem.getSequence(Resources.class.getResource("/tools/res/easy" + songNum + ".mid"));
    	
    	//gets the first track in the midi file (the one with all the data)
    	Track track = sequence.getTracks()[0];
    	
    	//gets the PPQ of the midi track; used to convert ticks into seconds
    	resolution = sequence.getResolution();
    	
    	
    	for(int i=0;i<track.size();i++) {
    		//gets the next event in the midi track
    		MidiEvent event = track.get(i);
    		MidiMessage message = event.getMessage();
    		if(message instanceof ShortMessage) {
    			ShortMessage sm = (ShortMessage)message;
    			//checks if the event is a 'beat' (note plays)
    			if(sm.getCommand() == NOTE_ON) {
    				//adds the tick time and the 'lane' the 'beat' occurred in
    				beats.add(event.getTick() + " " + sm.getData1());
    			}
    		}
    	}
    	
    	System.out.println(beats);
    	
    }
	//Loads all the audio
	public static void loadAudio(int numSongs, String[] songNames) {
		songs = new Media[numSongs];
		for(int i=0;i<numSongs;i++) {
			songs[i] = new Media(Resources.class.getResource("res/" + songNames[i] + ".wav").toExternalForm());

		}
	}
	
	public static void write(String score) {
		try {
			System.out.println(score);
			Files.write(Paths.get("src/tools/res/highscores.txt"), score.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

package tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.*;

public class LoadMidi {
	
	public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static ArrayList<String> beats = new ArrayList<String>();
    public static int resolution;
    
    static Sequence sequence;
    
    public static void load(int songNum) throws InvalidMidiDataException, IOException {
    	
    	sequence = MidiSystem.getSequence(LoadMidi.class.getResource("/tools/easy" + songNum + ".mid"));
    	
    	Track track = sequence.getTracks()[0];
    	
    	resolution = sequence.getResolution();
    	
    	
    	for(int i=0;i<track.size();i++) {
    		MidiEvent event = track.get(i);
    		MidiMessage message = event.getMessage();
    		if(message instanceof ShortMessage) {
    			ShortMessage sm = (ShortMessage)message;
    			
    			if(sm.getCommand() == NOTE_ON) {
    				beats.add(event.getTick() + " " + sm.getData1());
    			}
    		}
    	}
    	
    	System.out.println(beats);
    	
    }

}

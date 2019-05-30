package beats;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tools.LoadMidi;

public class Conductor {
	
	int numSongs = 5;
	
	double[] bpm = new double[numSongs];
	double[] beatVol = new double[numSongs];
	double prevFrame, songTime = 0, lastPos = 0;
	double increment;
	
	double beatSpeed, beatTime, travelTime;
		
	Media[] songs = new Media[numSongs];
	
	String[] songNames = new String[numSongs];
	
	double[] delays = new double[numSongs];

	int songNum;
	
	double height;
	
	double tickSize;
	
	MediaPlayer player;
	
	public Conductor(int songNum, double height) throws Exception {
		
		this.songNum = songNum;
		this.height = height;
		
		
		bpm[0] = 152;
		bpm[1] = 100;
		bpm[2] = 170;
		
		beatVol[0] = 0.15;
		beatVol[1] = 0.07;
		beatVol[2] = 0.3;
		
		songNames[0] = "Wilson (Expensive Mistakes)";
		songNames[1] = "Happier";
		songNames[2] = "Can't You See";
		
		for(int i=0;i<3;i++) {
			songs[i] = new Media(getClass().getResource(songNames[i] + ".wav").toExternalForm());
		}
		
		player = new MediaPlayer(songs[songNum]);
		increment = 60/bpm[songNum];
		
		//t = bpm/60
		//travel 550 in t
		//speed = 550/(t)
		//updates 60 times per sec, so
		//speed = speed/60
		
		beatSpeed = (height-100.0)*(bpm[songNum]/7200);
		travelTime = (height-50.0)/(60*beatSpeed);
		//System.out.println(travelTime);
		//beatTime = travelTime;
		
		
	}
	/*
	public void addBeat() {
		beatTime += increment;
	}
	*/
	
	
	
	public void play() throws InvalidMidiDataException, IOException {
		LoadMidi.load(songNum);
		tickSize = LoadMidi.resolution * (bpm[songNum]/60);
		tickSize = 1.0/tickSize;
		prevFrame = System.nanoTime()/1000000000.0;
		player.play();
	}
	
	public void setSong(int num){
		songNum = num;
		player = new MediaPlayer(songs[songNum]);
	}
	
	public double getTickSize() {
		return tickSize;
	}
	
	public String[] getSongList() {
		return songNames;
	}
	
	public String getSongName() {
		return songNames[songNum];
	}
	
	public double getBeatVol() {
		return beatVol[songNum];
	}
	public double getTravelTime() {
		return travelTime;
	}
	
	public double getDelay() {
		return delays[songNum];
	}
	
	public double getBeatSpeed() {
		return beatSpeed;
	}
	
	public double getBeatTime() {
		return beatTime;
	}
	
	public double songPosition() {
		return songTime - delays[songNum];
	}
	
	public double getBPM() {
		return bpm[songNum];
	}
	
	public double getIncr() {
		return increment;
	}
	
	
	public double songLength() {
		return player.getTotalDuration().toSeconds();
	}
	
	public int update() {
		songTime += System.nanoTime()/1000000000.0 - prevFrame;
		prevFrame = System.nanoTime()/1000000000.0;
		
		if(Math.abs(songPosition() - songLength()) < 0.05) {
			return 1;
			
		} else if (player.getCurrentTime().toSeconds() != lastPos) {
			
			songTime = (songTime + player.getCurrentTime().toSeconds())/2;
			lastPos = player.getCurrentTime().toSeconds();
			
		}
		
		return 0;
		
		
	}
	
	

}

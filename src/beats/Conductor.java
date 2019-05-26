package beats;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tools.LoadMidi;

public class Conductor {
	
	int numSongs = 4;
	
	double[] bpm = new double[numSongs];
	double[] actualStart = new double[numSongs];
	double prevFrame, songTime = 0, lastPos = 0;
	double increment;
	
	double beatSpeed, beatTime, travelTime;
		
	Media[] songs = new Media[numSongs];
	
	double[] delays = new double[numSongs];

	int songNum;
	
	double height;
	
	double tickSize;
	
	MediaPlayer player;
	
	public Conductor(int songNum, double height) throws Exception {

		LoadMidi.load(songNum);
		
		this.songNum = songNum;
		this.height = height;
		
		actualStart[1] =  0.456;
		actualStart[2] = 10.722;
		
		bpm[1] = 125;
		bpm[2] = 95;
		bpm[3] = 100;
		
		
		songs[0] = new Media(getClass().getResource("Take On Me.mp3").toExternalForm());
		songs[1] = new Media(getClass().getResource("01 What Makes You Beautiful.m4a").toExternalForm());
		songs[2] = new Media(getClass().getResource("Closer.mp3").toExternalForm());
		songs[3] = new Media(getClass().getResource("Happier.wav").toExternalForm());
		
		player = new MediaPlayer(songs[songNum]);
		increment = 60/bpm[songNum];
		
		tickSize = LoadMidi.resolution * (bpm[songNum]/60);
		tickSize = 1.0/tickSize;
		
		beatSpeed = (height - 50.0)*(bpm[songNum]/7200);
		travelTime = (height-50.0)/(60*beatSpeed);
		beatTime = actualStart[songNum] - travelTime;
		
		
	}
	
	public void addBeat() {
		beatTime += increment;
	}
	
	public double getTickSize() {
		return tickSize;
		
	}
	
	
	public void play() {
		prevFrame = System.nanoTime()/1000000000.0;
		player.play();
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
	
	public double getActualStart() {
		return actualStart[songNum];
	}
	
	public double songLength() {
		return player.getTotalDuration().toSeconds();
	}
	
	public int update() {
		songTime += System.nanoTime()/1000000000.0 - prevFrame;
		prevFrame = System.nanoTime()/1000000000.0;
		
		if(Math.abs(songPosition() - songLength()) < 0.01) {
			return 1;
			
		} else if (player.getCurrentTime().toSeconds() != lastPos) {
			
			songTime = (songTime + player.getCurrentTime().toSeconds())/2;
			lastPos = player.getCurrentTime().toSeconds();
			
		}
		
		return 0;
		
		
	}
	
	

}

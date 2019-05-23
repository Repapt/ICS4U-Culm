package beats;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Conductor {
	
	double[] bpm = new double[3];
	double[] actualStart = new double[3];
	double prevFrame, songTime = 0, lastPos = 0;
	double increment;
	
	double beatSpeed, beatTime, travelTime;
		
	Media[] songs = new Media[3];
	
	double[] delays = new double[3];
	
	int songNum;
	
	double height;
	
	MediaPlayer player;
	
	public Conductor(int songNum, double height) {
		
		this.songNum = songNum;
		this.height = height;

		delays[1] = 1.464;
		
		actualStart[1] =  0.456;
		actualStart[2] = 10.722;
		
		bpm[1] = 125;
		bpm[2] = 95;
		
		songs[0] = new Media(getClass().getResource("Take On Me.mp3").toExternalForm());
		songs[1] = new Media(getClass().getResource("01 What Makes You Beautiful.m4a").toExternalForm());
		songs[2] = new Media(getClass().getResource("Closer.mp3").toExternalForm());
		
		player = new MediaPlayer(songs[songNum]);
		increment = 60/bpm[songNum];
		

		
		beatSpeed = (height - 50.0)*(bpm[songNum]/14400);
		travelTime = (height-50.0)/(60*beatSpeed);
		beatTime = actualStart[songNum] - travelTime;
		
		
	}
	
	public void addBeat() {
		beatTime += increment;
	}
	
	
	public void play() {
		prevFrame = System.nanoTime()/1000000000.0;
		player.play();
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
		
		if(Math.abs(player.getCurrentTime().toSeconds() - songLength()) < 0.1) {
			return 1;
			
		} else if (player.getCurrentTime().toSeconds() != lastPos) {
			
			songTime = (songTime + player.getCurrentTime().toSeconds())/2;
			lastPos = player.getCurrentTime().toSeconds();
			
		}
		
		return 0;
		
		
	}
	
	

}

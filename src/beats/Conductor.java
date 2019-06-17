package beats;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Tempo keeper for game. Controls the audio being played and keeps track of the game time.
 * Keeps all track information including names, artists, bpm, ticksize
 */
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javafx.scene.media.MediaPlayer;
import tools.Resources;

public class Conductor {
	
	int numSongs = 4;
	
	double[] bpm = new double[numSongs];
	double[] beatVol = new double[numSongs];
	double prevFrame, songTime = 0, lastPos = 0;
	double increment;
	
	//Variables controlling how quickly 'beats' travel across the screen;
	//Useful for syncing up when 'beats' are generated vs when they require a key press
	double beatSpeed, beatTime, travelTime;
	
	String[] songNames = new String[numSongs];
	String[] artists = new String[numSongs];

	int songNum;
	
	double height;
	
	double tickSize;
	
	MediaPlayer player;
	
	public Conductor(int songNum, double height) throws Exception {
		
		this.songNum = songNum;
		this.height = height;
		
		//These are manually typed in
		//In the (distant) future, these will be automatically generated
		bpm[0] = 152;
		bpm[1] = 100;
		bpm[2] = 170;
		bpm[3] = 90;
		
		beatVol[0] = 0.3;
		beatVol[1] = 0.14;
		beatVol[2] = 0.6;
		beatVol[3] = 0.3;
		
		songNames[0] = "Wilson (Expensive Mistakes)";
		songNames[1] = "Happier";
		songNames[2] = "Can't You See";
		songNames[3] = "Sunflower";
		
		artists[0] = "Fall Out Boy";
		artists[1] = "Marshmello ft. Bastille";
		artists[2] = "FIDLAR";
		artists[3] = "Post Malone & Swae Lee";
		
		Resources.loadAudio(numSongs, songNames);
		
		player = new MediaPlayer(Resources.songs[songNum]);
		
		//calculates based on distance they travel and how quickly they aught to move
		beatSpeed = (height-100.0)*(bpm[songNum]/7200);
		travelTime = (height-50.0)/(60*beatSpeed);
		
		
	}
	
	
	
	public void play() throws InvalidMidiDataException, IOException {
		Resources.loadSong(songNum);
		//the duration of one 'tick' in the midi track, in seconds
		tickSize = Resources.resolution * (bpm[songNum]/60);
		tickSize = 1.0/tickSize;
		prevFrame = System.nanoTime()/1000000000.0;
		player.play();
	}
	
	public void setSong(int num){
		songNum = num;
		player = new MediaPlayer(Resources.songs[songNum]);
		
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
	public String[] getArtistList() {
		return artists;
	}
	public String getArtistName() {
		return artists[songNum];
	}
	public double getBeatVol() {
		return beatVol[songNum];
	}
	public double getTravelTime() {
		return travelTime;
	}
	
	public double getBeatSpeed() {
		return beatSpeed;
	}
	
	public double getBeatTime() {
		return beatTime;
	}
	
	public double songPosition() {
		return songTime;
	}
	
	public double getBPM() {
		return bpm[songNum];
	}
	
	public double getIncr() {
		return increment;
	}
	
	public double getSongVol() {
		return player.getVolume();
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
	
	public void pause() {
		player.pause();
	}
	public void unpause() {
		prevFrame = System.nanoTime()/1000000000.0;
		player.play();
	}
	public void setBeatVol(double vol) {
		beatVol[songNum] = vol;
	}
	public void setSongVol(double vol) {
		player.setVolume(vol);
	}
	
	

}

package beats;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * handles the kick drum sound when a beat is made/during the starting sequence.
 */
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class DrumSound {

	Media sound;
	MediaPlayer player;
	
	public DrumSound(double vol, int num) {
		//gets the drum sound and plays it at a given volume
		sound = new Media(getClass().getResource("Drum" + num + ".wav").toExternalForm());
		player = new MediaPlayer(sound);
		player.setVolume(vol);
		player.play();
	
	}
	
	public boolean isEnded() {
		return player.getTotalDuration().toSeconds() == player.getCurrentTime().toSeconds();
	}
}

package beats;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class DrumSound {

	Media sound;
	MediaPlayer player;
	
	public DrumSound(double vol) {
		sound = new Media(getClass().getResource("Drum.wav").toExternalForm());
		player = new MediaPlayer(sound);
		player.setVolume(vol);
		player.play();
	
	}
	
	public boolean isEnded() {
		return player.getTotalDuration().toSeconds() == player.getCurrentTime().toSeconds();
	}
}

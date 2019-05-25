package states;

import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import main.Main;
import tools.Print;

public class Menu extends GameState {
	
	Main game;
	Print startText;
	
	public Menu(Main g) {
		game = g;
		
		startText = new Print(40, 400, -1, Color.WHITE, "Press any key to begin");
	}

	@Override
	public void update(int counter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPress(KeyEvent event) {
		
		
	}

	@Override
	public void draw(Group group) {
		
		group.getChildren().add(startText.getText());
		
	}

	@Override
	public void keyRelease(KeyEvent event) {
		game.changeState(new Playing(game));
		
	}

}

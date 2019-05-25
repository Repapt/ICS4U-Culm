package main;

import java.util.ArrayList;

import beats.Beat;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import states.*;

public class Main extends Application{
	
	Button start;
	Button button;
	int height = 600;
	int width = 400;
	public ArrayList<Beat> beats;
	GameState game;
	
	UserInput input = new UserInput();

	public static void main(String[] args) {
		launch(args);
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void start(Stage mainStage) throws Exception {
		
		Group root = new Group();
		
		mainStage.setTitle("Weezer");
		
		
		game = new Menu(this);
		
		
		Scene scene = new Scene(root, width, height, Color.BLACK);
		
		scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
			
			public void handle(KeyEvent event) {
				game.keyRelease(event);
			}
			
		}
		);	
		
		AnimationTimer timer = new AnimationTimer() {
			int counter = 0;
			
			public void handle(long currTime) {
				counter ++;
				root.getChildren().clear();
				
				game.update(counter);
				game.draw(root);
			}
		};
		
		timer.start();
		
		mainStage.setScene(scene);
		mainStage.show();
		
		
	}
	
	public void changeState(GameState state) {
		game = state;
	}
	
	public void end() throws Exception {
		System.out.println("end");
		stop();
		Platform.exit();
	}
	

}

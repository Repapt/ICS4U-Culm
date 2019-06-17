package main;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Entry point for my program.
 * Rhythm game using javafx where user must match key presses to the beat of the song along with
 * falling 'beats' that signify a key press.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import states.*;
import tools.Resources;

public class Main extends Application{
	
	//Initializing variables
	int height = 600;
	int width = 400;
	GameState game;
	
	int counter = 0;
	

	//Entry point for program
	public static void main(String[] args) {
		Resources.load();
		launch(args);
	}
	
	//returns height of window
	public int getHeight() {
		return height;
	}
	
	//returns width of window
	public int getWidth() {
		return width;
	}
	
	@Override
	public void start(Stage mainStage) throws Exception {
		
		//all elements are added to this group, which are then displayed on the scene
		Group root = new Group();
		
		mainStage.setTitle("Impulse");
		
		//beginning gamestate is menu
		game = new Menu(this);
		
		Scene scene = new Scene(root, width, height, Color.BLACK);
		
		//event handling
		scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
			
			@Override
			public void handle(KeyEvent event) {
				game.keyRelease(event);
			}
			
		}
		);	
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
			
			@Override
			public void handle(KeyEvent event) {
				game.keyPress(event);
			}
			
		}
		);	
		
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) {
				game.click(event);
			}
			
		}
		);	
		
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				game.moved(event);
				
			}
		});
		
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//System.out.println(event.getX() + ", " + event.getY());
				game.dragged(event);
			}
		});
		
		scene.setOnMouseReleased(new EventHandler <MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				game.mouseRelease(event);
			}
		});
		
		
		//main game loop
		AnimationTimer timer = new AnimationTimer() {
			
			@Override
			public void handle(long currTime) {
				
				counter ++;
				root.getChildren().clear();
				
				//separated for ease of understanding when writing and debugging
				game.update(counter);
				game.draw(root);
			}
		};
		
		timer.start();
		
		mainStage.setScene(scene);
		mainStage.show();
		
		
	}
	
	//switches various gamestates
	public void changeState(GameState state) {
		game = state;
	}
	//terminates program
	public void end() throws Exception {
		System.out.println("end");
		stop();
		Platform.exit();
	}
	//returns counter variable to 0
	public void refreshCounter() {
		counter = 0;
	}
	

}

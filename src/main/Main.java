package main;

import java.util.ArrayList;

import beats.Beat;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import states.*;
import tools.Images;

public class Main extends Application{
	
	Button start;
	Button button;
	int height = 600;
	int width = 400;
	public ArrayList<Beat> beats;
	GameState game;
	
	int counter = 0;
	

	public static void main(String[] args) {
		Images.load();
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
		
		mainStage.setTitle("Impulse");
		
		
		game = new Menu(this);
		
		Scene scene = new Scene(root, width, height, Color.BLACK);
		
		scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
			
			public void handle(KeyEvent event) {
				game.keyRelease(event);
			}
			
		}
		);	
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
			
			public void handle(KeyEvent event) {
				game.keyPress(event);
			}
			
		}
		);	
		
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			
			public void handle(MouseEvent event) {
				game.click(event);
			}
			
		}
		);	
		
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				game.moved(event);
				
			}
		});
		
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				//System.out.println(event.getX() + ", " + event.getY());
				game.dragged(event);
			}
		});
		
		scene.setOnMouseReleased(new EventHandler <MouseEvent>(){
			public void handle(MouseEvent event) {
				game.mouseRelease(event);
			}
		});
		
		
		
		AnimationTimer timer = new AnimationTimer() {
			
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
	public void refreshCounter() {
		counter = 0;
	}
	

}

package game;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Editor {

	private static Scene scene;
	private static Group entities;
	private static Group toolbar;
	private static Rectangle slider;
	private static double startingX;
	
	public static void createScene(double width, double height) {
		Group g = new Group();
		entities = new Group();
		toolbar = new Group();
		
		Image i = new Image("file:pictures/Shop/Shop.png");
		ImageView iv = new ImageView(i);
		entities.getChildren().add(iv);
		iv.setFitWidth(width - 200);
		iv.setFitHeight(height - 25);
		
		slider = new Rectangle(150, 25, Paint.valueOf("#970"));
		slider.setLayoutY(height - 25);
		slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getSceneX() - startingX >= 0 &&
						event.getSceneX() - startingX + slider.getWidth() <= width - 200) {
					slider.setLayoutX(event.getSceneX() - startingX);
				}
			}
		});
		slider.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startingX = event.getSceneX() - slider.getLayoutX();
			}
		});
		
		Image i2 = new Image("file:pictures/Shop/Shop.png");
		ImageView iv2 = new ImageView(i2);
		toolbar.getChildren().add(iv2);
		iv2.setLayoutX(width - 200);
		iv2.setLayoutY(0);
		iv2.setFitWidth(200);
		iv2.setFitHeight(height);
		
		g.getChildren().addAll(entities, toolbar, slider);
		
		scene = new Scene(g, width, height);
	}
	
	
	public static Scene getScene() {
		return scene;
	}
}

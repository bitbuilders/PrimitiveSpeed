package game;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import models.Platform;
import models.Player;

public class Game {
	
	private Label seconds;
	private Label gold;
	private boolean isEndless;
	private Pane entities;
	private Scene gameScene;
	private Menu menu;
	private Player player;
	private ArrayList<ArrayList<Pane>> platforms = new ArrayList<ArrayList<Pane>>();
	//private ArrayList<Pane> platforms = new ArrayList<>();
	private final double[] positions;
	
	public Game(Menu menu, boolean isEndless) {
		this.isEndless = isEndless;
		this.menu = menu;
		
		positions = new double[] {
			menu.getDefaultHeight(),
			menu.getDefaultHeight() - 200,
			menu.getDefaultHeight() - 400
		};
		
		player = new Player(menu.getDefaultHeight());
		gameScene = createScene();
	}
	
	private Scene createScene() {
		Group g = new Group();
		entities = new Pane();
		
		Image i = new Image("file:pictures/Coin.png");
		ImageView iv = new ImageView(i);
		iv.setFitWidth(17);
		iv.setFitHeight(17);
		gold = new Label("0");
		gold.setLayoutX(20);
		gold.setLayoutY(-2);
		gold.setStyle("-fx-font: 18 Arial; -fx-text-fill: #CA0;");
		g.getChildren().add(iv);
		g.getChildren().add(gold);
		g.setLayoutX(2);
		g.setLayoutY(2);
		
		Image i2 = new Image("file:pictures/Clock.png");
		ImageView iv2 = new ImageView(i2);
		iv2.setLayoutX(75);
		iv2.setFitWidth(17);
		iv2.setFitHeight(17);
		seconds = new Label("0");
		seconds.setLayoutX(95);
		seconds.setLayoutY(-2);
		seconds.setStyle("-fx-font: 18 Arial; -fx-text-fill: #777;");
		g.getChildren().add(iv2);
		g.getChildren().add(seconds);
		g.setLayoutX(2);
		g.setLayoutY(2);
		
		entities.getChildren().add(g);
		
		entities.getChildren().add(player.getImageView());
		Platform plat = new Platform((int) (menu.getScene().getWidth() / 20 + 10), menu.getDefaultHeight(), 0);
		entities.getChildren().add(plat.getPlatform());
		plat.getPlatform().toBack();
		
		ArrayList<Pane> p = new ArrayList<>();
		ArrayList<Pane> p2 = new ArrayList<>();
		ArrayList<Pane> p3 = new ArrayList<>();
		platforms.add(p);
		platforms.add(p2);
		platforms.add(p3);
		
		platforms.get(2).add(plat.getPlatform());
		
		Scene s = new Scene(entities, menu.getDefaultWidth(), menu.getDefaultHeight());
		gameScene = s;
		//System.out.println(gameScene.getWidth() + ";" + gameScene.getHeight());
		return s;
	}
	
	public Label getSeconds() {
		return seconds;
	}
	
	public Label getLabel() {
		return gold;
	}
	
	public ArrayList<ArrayList<Pane>> getPlatforms() {
		return platforms;
	}
	
	public Scene getScene() {
		return gameScene;
	}
	
	public Pane getEntities() {
		return entities;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isEndless() {
		return isEndless;
	}
	
	public double[] getPositions() {
		return positions;
	}
}

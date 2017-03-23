package game;

import java.util.ArrayList;
import java.util.Random;

import interfaces.Pausable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import models.Coin;
import models.Entity;
import models.Player;

public class EntityHandler implements Runnable, Pausable {

	private Game game;
	private PlatformHandler ph;
	private ArrayList<Entity> entities = new ArrayList<>();
	private double seconds;
	private double coinMultiplier = 2.0;
	private double lastCoinSpawn = 0;
	private double coinSpawnTime = 0;
	private Thread thread;
	private Timeline timeline;
	private static Random rand = new Random();
	private Coin c = new Coin(0,0);

	public EntityHandler(Game g, PlatformHandler ph) {
		game = g;
		this.ph = ph;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(10),
		        ae -> timerTick()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	private void timerTick() {
		seconds += .01;
		
		moveEntities();
		
		if (seconds - lastCoinSpawn >= coinSpawnTime) {
			createCoin();
		}
	}
	
	private void moveEntities() {
		for (int i = 0; i < entities.size(); i++) {
			ImageView iv = entities.get(i).getImageView();
			iv.setLayoutX(iv.getLayoutX() - ph.getScrollSpeed());
			if (entities.get(i).getClass().equals(c.getClass())) {
				checkCollected((Coin) entities.get(i));
			}
			if (iv.getLayoutX() + iv.getFitWidth() < - 50) {
				game.getEntities().getChildren().remove(entities.get(i).getImageView());
				entities.remove(entities.get(i));
			}
		}
	}
	
	private void checkCollected(Coin c) {
		Player p = game.getPlayer();
		
		double leftBound = p.getImageView().getBoundsInParent().getMinX() - p.getPickupRange();
		double rightBound = p.getImageView().getBoundsInParent().getMaxX() + p.getPickupRange();
		double topBound = p.getImageView().getBoundsInParent().getMinY() - p.getPickupRange();
		double bottomBound = p.getImageView().getBoundsInParent().getMaxY() + p.getPickupRange();
		
		if (rightBound >= c.getImageView().getBoundsInParent().getMinX() &&
				leftBound <= c.getImageView().getBoundsInParent().getMaxX()) {
			if (topBound <= c.getImageView().getBoundsInParent().getMaxY() &&
					bottomBound >= c.getImageView().getBoundsInParent().getMinY()) {
				if (c.getImage().equals(Coin.getCoin())) {
					game.getPlayer().setGold(game.getPlayer().getGold() + 10);
				}
				else {
					game.getPlayer().setGold(game.getPlayer().getGold() + 1);
				}
				game.getLabel().setText("" + game.getPlayer().getGold());;
				
				entities.remove(c);
				game.getEntities().getChildren().remove(c.getImageView());
			}
		}
	}
	
	private void createCoin() {
		double x = 0;
		double y = 0;
		do {
			x = game.getScene().getWidth();
			int a = rand.nextInt(3);
			
			int height = (int) game.getScene().getHeight();
			if (a == 0) {
				y = rand.nextInt((height - 600) + 65) + 20;
			}
			else if (a == 1) {
				y = rand.nextInt((height - 200) - (height - 400) - 75) + (height - 400);
			}
			else {
				y = rand.nextInt(height - (height - 200) - 100) + (height - 200) + 25;
			}
		}while(false);
		
		Coin c = new Coin(x, y);
		entities.add(c);
		game.getEntities().getChildren().add(c.getImageView());
		
		lastCoinSpawn = seconds;
		
		double w = rand.nextDouble() * 10 + 1;
		w /= coinMultiplier;
		coinSpawnTime = w;
	}
	
	public double getCoinMultiplier() {
		return coinMultiplier;
	}
	
	public void setCoinMultiplier(double d) {
		this.coinMultiplier = d;
	}
	
	public void stopTimer() {
		timeline.stop();
	}
	
	public void startTimer() {
		timeline.play();
	}
	
	public void pauseTimer() {
		timeline.pause();
	}
}

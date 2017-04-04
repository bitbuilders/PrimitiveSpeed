package game;

import java.util.ArrayList;
import java.util.Random;

import interfaces.Pausable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.Bullet;
import models.Coin;
import models.Enemy;
import models.Entity;
import models.MeleeEnemy;
import models.Player;
import models.RangedEnemy;

public class EntityHandler implements Runnable, Pausable {

	private Game game;
	private PlatformHandler ph;
	private ArrayList<Bullet> bullets = new ArrayList<>();
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private ArrayList<Entity> entities = new ArrayList<>();
	private double seconds;
	private double coinMultiplier = 3.5;
	private double lastCoinSpawn = 0;
	private double coinSpawnTime = 0;
	private int enemySpawnTime = 6;
	private double lastEnemySpawn = 0;
	private double enemyTime = 10;
	private double lastShot = 0;
	private double shotTime = 0;
	private Thread thread;
	private Timeline timeline;
	private Timeline levelTimeline;
	private static Random rand = new Random();
	private Coin c = new Coin(0,0);
	private ModeSelection ms;

	public EntityHandler(Game g, PlatformHandler ph, double multiplier, ModeSelection ms,
			ArrayList<Entity> entities, ArrayList<Enemy> enemies) {
		this.ms = ms;
		game = g;
		//System.out.println(entities.size());
		if (entities != null && !entities.isEmpty()) {
			this.entities = entities;
		}
		if (enemies != null && !enemies.isEmpty()) {
			this.enemies = enemies;
		}
		coinMultiplier = multiplier;
		this.ph = ph;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		if (game.isEndless()) {
			timeline = new Timeline(new KeyFrame(
			        Duration.millis(10),
			        ae -> timerTick()));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		}
		else {
			levelTimeline = new Timeline(new KeyFrame(
			        Duration.millis(10),
			        ae -> levelTick()));
			levelTimeline.setCycleCount(Animation.INDEFINITE);
			levelTimeline.play();
		}
	}
	
	private void levelTick() {
		seconds += .01;
		
		moveEntities();
		checkMelee();
		moveBullets();
		
		//System.out.println(entities.size());
		
		if (seconds - lastShot >= shotTime) {
			EnemyShoot();
		}
	}
	
	private void timerTick() {
		seconds += .01;
		
		moveEntities();
		checkMelee();
		moveBullets();
		
		if (seconds - lastCoinSpawn >= coinSpawnTime) {
			createCoin();
		}
		
		if (seconds - lastEnemySpawn >= enemyTime) {
			createEnemy();
		}
		
		if (seconds - lastShot >= shotTime) {
			EnemyShoot();
		}
	}
	
	private void checkMelee() {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getClass().equals(MeleeEnemy.class)) {
				if (Intersects(enemies.get(i).getImageView(), game.getPlayer())) {
					if (game.getPlayer().isCanStomp() &&
							game.getPlayer().getImageView().getBoundsInParent().getMaxY() <=
							enemies.get(i).getImageView().getBoundsInParent().getMinY() + 30) {
						game.getPlayer().setJumpHeight(game.getPlayer().getStartingHeight() / 2);
						ms.getKey().setFalling(true);
					}
					else {
						double enemyHorz = enemies.get(i).getImageView().getBoundsInParent().getMinX() +
								enemies.get(i).getImageView().getBoundsInParent().getWidth() / 2;
						double playerHorz = game.getPlayer().getImageView().getBoundsInParent().getMinX() +
								game.getPlayer().getImageView().getBoundsInParent().getWidth() / 2;
						
						game.getPlayer().getKnockedBack(playerHorz - enemyHorz, ms.getKey());
						damagePlayer(enemies.get(i).getImageView());
						entities.remove(enemies.get(i));
						enemies.remove(enemies.get(i));
					}
				}
			}
		}
	}
	
	private void moveBullets() {
		for (int i = 0; i < bullets.size(); i++) {
			if (!bullets.get(i).isMovingRight()) {
				bullets.get(i).getImageView().setLayoutX(bullets.get(i).getImageView().getLayoutX() - 4);
			}
			else {
				bullets.get(i).getImageView().setLayoutX(bullets.get(i).getImageView().getLayoutX() + 5);
			}
			if (Intersects(bullets.get(i).getImageView(), game.getPlayer()) &&
					!bullets.get(i).isMovingRight()) {
				damagePlayer(bullets.get(i).getImageView());
				entities.remove(bullets.get(i));
				bullets.remove(bullets.get(i));
				if (game.getPlayer().getSpeed() >= 1.2) {
					game.getPlayer().setSpeed(game.getPlayer().getSpeed() - .5);
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), 
									ae -> unSlow(game.getPlayer())));
					timeline.play();
				}
			}
			
			for (int x = 0; x < enemies.size(); x++) {
				if (i < bullets.size() && 
						Intersects(bullets.get(i).getImageView(), enemies.get(x).getImageView()) &&
						bullets.get(i).isMovingRight()) {
					game.getPlayer().setGold(game.getPlayer().getGold() + enemies.get(x).getGoldValue());
					game.getLabel().setText("" + game.getPlayer().getGold());
					
					entities.remove(bullets.get(i));
					game.getEntities().getChildren().remove(bullets.get(i).getImageView());
					bullets.remove(bullets.get(i));
					
					entities.remove(enemies.get(x));
					game.getEntities().getChildren().remove(enemies.get(x).getImageView());
					enemies.remove(enemies.get(x));
				}
			}
		}
	}
	
	private void unSlow(Player p) {
		p.setSpeed(p.getSpeed() + .5);
	}
	
	private Boolean Intersects(ImageView b, ImageView e) {
		if (b.getBoundsInParent().getMinX() <= e.getBoundsInParent().getMaxX() - 10 &&
				b.getBoundsInParent().getMaxX() >= e.getBoundsInParent().getMinX() + 10 &&
				b.getBoundsInParent().getMinY() <= e.getBoundsInParent().getMaxY() - 10 &&
				b.getBoundsInParent().getMaxY() >= e.getBoundsInParent().getMinY() + 10) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Boolean Intersects(ImageView b, Player p) {
		if (b.getBoundsInParent().getMinX() <= p.getImageView().getBoundsInParent().getMaxX() - 10 &&
				b.getBoundsInParent().getMaxX() >= p.getImageView().getBoundsInParent().getMinX() + 10 &&
				b.getBoundsInParent().getMinY() <= p.getImageView().getBoundsInParent().getMaxY() - 10 &&
				b.getBoundsInParent().getMaxY() >= p.getImageView().getBoundsInParent().getMinY() + 10) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void damagePlayer(ImageView b) {
		game.getEntities().getChildren().remove(b);
		game.getPlayer().setLives(game.getPlayer().getLives() - 1);
		if (game.getPlayer().getLives() <= 0) {
			gameOver();
		}
		else {
			game.getHeartGroup().getChildren().remove(game.getHearts().size() - 1);
			game.getHearts().remove(game.getHearts().size() - 1);
		}
	}
	
	private void gameOver() {
		game.toggleShopDisplay();
		ms.pauseAllTimers();
	}
	
	private void moveEntities() {
		for (int i = 0; i < entities.size(); i++) {
			//System.out.println(bullets.size());
			ImageView iv = entities.get(i).getImageView();
			iv.setLayoutX(iv.getLayoutX() - ph.getScrollSpeed());
			if (entities.get(i).getClass().equals(c.getClass())) {
				checkCollected((Coin) entities.get(i));
			}
			if (iv.getLayoutX() + iv.getFitWidth() < - 50) {
				game.getEntities().getChildren().remove(entities.get(i).getImageView());
				if (bullets.contains(entities.get(i))) {
					bullets.remove(entities.get(i));
				}
				if (enemies.contains(entities.get(i))) {
					enemies.remove(entities.get(i));
				}
				entities.remove(entities.get(i));
			}
			else if (iv.getLayoutX() > game.getScene().getWidth() &&
					entities.get(i).getClass().equals(Bullet.class)) {
				game.getEntities().getChildren().remove(entities.get(i).getImageView());
				bullets.remove(entities.get(i));
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
				c.playAnimation(game.getPlayer(), game);
				entities.remove(c);
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
	
	private void createEnemy() {
		ArrayList<ArrayList<Pane>> positions = ph.getPositions();
		int x = rand.nextInt(3);
		Enemy e;
		if (x <= 1) {
			e = new MeleeEnemy();
		}
		else {
			e = new RangedEnemy();
		}
		e.getImageView().setFitWidth(64);
		e.getImageView().setFitHeight(64);
		
		e.getImageView().setLayoutX(game.getScene().getWidth());
		int y = rand.nextInt(3);
		if (y == 0) {
			if (positions.get(0).size() > 0) {
			if (positions.get(0).get(positions.get(0).size() - 1).getBoundsInParent().getMaxX() >=
					game.getScene().getWidth() + 64) {
				e.getImageView().setLayoutY(game.getScene().getHeight() - 558);
				enemies.add(e);
				entities.add(e);
				game.getEntities().getChildren().add(e.getImageView());
				enemyTime = rand.nextInt(enemySpawnTime - (enemySpawnTime - 3) + 1) + (enemySpawnTime - 3);
			}
			else {
				enemyTime = 1;
			}
			}
		}
		else if (y == 1) {
			if (positions.get(1).size() > 0) {
			if (positions.get(1).get(positions.get(1).size() - 1).getBoundsInParent().getMaxX() >=
					game.getScene().getWidth() + 64) {
				e.getImageView().setLayoutY(game.getScene().getHeight() - 358);
				enemies.add(e);
				entities.add(e);
				game.getEntities().getChildren().add(e.getImageView());
				enemyTime = rand.nextInt(enemySpawnTime - (enemySpawnTime - 3) + 1) + (enemySpawnTime - 3);
			}
			else {
				enemyTime = 1;
			}
			}
		}
		else {
			if (positions.get(2).size() > 0) {
			if (positions.get(2).get(positions.get(2).size() - 1).getBoundsInParent().getMaxX() >=
					game.getScene().getWidth() + 64) {
				e.getImageView().setLayoutY(game.getScene().getHeight() - 158);
				enemies.add(e);
				entities.add(e);
				game.getEntities().getChildren().add(e.getImageView());
				enemyTime = rand.nextInt(enemySpawnTime - (enemySpawnTime - 3) + 1) + (enemySpawnTime - 3);
			}
			else {
				enemyTime = 1;
			}
		}
		}
		
		lastEnemySpawn = seconds;
	}
	
	private void EnemyShoot() {
		boolean hasRanged = false;
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getClass().equals(RangedEnemy.class)) {
				hasRanged = true;
			}
		}
		
		if (hasRanged) {
		do {
			int x = rand.nextInt(enemies.size());
			
			if (!enemies.get(x).getClass().equals(RangedEnemy.class)) {
				hasRanged = false;
			}
			else {
				Bullet b = ((RangedEnemy) enemies.get(x)).Shoot();
				entities.add(b);
				game.getEntities().getChildren().add(b.getImageView());
				b.getImageView().setLayoutX(enemies.get(x).getImageView().getLayoutX() - 48);
				b.getImageView().setLayoutY(enemies.get(x).getImageView().getLayoutY() + 
						enemies.get(x).getImageView().getBoundsInParent().getHeight() / 2);
				bullets.add(b);
				hasRanged = true;
			}
		}while(!hasRanged);
		}
		shotTime = rand.nextDouble() * 1.5 + 1;
		lastShot = seconds;
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public double getCoinMultiplier() {
		return coinMultiplier;
	}
	
	public void setCoinMultiplier(double d) {
		this.coinMultiplier = d;
	}
	
	public void stopTimer() {
		if (game.isEndless()) {
			timeline.stop();	
		}
		else {
			levelTimeline.stop();
		}
	}
	
	public void startTimer() {
		entities.clear();
		enemies.clear();
		bullets.clear();
		if (game.isEndless()) {
			timeline.play();
		}
		else {
			levelTimeline.play();
		}
	}
	
	public void pauseTimer() {
		game.getPlayer().setSpeed(game.getPlayer().getMaxSpeed());
		if (game.isEndless()) {
			timeline.pause();
		}
		else {
			levelTimeline.pause();
		}
	}
}

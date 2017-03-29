package models;

import java.util.Random;

import game.Game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Coin extends Entity {

	private static Image dirtyCoin = new Image("file:pictures/DirtyCoin.png");
	public static Image coin = new Image("file:pictures/Coin.png");
	private static Random rand = new Random();
	private Timeline timeline;
	
	public Coin(double x, double y) {
		setImageView(new ImageView());
		int a = rand.nextInt(5);
		if (a == 0) {
			getImageView().setImage(coin);
		}
		else {
			getImageView().setImage(dirtyCoin);
		}
		
		getImageView().setLayoutX(x);
		getImageView().setLayoutY(y);
		getImageView().setFitWidth(17);
		getImageView().setFitHeight(17);
		getImageView().toFront();
	}
	
	public void playAnimation(Player p, Game g) {
		timeline = new Timeline(new KeyFrame(Duration.millis(2), ae -> timerTick(p, g)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	private void timerTick(Player p, Game g) {
		checkX(p);
		checkY(p);
		checkCollect(p, g);
	}
	
	private void addGold(Player p, Game g) {
		if (getImage().equals(Coin.getCoin())) {
			p.setGold(p.getGold() + 10);
		}
		else {
			p.setGold(p.getGold() + 1);
		}
		g.getLabel().setText("" + g.getPlayer().getGold());;
	}
	
	private void checkX(Player p) {
		ImageView iv = getImageView();
		ImageView piv = p.getImageView();
		if (iv.getBoundsInParent().getMinX() > 
		piv.getBoundsInParent().getMinX() + piv.getBoundsInParent().getWidth() / 2) {
			iv.setLayoutX(iv.getLayoutX() - 1);
		}
		else if (iv.getBoundsInParent().getMaxX() < 
		piv.getBoundsInParent().getMinX() + piv.getBoundsInParent().getWidth() / 2) {
			iv.setLayoutX(iv.getLayoutX() + 1);
		}
	}
	
	private void checkY(Player p) {
		ImageView iv = getImageView();
		ImageView piv = p.getImageView();
		if (iv.getBoundsInParent().getMinY() > 
		piv.getBoundsInParent().getMinY() + piv.getBoundsInParent().getHeight() / 2) {
			iv.setLayoutY(iv.getLayoutY() - 1);
		}
		else if (iv.getBoundsInParent().getMaxY() < 
		piv.getBoundsInParent().getMinY() + piv.getBoundsInParent().getHeight() / 2) {
			iv.setLayoutY(iv.getLayoutY() + 1);
		}
	}
	
	private void checkCollect(Player p, Game g) {
		ImageView iv = getImageView();
		ImageView piv = p.getImageView();
		if (iv.getBoundsInParent().intersects(piv.getBoundsInParent())) {
			addGold(p, g);
			g.getEntities().getChildren().remove(getImageView());
			timeline.stop();
		}
	}
	
	public Image getImage() {
		return getImageView().getImage();
	}

	public static Image getCoin() {
		return coin;
	}

	public static void setCoin(Image coin) {
		Coin.coin = coin;
	}
}

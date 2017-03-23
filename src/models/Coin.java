package models;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Coin extends Entity {

	private static Image dirtyCoin = new Image("file:pictures/DirtyCoin.png");
	public static Image coin = new Image("file:pictures/Coin.png");
	private static Random rand = new Random();
	
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

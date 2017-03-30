package models;

import interfaces.Shootable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RangedEnemy extends Enemy implements Shootable {
	
	private static Image image = new Image("file:pictures/RangedEnemy.png");
	private static int minGold = 15;
	private static int maxGold = 25;
	
	public RangedEnemy() {
		setSpeed(2);
		setImageView(new ImageView(image));
		setGoldValue(getRand().nextInt(maxGold - minGold + 1) + minGold);
	}

	@Override
	public void RecieveDamage(Entity attacker) {
		playDeathAnimation();
		attacker.setGold(attacker.getGold() + getGoldValue());
	}

	@Override
	public Bullet Shoot() {
		Bullet b = new Bullet(false, getImageView().getLayoutX() - 32,
				getImageView().getLayoutY() +
				getImageView().getBoundsInParent().getHeight() / 2);
		b.setMoving(true);
		//System.out.println(b.getImageView().getLayoutX() + ";" + b.getImageView().getLayoutY());
		
		return b;
	}

	@Override
	void playDeathAnimation() {
		
	}
}

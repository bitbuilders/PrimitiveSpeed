package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MeleeEnemy extends Enemy {
	
	private static Image image = new Image("file:pictures/MeleeEnemy.png");
	private static int minGold = 10;
	private static int maxGold = 20;

	public MeleeEnemy() {
		setSpeed(5);
		setImageView(new ImageView(image));
		setGoldValue(getRand().nextInt(maxGold - minGold + 1) + minGold);
	}

	@Override
	public void RecieveDamage(Entity attacker) {
		playDeathAnimation();
		attacker.setGold(attacker.getGold() + getGoldValue());
	}

	@Override
	void playDeathAnimation() {
		
	}
}

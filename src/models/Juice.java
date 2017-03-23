package models;

import interfaces.Consumable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Juice implements Consumable {

	private Timeline timeline;
	
	@Override
	public void use(Player p) {
		p.setSpeed(p.getSpeed() + 2);
		if (p.getJumpHeight() == p.getStartingHeight()) {
			p.setStartingHeight(p.getStartingHeight() + 2);
			p.setJumpHeight(p.getStartingHeight());
		}
		else {
			p.setStartingHeight(p.getStartingHeight() + 2);
		}
		timeline = new Timeline(new KeyFrame(Duration.millis(6000), ae -> timerTick(p)));
		timeline.play();
	}
	
	private void timerTick(Player p) {
		p.setSpeed(p.getSpeed() - 2);
		if (p.getJumpHeight() == p.getStartingHeight()) {
			p.setStartingHeight(p.getStartingHeight() - 2);
			p.setJumpHeight(p.getStartingHeight());
		}
		else {
			p.setStartingHeight(p.getStartingHeight() - 2);
		}
	}
}

package models;

import java.util.Random;

import interfaces.Damageable;

public abstract class Enemy extends Entity implements Damageable {

	private int goldValue;
	private Random rand;
	
	abstract void playDeathAnimation();
	
	public Enemy() {
		setRand(new Random());
	}

	public Random getRand() {
		return rand;
	}
	
	public void setRand(Random rand) {
		this.rand = rand;
	}

	public int getGoldValue() {
		return goldValue;
	}

	public void setGoldValue(int goldValue) {
		this.goldValue = goldValue;
	}
}

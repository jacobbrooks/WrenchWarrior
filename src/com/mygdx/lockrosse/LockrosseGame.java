package com.mygdx.lockrosse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LockrosseGame extends Game {
	
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(batch, this));
	}

	@Override
	public void render () {
		super.render();
	}
	
}

package com.mygdx.lockrosse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen{
	
	private Texture img;
	private SpriteBatch sb;
	private LockrosseGame game;
	private int result;
	
	public GameOverScreen(LockrosseGame game, SpriteBatch sb, int result) {
		this.game = game;
		this.sb = sb;
		this.result = result;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		if(result == 0) {
			img = new Texture("youlost.png");
		}else {
			img = new Texture("youwon.png");
		}
		
		sb = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		sb.begin();
		sb.draw(img, 0, 0);
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			game.setScreen(new GameScreen(sb, game));
		}
		sb.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

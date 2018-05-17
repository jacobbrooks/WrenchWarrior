package com.mygdx.lockrosse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameScreen implements Screen {

	private LockrosseGame game;

	private World world;
	private Box2DDebugRenderer debugRenderer;

	private SpriteBatch sb;

	private Wrench player;
	private Wrench player2;
	private Wrench player3;

	private Wrench opponent;
	private Wrench opponent2;
	private Wrench opponent3;

	private Texture bg;

	private OrthographicCamera camera;

	private int iterations;

	private Wrench[] players;

	private boolean playerLocking;

	public GameScreen(SpriteBatch sb, LockrosseGame game) {
		this.sb = sb;
		this.game = game;
	}

	@Override
	public void show() {
		iterations = 0;
		world = new World(new Vector2(0, 0f), true);
		debugRenderer = new Box2DDebugRenderer();

		player = new Wrench(world, 0);
		player2 = new Wrench(world, 0);
		player3 = new Wrench(world, 0);

		opponent = new Wrench(world, 1);
		opponent2 = new Wrench(world, 1);
		opponent3 = new Wrench(world, 1);

		player.setPosition(10, 100);
		player2.setPosition(300, 100);
		player3.setPosition(500, 100);
		opponent.setPosition(10, 400);
		opponent2.setPosition(300, 400);
		opponent3.setPosition(500, 400);

		players = new Wrench[6];
		players[0] = player;
		players[1] = player2;
		players[2] = player3;
		players[3] = opponent;
		players[4] = opponent2;
		players[5] = opponent3;

		bg = new Texture("gameBackground.png");
		WorldBounds worldBounds = new WorldBounds(world);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		playerLocking = false;
	}

	@Override
	public void render(float delta) {
		world.step(1 / 60f, 8, 3);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(bg, 0, 0);

		for(int i = 0; i < 6; i++) {
			players[i].render(sb, iterations);
		}
		
		if(!opponent.opponentDocked(player)) {
			if(!playerLocking) {
				player.move();
			}
			opponent.seek(player.getBody().getPosition());
			opponent.rotateToAngle(player.getBody().getPosition());
		}else {
			opponent.lock(player);
			opponent.seekGoal();
			if(inGoal(opponent)) {
				game.setScreen(new GameOverScreen(game, sb, 0));
			}
		}
	
		if(!(player.docked(opponent2) && Gdx.input.isKeyPressed(Keys.SPACE))) {
			playerLocking = false;
			if(!opponent2.opponentDocked(player2)) {
				player2.randomMove();
				opponent2.seek(player2.getBody().getPosition());
				opponent2.rotateToAngle(player2.getBody().getPosition());
			}else {
				opponent2.lock(player2);
				opponent2.seekGoal();
				if(inGoal(opponent2)) {
					game.setScreen(new GameOverScreen(game, sb, 0));
				}
			}
		}else {
			player.lock(opponent2);
			player.lockingMove();
			playerLocking = true;
			if(inGoal(player)) {
				game.setScreen(new GameOverScreen(game, sb, 1));
			}
		}
		
		if(!(player.docked(opponent3) && Gdx.input.isKeyPressed(Keys.SPACE))) {
			playerLocking = false;
			if(!opponent3.opponentDocked(player3)) {
				player3.randomMove();
				opponent3.seek(player3.getBody().getPosition());
				opponent3.rotateToAngle(player3.getBody().getPosition());
			}else {
				opponent3.lock(player3);
				opponent3.seekGoal();
				if(inGoal(opponent3)) {
					game.setScreen(new GameOverScreen(game, sb, 0));
				}
			}
		}else {
			player.lock(opponent3);
			player.lockingMove();
			playerLocking = true;
			if(inGoal(player)) {
				game.setScreen(new GameOverScreen(game, sb, 1));
			}
		}
		

		sb.end();

		iterations++;

		// debugRenderer.render(world, camera.combined);
	}
	
	private boolean inGoal(Wrench w) {
		int goalWidth = 240;
		int goalHeight = 90;
		float posX = w.getBody().getPosition().x;
		float posY = w.getBody().getPosition().y;
		if(w.getTeam() == 1) {
			if(posX > (300 - goalWidth / 2)
					&& posX < (300 + goalWidth / 2)
					&& posY > 0 && posY < goalHeight) {
				return true; 
			}
		}else if(posX > (300 - goalWidth / 2)
				&& posX < (300 + goalWidth / 2)
				&& posY < 600 && posY > (600 - goalHeight)) {
			return true; 
		}
		return false;
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
	
	//240 * 90 goal

}

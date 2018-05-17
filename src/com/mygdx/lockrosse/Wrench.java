package com.mygdx.lockrosse;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class Wrench {

	private Sprite sprite;
	private Sprite teamLight;

	private Texture img;
	private Texture teamLightImg;

	private BodyEditorLoader loader;
	private Body body;
	private MassData massData;

	private float width;
	private float height;

	private World world;

	private int team;

	private Random r;

	private Vector2 goalVector;

	private int ranX;
	private int ranY;
	private Vector2 ranTarget;


	public Wrench(World world, int team) {
		img = new Texture("lockrosseplayer.png");

		if (team == 0) {
			teamLightImg = new Texture("blue.png");
			goalVector = new Vector2(300f, 600f);
		} else {
			teamLightImg = new Texture("purple.png");
			goalVector = new Vector2(300f, 0f);
		}

		sprite = new Sprite(img);
		teamLight = new Sprite(teamLightImg);

		this.team = team;

		width = 70;
		height = 42;

		this.world = world;
		loader = new BodyEditorLoader(Gdx.files.internal("WrenchBody"));

		initializeBody();
		initializeSprites();

	}

	private void initializeBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 0);
		bodyDef.type = BodyType.DynamicBody;
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1;
		body = world.createBody(bodyDef);
		loader.attachFixture(body, "Wrench", fixtureDef, 70);

		PolygonShape gSpot = new PolygonShape();
		gSpot.setAsBox(1, 1, new Vector2(0, 0), 0f);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = gSpot;

		Vector2[] vertices = new Vector2[gSpot.getVertexCount()];
		for (int i = 0; i < gSpot.getVertexCount(); i++) {
			vertices[i] = new Vector2();
			gSpot.getVertex(i, vertices[i]);
		}

		for (int i = 0; i < vertices.length; i++) {
			vertices[i].x += 16;
			vertices[i].y += 20;
		}

		gSpot.set(vertices);

		body.createFixture(gSpot, 1);

		massData = body.getMassData();
		massData.center.set(35, 20);
		massData.mass = 5;
		body.setMassData(massData);

		r = new Random();
		ranX = r.nextInt(600);
		ranY = r.nextInt(600);
		ranTarget = new Vector2(ranX, ranY);

	}

	private void initializeSprites() {
		sprite.setSize(width, height);
		sprite.setOrigin(body.getPosition().x, body.getPosition().y);

		teamLight.setSize(20, 15);
		teamLight.setOrigin(body.getPosition().x - 18, body.getPosition().y - 13);
	}

	public void render(SpriteBatch sb, int iterations) {
		sprite.setPosition((body.getPosition().x - sprite.getWidth() / 2) + 35,
				(body.getPosition().y - sprite.getHeight() / 2) + 20);
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

		teamLight.setPosition(body.getPosition().x + 18, body.getPosition().y + 13);
		teamLight.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

		sprite.draw(sb);

		if (iterations % 50 < 10) {
			teamLight.draw(sb);
		}
	}

	public void seekGoal() {
		seek(goalVector);
	}

	public float distanceToTarget(Vector2 targetVector) {
		float dist = body.getPosition().dst(targetVector);
		return dist;
	}

	public boolean docked(Wrench w) {
		Array<Contact> contacts = world.getContactList();
		for (int i = 0; i < contacts.size; i++) {
			if (contacts.get(i).getFixtureB().equals(w.getBody().getFixtureList().get(8))
					&& contacts.get(i).getFixtureA().getBody() == body && w.getTeam() != team) {
				return true;
			}
		}
		return false;
	}

	public boolean opponentDocked(Wrench w) {
		Array<Contact> contacts = world.getContactList();
		for (int i = 0; i < contacts.size; i++) {
			if (contacts.get(i).getFixtureA().equals(w.getBody().getFixtureList().get(8))
					&& contacts.get(i).getFixtureB().getBody() == body && w.getTeam() != team) {
				return true;
			}
		}
		return false;
	}

	public void lock(Wrench w) {
		body.setAngularVelocity(0);
		w.getBody().setLinearVelocity(body.getLinearVelocity());
		w.getBody().setMassData(body.getMassData());
	}

	public void move() {
		float forceX = (float) Math.cos(body.getAngle());
		float forceY = (float) Math.sin(body.getAngle());
		Vector2 force = new Vector2(forceX, forceY);
		force.nor();
		force.scl(50);

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			body.setLinearVelocity(body.getLinearVelocity().add(force));
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			body.setLinearVelocity(body.getLinearVelocity().sub(force));
		} else {
			body.setLinearVelocity(0f, 0f);
		}

		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			body.setAngularVelocity(-2f);
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			body.setAngularVelocity(2f);
		} else {
			body.setAngularVelocity(0f);
		}
	}

	public void lockingMove() {
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			body.setLinearVelocity(0f, 200f);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			body.setLinearVelocity(0f, -200f);
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			body.setLinearVelocity(-200f, 0f);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			body.setLinearVelocity(200f, 0f);
		}
	}

	public void randomMove() {
		seek(ranTarget);
		//rotateToAngle(ranTarget);
		if (distanceToTarget(ranTarget) < 10) {
			ranY = r.nextInt(600);
			ranX = r.nextInt(600);
			ranTarget = new Vector2(ranX, ranY);
		}
	}

	public void seek(Vector2 target) {
		Vector2 targetCpy = new Vector2(target.x, target.y);
		Vector2 desired = targetCpy.sub(body.getPosition());
		desired.nor();
		desired.scl(95);
		Vector2 steer = desired.sub(body.getLinearVelocity());
		if (team == 1) {
			body.applyLinearImpulse(steer.scl(2), body.getWorldCenter(), true);
		} else {
			body.applyLinearImpulse(steer, body.getWorldCenter(), true);
		}
	}

	public void rotateToAngle(Vector2 target) {
		Vector2 targetCpy = new Vector2(target.x, target.y);
		Vector2 targetAngleVector = targetCpy.sub(body.getPosition());
		float newAngle = (float) (Math.atan2(-targetAngleVector.x, targetAngleVector.y));
		float nextAngle = (float) (body.getAngle() - (Math.PI / 2) + body.getAngularVelocity() / 60.0);
		float totalRotation = newAngle - nextAngle;
		while (totalRotation < Math.toRadians(-180))
			totalRotation += Math.toRadians(360);
		while (totalRotation > Math.toRadians(180))
			totalRotation -= Math.toRadians(360);
		float desiredAngularVelocity = totalRotation * 60;
		float torque = (float) (body.getInertia() * desiredAngularVelocity / (1 / 60.0));
		body.applyTorque(torque, true);
	}

	public Sprite getTeamLight() {
		return teamLight;
	}

	public void setCenter(MassData other) {
		massData.center.set(other.center.x, other.center.y);
		body.setMassData(massData);
	}

	public void setProperCenter() {
		massData.center.set(35, 20);
		body.setMassData(massData);
	}

	public void setPosition(float x, float y) {
		body.setTransform(x, y, 0);
	}

	public Body getBody() {
		return body;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getTeam() {
		return team;
	}

	public Vector2 getGoalVector() {
		return goalVector;
	}

}

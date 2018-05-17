package com.mygdx.lockrosse;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ice {
	
	private Body body;
	private Sprite sprite;
	private Texture img;
	
	private World world;
	
	private float posX, posY;
	private float width, height;
	
	public Ice(World world) {
		this.world = world;
		
		img = new Texture("ice.png");
		sprite = new Sprite(img);
		
		width = 10;
		height = 30;
		
		initializeBody();
	}
	
	private void initializeBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 0);
		bodyDef.type = BodyType.DynamicBody;
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(2, 2);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = box;
		
		body = world.createBody(bodyDef);
		body.setBullet(true);
		
		body.createFixture(fixture);
	}
	
	public void render(SpriteBatch sb) {
		posX = body.getPosition().x;
		posY = body.getPosition().y;
		double rotation = Math.toDegrees(body.getAngle());
		sprite.setPosition(posX + 300, posY + 301);
		sprite.setRotation((float)rotation);
		sprite.setSize(width, height);
		sprite.draw(sb);
	}
	
	public void setPosition(float x, float y) {
		body.setTransform(x, y, 0);
	}
	
	public Body getBody() {
		return body;
	}

}

package com.mygdx.lockrosse;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class WorldBounds {
	
	private World world;
	
	public WorldBounds(World world) {
		this.world = world;
		generateBounds();
	}
	
	private void generateBounds() {
		BodyDef boundBodyDef = new BodyDef();
		boundBodyDef.position.set(0,0);
		boundBodyDef.type = BodyType.StaticBody;
		
		FixtureDef sideBoundFixtureDef = new FixtureDef();
		PolygonShape box = new PolygonShape();
		box.setAsBox(10, 610, new Vector2(5, 300), 0); 
		sideBoundFixtureDef.shape = box;
		
		Body rightBound = world.createBody(boundBodyDef);
		rightBound.createFixture(sideBoundFixtureDef);
		rightBound.setTransform(600, 0, 0);
		
		Body leftBound = world.createBody(boundBodyDef);
		leftBound.createFixture(sideBoundFixtureDef);
		leftBound.setTransform(-10, 0, 0);
		
		FixtureDef topAndBottomBoundFixtureDef = new FixtureDef();
		box.setAsBox(600, 10, new Vector2(300, 5), 0); 
		topAndBottomBoundFixtureDef.shape = box;
		
		Body topBound = world.createBody(boundBodyDef);
		topBound.createFixture(topAndBottomBoundFixtureDef);
		topBound.setTransform(0, 600, 0);
		
		
		Body bottomBound = world.createBody(boundBodyDef);
		bottomBound.createFixture(topAndBottomBoundFixtureDef);
		bottomBound.setTransform(0, -10, 0);
	}
}

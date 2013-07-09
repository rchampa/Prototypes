package com.me.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.me.mygdxgame.MiBodyEditorLoader;

public class GameObject{

	public Body model;
	private Vector2 modelOrigin;
    public MassData massData = new MassData();
    public Sprite sprite;
    public float density, friction, restitution, scale;
    public BodyType bodyType;
    public boolean isSensor;
    

    //FIXME add the possibility to work with more than one loader
    public GameObject(BodyType bodyType, float density, float friction, float restitution, float scale, boolean isSensor){
    	
    	this.bodyType = bodyType;
    	this.density = density;
    	this.friction = friction;
    	this.restitution = restitution;
		this.isSensor = isSensor;
    	this.scale = scale;
    }
    
    /**
     * This method set the Gameobject in the world. Use this method wisely.
     * 
     * @param loader
     * @param world
     * @return
     */
    public GameObject init(MiBodyEditorLoader loader, String name, World world){
    	
    	// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = bodyType;//BodyType.DynamicBody;

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		fd.isSensor = isSensor;

		// 3. Create a Body, as usual.
		model = world.createBody(bd);

		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(model, name, fd, this.scale);
		modelOrigin = loader.getOrigin(name, this.scale).cpy();
    	
		model.setMassData(massData);
		model.setUserData(this);
    	
    	return this;
    }
    
    public void render(SpriteBatch batch) {
    	
    }

  

}

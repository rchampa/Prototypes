package com.RotatingCanvasGames.Joints;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PhysicsManager {
	Body pivot;
	Body spike;
	BoxBodyBuilder bodyFactory;
	Joint joint;
	
	float accumulator;
	boolean isPaused;
	
	static final float BOX_STEP = 1 / 120f;
	static final float RENDER_STEP = 1 / 40f;
	static final int VELOCITY_ITER = 8;
	static final int POSITION_ITER = 3;
	
	World world;
	
	Box2DDebugRenderer debugRenderer;  
	Matrix4 debugMatrix;
	
	public PhysicsManager(OrthographicCamera cam){
		isPaused=false;
		world=new World(new Vector2(0,-20), true);
		
		debugMatrix=cam.combined.cpy();
		debugMatrix.scale(BoxBodyBuilder.BOX_TO_WORLD, BoxBodyBuilder.BOX_TO_WORLD, 1f);
		debugRenderer=new Box2DDebugRenderer();
		
		bodyFactory=new BoxBodyBuilder();
		pivot=bodyFactory.CreateCircleBody(world, BodyType.StaticBody, 200, 200, 30);
		spike=bodyFactory.CreateCircleBody(world, BodyType.DynamicBody, 200, 200, 20);
		MakeJoint();
		
	}
	
	
	void MakeJoint(){
		RevoluteJoint j=new RevoluteJoint(pivot,spike,false);
		j.SetAnchorA(BoxBodyBuilder.ConvertToBox(0), BoxBodyBuilder.ConvertToBox(0));
		j.SetAnchorB(BoxBodyBuilder.ConvertToBox(100), BoxBodyBuilder.ConvertToBox(0));
		//j.SetAngleLimit(60, 120);
		j.SetMotor(10, 360);
		joint=j.CreateJoint(world);
	}
	
	public void Update(float dt){
		if(!isPaused){
			accumulator+=dt;
			while(accumulator>dt){
				world.step(BOX_STEP,VELOCITY_ITER, POSITION_ITER);
				accumulator-=BOX_STEP;
			}
		}
		
	}
	
	public void Draw(SpriteBatch sp){
		sp.begin();
		debugRenderer.render(world, debugMatrix);
		sp.end();
	}
	
	public void Dispose(){
		//DISPOSE JOINTS FIRST
		debugRenderer.dispose();
		DisposeJoints();
		DisposeBodies();
		world.dispose();
	}

	private void DisposeBodies() {
		// TODO Auto-generated method stub
		while(world.getBodies().hasNext()){
			world.destroyBody(world.getBodies().next());
		}
	}

	private void DisposeJoints() {
		// TODO Auto-generated method stub
		while(world.getJoints().hasNext()){
			world.destroyJoint(world.getJoints().next());
		}
	}
}

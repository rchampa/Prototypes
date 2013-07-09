package sandbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Sandbox extends ApplicationAdapter{

	private World world;
	private Body model;
	private Vector2 modelOrigin;
	protected Box2DDebugRenderer debugRenderer;
	OrthographicCamera debugCam;
	
	public final float KG = 1000;
	
	@Override
	public void create() {
		
		world = new World(new Vector2(0, 0), false);
		debugRenderer = new Box2DDebugRenderer(true, true, true, true, true);
		debugCam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		debugCam.position.set(0f, 0f, 0f);
		debugCam.update();
		createBall();
		restart();
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override 
			public boolean touchDown(int x, int y, int pointer, int button) {
				Vector2 n = new Vector2(KG,1).mul(10000000f);
				//model.setLinearVelocity(100f, 100f);
				//model.applyForce(n, modelOrigin);
				//model.applyForceToCenter(n);//lo mismo que la anteior pero sin tener que pasar el punto de aplicacion
				//model.applyLinearImpulse(n, modelOrigin);
				//model.applyAngularImpulse(100000000f);//No hace nada >.<
				//model.applyTorque(100f);//no hace nada :(
				
				
				
				return true;
			}
			
			@Override
			public boolean scrolled (int amount) {
				//
				return true;
			}
		});
	}
	
	private void restart() {
		
		model.setTransform(0, 0, 0);
		//model.setAngularVelocity(-5f);
	}
	
	private void createBall() {
		BodyDef ballBodyDef = new BodyDef();
		ballBodyDef.type = BodyType.DynamicBody;

		CircleShape shape = new CircleShape();
		shape.setRadius(10f);

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 1f;
		fd.restitution = 0.5f;
		fd.shape = shape;
		
		model = world.createBody(ballBodyDef);
		model.createFixture(fd);
		
		/*
		 * Asignando masa
		 */
		MassData m = new MassData();
		m.mass = KG;
		model.setMassData(m);
		
		
		modelOrigin = new Vector2(0f,0f);//No sé como calcular el puto origen, pero sé que es 0,0

		shape.dispose();
		
	}
	
	
	@Override
	public void render() {

		world.step(1f/60f, 10, 10);
		
		Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 0 ); 
		Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		debugRenderer.render(world, debugCam.combined);
	}
}

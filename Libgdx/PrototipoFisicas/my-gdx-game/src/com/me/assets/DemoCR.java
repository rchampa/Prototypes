package com.me.assets;

import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.me.mygdxgame.MiBodyEditorLoader;

public class DemoCR extends ApplicationAdapter {
	
		// -------------------------------------------------------------------------
		// Static fields
		// -------------------------------------------------------------------------
	
		private final short CATEGORY_PLANET = 0x0001;  // 0000000000000001 in binary
		private final short CATEGORY_CORE_PLANET = 0x0002; // 0000000000000010 in binary
		private final short CATEGORY_BALLS = 0x0004; // 0000000000000100 in binary
			
		private final short MASK_PLANET = CATEGORY_BALLS; 
		private final short MASK_CORE_PLANET = CATEGORY_BALLS;
		private final short MASK_BALLS = CATEGORY_PLANET;
	
		private float WIDTH_DEVICE;
		private float HEIGHT_DEVICE;
		private float WIDTH_WORLD;
		private float HEIGHT_WORLD;
		private float ASPECT_RATIO;
	
		private final float VIEWPORT_HEIGHT = 40;
		private final float PLANET_WIDTH = 12;
		private final float PLANET_CORE_WIDTH = 12;
		
		private static final float BALL_RADIUS = 0.7f;
		private static final int MAX_BALLS = 50;

		// Models
		private World world;
		private Body planetModel;
		private Vector2 planetModelOrigin;
		private Body planetCoreModel;
		private Vector2 planetCoreModelOrigin;
		private Body[] ballModels;
		
		// Render
		private Texture planetTexture;
		private Sprite planetSprite;
		private Texture planetCoreTexture;
		private Sprite planetCoreSprite;
		private Texture backgroundTexture;
		private Sprite backgroundSprite;
		private Texture ballTexture;
		private Sprite[] ballSprites;
		
		
		// Render general
		private SpriteBatch batch;
		private BitmapFont font;
		private OrthographicCamera camera;

		// Misc
		private final TweenManager tweenManager = new TweenManager();
		private final Random rand = new Random();
		private MiBodyEditorLoader loader;


		@Override
		public void create() {
			
			WIDTH_DEVICE = Gdx.graphics.getWidth();
			HEIGHT_DEVICE = Gdx.graphics.getHeight();

			ASPECT_RATIO = WIDTH_DEVICE/HEIGHT_DEVICE;
			
			WIDTH_WORLD = VIEWPORT_HEIGHT*ASPECT_RATIO;
			HEIGHT_WORLD = VIEWPORT_HEIGHT;
			
			
			// Models initialization

			//In the space there is not gravity :D 
			int x_gravity= 0;
			int y_gravity= 0;
			
			boolean improve_performance_lowering_simulating_of_bodies= false;
			
			world = new World(new Vector2(x_gravity, y_gravity), improve_performance_lowering_simulating_of_bodies);
			
			
			/// Create a loader for the file saved from the editor. READ ONCE
			loader = new MiBodyEditorLoader(Gdx.files.internal("data/cr/world.json"));
			
			createPlanet();
			createPlanetCore();
			createBalls();

			// Render initialization

			batch = new SpriteBatch();
			font = new BitmapFont();
			font.setColor(Color.BLACK);

			
			camera = new OrthographicCamera();
			camera.setToOrtho(false, WIDTH_WORLD, HEIGHT_WORLD);//setting aspect ratio and y-axis pointing to up
			camera.position.set(0, camera.viewportHeight/2, 0);
			camera.update();

			createSprites();

			// Input initialization

			Gdx.input.setInputProcessor(new InputAdapter() {
				@Override public boolean touchDown(int x, int y, int pointer, int button) {
					restart();
					return true;
				}
			});

			// Run
			restart();
			
		}
		

		private void createBalls() {
			BodyDef ballBodyDef = new BodyDef();
			ballBodyDef.type = BodyType.DynamicBody;

			CircleShape shape = new CircleShape();
			shape.setRadius(BALL_RADIUS);

			FixtureDef fd = new FixtureDef();
			fd.density = 1;
			fd.friction = 0.5f;
			fd.friction = 1f;
			fd.restitution = 0.5f;
			fd.shape = shape;
			fd.filter.categoryBits = CATEGORY_BALLS;
			fd.filter.maskBits = MASK_BALLS;

			ballModels = new Body[MAX_BALLS];
			for (int i=0; i<MAX_BALLS; i++) {
				ballModels[i] = world.createBody(ballBodyDef);
				ballModels[i].createFixture(fd);
			}

			shape.dispose();
			
		}


		private void createPlanetCore() {
			
			// 1. Create a BodyDef, as usual.
			BodyDef bd = new BodyDef();
			//bd.type = BodyType.KinematicBody;
			bd.type = BodyType.DynamicBody;
			//bd.active = false;//This means the body will not participate in collisions, ray casts, etc.

			// 2. Create a FixtureDef, as usual.
			FixtureDef fd = new FixtureDef();
			//A kinematic body behaves as if it has infinite mass fd.density = 1000000;
			fd.friction = 0.5f;
			fd.restitution = 0.3f;
			fd.filter.categoryBits = CATEGORY_CORE_PLANET;
			fd.filter.maskBits = MASK_CORE_PLANET;

			// 3. Create a Body, as usual.
			planetCoreModel = world.createBody(bd);

			// 4. Create the body fixture automatically by using the loader.
			loader.attachFixture(planetCoreModel, "planet_core", fd, PLANET_CORE_WIDTH);
			planetCoreModelOrigin = loader.getOrigin("planet_core", PLANET_CORE_WIDTH).cpy();
			
		}


		private void createPlanet() {
	
			// 1. Create a BodyDef, as usual.
			BodyDef bd = new BodyDef();
			//bd.type = BodyType.KinematicBody;
			bd.type = BodyType.KinematicBody;

			// 2. Create a FixtureDef, as usual.
			FixtureDef fd = new FixtureDef();
			//A kinematic body behaves as if it has infinite mass 
			fd.friction = 0.5f;
			fd.restitution = 0.3f;
			fd.filter.categoryBits = CATEGORY_PLANET;
			fd.filter.maskBits = MASK_PLANET;

			// 3. Create a Body, as usual.
			planetModel = world.createBody(bd);

			// 4. Create the body fixture automatically by using the loader.
			loader.attachFixture(planetModel, "planet", fd, PLANET_WIDTH);
			planetModelOrigin = loader.getOrigin("planet", PLANET_WIDTH).cpy();
		}
		
		private void createSprites() {
			
			//background
			backgroundTexture = new Texture(Gdx.files.internal("data/cr/map1bg.png"));
			backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			backgroundSprite = new Sprite(backgroundTexture);
			backgroundSprite.setSize(WIDTH_WORLD, HEIGHT_WORLD);
			backgroundSprite.setPosition(-WIDTH_WORLD/2f, 0);
			
			//planet
			planetTexture = new Texture(Gdx.files.internal("data/cr/planet_terrain.png"));
			planetTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			planetSprite = new Sprite(planetTexture);
			planetSprite.setSize(PLANET_WIDTH, PLANET_WIDTH*planetSprite.getHeight()/planetSprite.getWidth());
			
			
			//planet core
			planetCoreTexture = new Texture(Gdx.files.internal("data/cr/planet_core.png"));
			planetCoreTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			planetCoreSprite = new Sprite(planetCoreTexture);
			planetCoreSprite.setSize(PLANET_CORE_WIDTH, PLANET_CORE_WIDTH*planetCoreSprite.getHeight()/planetCoreSprite.getWidth());
			
			
			//balls
			ballTexture = new Texture(Gdx.files.internal("data/gfx/ball.png"));
			ballTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			ballSprites = new Sprite[MAX_BALLS];
			for (int i=0; i<MAX_BALLS; i++) {
				ballSprites[i] = new Sprite(ballTexture);
				ballSprites[i].setSize(BALL_RADIUS*2, BALL_RADIUS*2);
				ballSprites[i].setOrigin(BALL_RADIUS, BALL_RADIUS);
			}
			
		}
		
		@Override
		public void dispose() {
			
			planetTexture.dispose();
			backgroundTexture.dispose();
			planetCoreTexture.dispose();
			ballTexture.dispose();
			batch.dispose();
			font.dispose();
			
			//disposing bodies
			world.destroyBody(planetCoreModel);
			world.destroyBody(planetModel);
			for (int i=0; i<MAX_BALLS; i++)	world.destroyBody(ballModels[i]);
			world.dispose();
			
		}
		
		@Override
		public void render() {

			updatePhysics();
			//System.out.println("render");
			
			Gdx.graphics.getGL20().glClearColor( 1, 1, 1, 1 ); // White color
			Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			backgroundSprite.draw(batch);
			planetCoreSprite.draw(batch);
			planetSprite.draw(batch);
			for (int i=0; i<MAX_BALLS; i++) ballSprites[i].draw(batch);
			batch.end();

			batch.getProjectionMatrix().setToOrtho2D(0, 0, WIDTH_DEVICE, HEIGHT_DEVICE);
			batch.begin();
			font.draw(batch, "Touch the screen to restart", 5, HEIGHT_DEVICE-5);
			batch.end();
			
		}
		
		private void updatePhysics(){
			// Update
			//float deltaTime = Gdx.graphics.getRawDeltaTime();
			float deltaTime = 1/60f;
			//System.out.println(deltaTime);
			tweenManager.update(deltaTime);//Setting delta time
			world.step(deltaTime, 10, 10);

			Vector2 planetPos = planetModel.getPosition().sub(planetModelOrigin);
			planetSprite.setPosition(planetPos.x, planetPos.y);
			planetSprite.setOrigin(planetModelOrigin.x, planetModelOrigin.y);
			planetSprite.setRotation(planetModel.getAngle() * MathUtils.radiansToDegrees);
			
			Vector2 planetCorePos = planetCoreModel.getPosition().sub(planetCoreModelOrigin);
			planetCoreSprite.setPosition(planetCorePos.x, planetCorePos.y);
			planetCoreSprite.setOrigin(planetCoreModelOrigin.x, planetCoreModelOrigin.y);
			planetCoreSprite.setRotation(planetCoreModel.getAngle() * MathUtils.radiansToDegrees);
			
			for (int i=0; i<MAX_BALLS; i++) {
				Vector2 ballPos = ballModels[i].getPosition();
				ballSprites[i].setPosition(ballPos.x - ballSprites[i].getWidth()/2, ballPos.y - ballSprites[i].getHeight()/2);
				ballSprites[i].setRotation(ballModels[i].getAngle() * MathUtils.radiansToDegrees);
			}
		}
		
		private void restart() {
			
			float y = (HEIGHT_WORLD/2) -10;
			
			planetCoreModel.setTransform(0, y, 0);
			planetCoreModel.setLinearVelocity(0f, 0f);
			planetCoreModel.setAngularVelocity(1f);
			
			planetModel.setTransform(0, y, 0f);
			planetModel.setLinearVelocity(0f, 0f);
			planetModel.setAngularVelocity(1f);
			
			
			Vector2 vec = new Vector2();

			for (int i=0; i<MAX_BALLS; i++) {
				float tx = rand.nextFloat() * 1.0f - 1f;
				float ty = HEIGHT_WORLD+BALL_RADIUS;
				float angle = rand.nextFloat() * MathUtils.PI * 2;

				ballModels[i].setActive(false);
				ballModels[i].setLinearVelocity(vec.set(0, -10));
				ballModels[i].setAngularVelocity(0f);
				ballModels[i].setTransform(vec.set(tx, ty), angle);
			}

			tweenManager.killAll();

			Tween.call(new TweenCallback() {
				private int idx = 0;
				
				@Override
				public void onEvent(int arg0, BaseTween<?> arg1) {
					if (idx < ballModels.length) {
						ballModels[idx].setAwake(true);
						ballModels[idx].setActive(true);
						idx += 1;
					}
				}
			}).repeat(MAX_BALLS, 0.5f).start(tweenManager);
			
			
		}
		
		@Override
		public void pause () {
			//Gdx.graphics.setContinuousRendering(false);
			System.out.println("Pause");
		}
		
		@Override
		public void resize (int width, int height) {
			System.out.println("resize");
		}
		
		@Override
		public void resume () {
			//Gdx.graphics.setContinuousRendering(true);
			System.out.println("resume");
		}

}

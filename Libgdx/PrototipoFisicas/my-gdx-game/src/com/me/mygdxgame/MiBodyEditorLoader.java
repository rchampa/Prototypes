package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;
/**
 * 
 * @author Ricardo
 * This class is a body loader from a json file.
 *
 */
public class MiBodyEditorLoader {
	
	//Fields of JSON
	private static final String RIGID_BODIES = "rigidBodies";
	private static final String NAME = "name";
	private static final String IMAGE_PATH = "imagePath";
	private static final String ORIGIN = "origin";
	private static final String POLYGONS = "polygons";
	private static final String CIRCLES = "circles";
	private static final String SHAPES = "shapes";
	private static final String SHAPE_TYPE = "type";
	private static final String VERTEXES = "vertices";
	
	//Vector pool allows the Object pool pattern, see more in http://en.wikipedia.org/wiki/Object_pool_pattern
	private final List<Vector2> vectorPool = new ArrayList<Vector2>();
	
	private final Model model;
	private final PolygonShape polygonShape = new PolygonShape();
	private final CircleShape circleShape = new CircleShape();
	private final Vector2 vec = new Vector2();

	public MiBodyEditorLoader(FileHandle file) {
		if (file == null)
			throw new NullPointerException("file is null");
		this.model = readJson(file.readString());
	}

	public void attachFixture(Body body, String name, FixtureDef fd, float scale) {
		
		//Load the rigidModel by key
		RigidBodyModel rbModel = this.model.rigidBodies.get(name);
		
		if (rbModel == null)
			throw new RuntimeException("Name '" + name + "' was not found.");
	
		
		Vector2 origin = this.vec.set(rbModel.origin).mul(scale);
		
		//Loading polygons
		Vector2[] vertexes;
		PolygonModel polygon;
		
		for (int i = rbModel.polygons.size()-1; 0 <= i; i--) {
			polygon = rbModel.polygons.get(i);
			vertexes = polygon.vectorBuffer;

			//Loading vertexes (scaled) from polygon
			for (int ii = vertexes.length-1; 0 <= ii; ii--) {
				vertexes[ii] = newVec().set(polygon.vertices.get(ii)).mul(scale);
				vertexes[ii].sub(origin);
			}

			//sets vertexs to polygon
			this.polygonShape.set(vertexes);
			fd.shape = this.polygonShape;
			body.createFixture(fd);
			System.out.println("Polygon fixture added.");

			//Send vertexes to the object pool
			for (int ii = vertexes.length-1;  0<=ii; ii--) {
				free(vertexes[ii]);
			}
		}

		//Loading circles
		CircleModel circle;
		Vector2 center;
		float radius;
		
		for (int i = rbModel.circles.size()-1; 0 <= i; i--) {
			circle = rbModel.circles.get(i);
			center = newVec().set(circle.center).mul(scale);
			center.sub(origin);
			radius = circle.radius * scale;

			this.circleShape.setPosition(center);
			this.circleShape.setRadius(radius);
			fd.shape = this.circleShape;
			body.createFixture(fd);
			System.out.println("Circle fixture added.");

			free(center);
		}
		
	}


	public Vector2 getOrigin(String name, float scale) {
		RigidBodyModel rbModel = (RigidBodyModel) this.model.rigidBodies.get(name);
		
		if (rbModel == null)
			throw new RuntimeException("Name '" + name + "' was not found.");

		return this.vec.set(rbModel.origin).mul(scale);
	}

	private Model readJson(String str) {
		Model m = new Model();
		OrderedMap<String, ?> rootElem = (OrderedMap) new JsonReader().parse(str);

		Array bodiesElems = (Array) rootElem.get(RIGID_BODIES);
		
		OrderedMap<String, ?> bodyElem;
		RigidBodyModel rbModel;

		for (int i = bodiesElems.size-1; 0 <=i; i--) {
			bodyElem = (OrderedMap) bodiesElems.get(i);
			rbModel = readRigidBody(bodyElem);
			m.rigidBodies.put(rbModel.name, rbModel);
		}

		return m;
	}

	/**
	 * Load all rigid models.
	 * @param bodyElem
	 * @return
	 */
	private RigidBodyModel readRigidBody(OrderedMap<String, ?> bodyElem) {
		RigidBodyModel rbModel = new RigidBodyModel();
		rbModel.name = ((String) bodyElem.get(NAME));
		rbModel.imagePath = ((String) bodyElem.get(IMAGE_PATH));

		OrderedMap<String, Float> originElem = (OrderedMap) bodyElem.get(ORIGIN);
		rbModel.origin.x = (originElem.get("x")).floatValue();
		rbModel.origin.y = (originElem.get("y")).floatValue();

		Array polygonsElem = (Array) bodyElem.get(POLYGONS);

		for (int i = polygonsElem.size-1; 0<=i ; i--) {
		
			PolygonModel polygon = new PolygonModel();
			rbModel.polygons.add(polygon);//add to polygon list

			Array<OrderedMap<String, Float>> verticesElem = (Array<OrderedMap<String, Float>>) polygonsElem.get(i);
			for (int ii = 0; ii < verticesElem.size; ii++) {
				OrderedMap<String, Float> vertexElem = verticesElem.get(ii);
				float x = (vertexElem.get("x")).floatValue();
				float y = (vertexElem.get("y")).floatValue();
				polygon.vertices.add(new Vector2(x, y));
			}

			polygon.vectorBuffer = new Vector2[polygon.vertices.size()];
		}

		Array circlesElem = (Array) bodyElem.get(CIRCLES);

		for (int i = circlesElem.size-1; 0<=i ; i--) {
			CircleModel circle = new CircleModel();
			rbModel.circles.add(circle);//add to circle list

			OrderedMap<String, Float> circleElem = (OrderedMap<String, Float>) circlesElem.get(i);
			circle.center.x = (circleElem.get("cx")).floatValue();
			circle.center.y = (circleElem.get("cy")).floatValue();
			circle.radius = (circleElem.get("r")).floatValue();
		}

		return rbModel;
	}

	/**
	 * Re-using vectors on demand
	 * @return
	 */
	private Vector2 newVec() {
		return this.vectorPool.isEmpty() ? new Vector2()
				: (Vector2) this.vectorPool.remove(0);
	}

	/**
	 * Adds a vector which is no longer used
	 * @param v vector ready to re-use
	 */
	public void free(Vector2 v) {
		this.vectorPool.add(v);
	}

	public static class CircleModel {
		public final Vector2 center = new Vector2();
		public float radius;
	}

	public static class PolygonModel {
		public final List<Vector2> vertices = new ArrayList<Vector2>();
		private Vector2[] vectorBuffer;
	}

	public static class RigidBodyModel {
		public String name;
		public String imagePath;
		public final Vector2 origin = new Vector2();
		public final List<PolygonModel> polygons = new ArrayList<PolygonModel>();
		public final List<CircleModel> circles = new ArrayList<CircleModel>();
	}

	public static class Model {
		public final Map<String, RigidBodyModel> rigidBodies = new HashMap<String, RigidBodyModel>();
	}
}
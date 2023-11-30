package map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import entity.Camera;
import entity.GameObject;
import entity.GameObjectType;
import helper.Collision;

/**
 * ObjectMap class represents a map that contains
 * GameObjects on its map
 * @author Vachia Thoj
 *
 */
public class ObjectMap implements Serializable
{
	//For serialization
	private static final long serialVersionUID = 1L;
	
	//Camera
	private transient Camera camera;
	
	//ArrayList of GameObjects
	private ArrayList<GameObject> gameObjects;
	
	//BufferedImages
	private transient BufferedImage[] treeImages;
	private transient BufferedImage[] bushImages;
	private transient BufferedImage[] rockImages;
	private transient BufferedImage[] structureImages;
	private transient BufferedImage[] buildingImages;
	private transient BufferedImage[] miscImages;
	
	/**
	 * Constructor
	 */
	public ObjectMap()
	{		
		this.gameObjects = new ArrayList<GameObject>();
	}
	
	public void init()
	{
		for(int i = 0; i < gameObjects.size(); i++)
		{
			gameObjects.get(i).setCamera(camera);
		}
	}
	
	//Getter methods
	public ArrayList<GameObject> getGameObjects() {return gameObjects;}
	
	//Setter methods
	public void setCamera(Camera camera) {this.camera = camera;}
	public void setTreeImages(BufferedImage[] treeImages) {this.treeImages = treeImages;}
	public void setBushImages(BufferedImage[] bushImages) {this.bushImages = bushImages;}
	public void setRockImages(BufferedImage[] rockImages) {this.rockImages = rockImages;}
	public void setStructureImages(BufferedImage[] structureImages) {this.structureImages = structureImages;}
	public void setBuildingImages(BufferedImage[] buildingImages) {this.buildingImages = buildingImages;}
	public void setMiscImages(BufferedImage[] miscImages) {this.miscImages = miscImages;}
	
	/**
	 * Method that adds a GameObject to the ObjectMap
	 * @param id (int) The id of the GameObject
	 * @param x (int) The x-coordinate of the GameObject
	 * @param y (int) The y-coordinate of the GameObject
	 * @param width (int) The width of the GameObject
	 * @param height (int) The height of the GameObject
	 * @param gameObjectType (GameObjecType) The type of the GameObject 
	 */
	public void addGameObject(int id, int x, int y, int width, int height, GameObjectType gameObjectType)
	{
		//Create GameObject
		GameObject newGameObject = new GameObject(id, x, y, width, height, gameObjectType);
		newGameObject.setCamera(camera);
		newGameObject.update();
		
		if(gameObjectType == GameObjectType.NONE)
		{
			removeGameObject(newGameObject);
		}
		else
		{
			gameObjects.add(newGameObject);
		}
	}
	
	/**
	 * Methods that removes a GameObject from the ObjectMap
	 * @param newGameObject (GameObject)
	 */
	private void removeGameObject(GameObject newGameObject)
	{
		for(int i = 0; i < gameObjects.size(); i++)
		{
			if(Collision.aabbCollision(newGameObject, gameObjects.get(i)))
			{
				gameObjects.remove(gameObjects.get(i));
				return;
			}
		}
	}
	
	/**
	 * Method that updates the ObjectMap
	 */
	public void update()
	{
		for(int i = 0; i < gameObjects.size(); i++)
		{
			gameObjects.get(i).update();
		}
	}
	
	/**
	 * Method that draws the ObjectMap
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		for(int i = 0; i < gameObjects.size(); i++)
		{
			GameObject temp = gameObjects.get(i);
			
			//Only draw the GameObject if it is within the camera's viewpoint
			if(Collision.aabbCollision(temp, camera))
			{
				//Obtain the type of GameObject that the object is and draw that object
				switch(temp.getGameObjectType())
				{
					case TREE:
						g.drawImage(
								treeImages[temp.getId()],
								temp.getScreenX(),
								temp.getScreenY(),
								null
						);
						break;
					case BUSH:
						g.drawImage(
								bushImages[temp.getId()],
								temp.getScreenX(),
								temp.getScreenY(),
								null
						);
						break;
					case ROCK:
						g.drawImage(
								rockImages[temp.getId()],
								temp.getScreenX(),
								temp.getScreenY(),
								null
						);
						break;
					case STRUCTURE:
						g.drawImage(
								structureImages[temp.getId()],
								temp.getScreenX(),
								temp.getScreenY(),
								null
						);
						break;
					case BUILDING:
						g.drawImage(
								buildingImages[temp.getId()],
								temp.getScreenX(), 
								temp.getScreenY(),
								null
						);
						break;
					case MISC:
						g.drawImage(
								miscImages[temp.getId()], 
								temp.getScreenX(),
								temp.getScreenY(),
								null);
					default:
						break;
				}
			}
		}
	}
}
package entity;


/**
 * GameObject class represents an object within the game
 * @author Vachia Thoj
 *
 */
public class GameObject extends Entity
{
	//For serialization
	private static final long serialVersionUID = 1L;
	
	//Id
	private int id;
	
	//Camera
	private transient Camera camera;
	
	//The type of game object
	private GameObjectType gameObjectType;
	
	
	/**
	 * Constructor
	 * @param x (int) The x-coordinate of GameObject
	 * @param y (int) The y-coordinate of GameObject
	 * @param width (int) The width of the GameObject
	 * @param height (int) The height of the GameObject
	 */
	public GameObject(int id, int x, int y, int width, int height, GameObjectType gameObjectType)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gameObjectType = gameObjectType;
	}
	
	/**
	 * Constructor
	 * @param x (int) The x-coordinate of Thing
	 * @param y (int) The y-coordinate of Thing
	 * @param width (int) The width of the Thing
	 * @param height (int) The height of the Thing
	 * @param camera (Camera) A Camera object
	 */
	public GameObject(int id, int x, int y, int width, int height, GameObjectType gameObjectType, Camera camera)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gameObjectType = gameObjectType;
		this.camera = camera;
		
		//Adjusts GameObject's location relative to Camera
		this.screenX = -camera.getX() + x;
		this.screenY = -camera.getY() + y;
	}
	
	//Getter methods
	public int getId() {return id;}
	public GameObjectType getGameObjectType() {return gameObjectType;}
	
	//Setter methods
	public void setId(int id) {this.id = id;}
	public void setCamera(Camera camera) {this.camera = camera;}
	public void setGameObjecType(GameObjectType gameObjectType) {this.gameObjectType = gameObjectType;}
	
	/**
	 * Method that updates GameObject
	 */
	public void update()
	{
		//Adjusts GameObject's location relative to Camera
		screenX = -camera.getX() + x;
		screenY = -camera.getY() + y;
	}
}
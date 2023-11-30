package entity;

import manager.KeyManager;
import map.TileMap;

/**
 * Camera class represents a camera; to follow the screen's/window's viewpoint 
 * @author Vachia Thoj
 *
 */
public class Camera extends Entity
{
	private static final long serialVersionUID = 1L;
	
	//To manage key events
	private transient KeyManager keyManager;
	
	//Lowest x and y coordinate Camera can be at
	private transient int minX;
	private transient int minY;
	
	//Highest x and y coordinate Camera can be at
	private transient int maxX;
	private transient int maxY;
	
	//Camera speed
	private transient int speed;
	
	//For keyboard input
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;
	
	/**
	 * Constructor
	 * @param x (int) x-coordinate of Camera
	 * @param y (int) y-coordinate of Camera
	 * @param width (int) the width viewpoint of the camera
	 * @param height (int) the height viewpoint of the camera
	 * @param tileMap (TileMap) a tileMap for the Camera to follow
	 */
	public Camera(int x, int y, int width, int height, TileMap tileMap)
	{
		this.keyManager = KeyManager.instance();
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.minX = 0;
		this.minY = 0;
		
		this.maxX = tileMap.getWidth() - width;
		this.maxY = tileMap.getHeight() - height;
		
		this.speed = 2;
	}
	
	/**
	 * Method that moves the Camera
	 */
	private void move()
	{
		if(keyManager.isKeyDown(LEFT) == true) //Left key pressed
		{
			x = x - speed;
		}
		else if(keyManager.isKeyDown(RIGHT) == true) //Right key pressed
		{
			x = x + speed;
		}
		else if(keyManager.isKeyDown(UP) == true) //Up key pressed
		{
			y = y - speed;
		}
		else if(keyManager.isKeyDown(DOWN) == true) //Down key pressed
		{
			y = y + speed;
		}
	}
	
	/**
	 * Method that checks if Camera is at the boundaries
	 */
	private void checkBoundaries()
	{
		if(x < minX)
		{
			x = minX;
		}
		else if(x > maxX)
		{
			x = maxX;
		}
		
		if(y < minY)
		{
			y = minY;
		}
		else if(y > maxY)
		{
			y = maxY;
		}
	}
	
	/**
	 * Method that updates the Camera
	 */
	public void update()
	{
		move();
		checkBoundaries();
	}
}
package entity;

import manager.KeyManager;
import map.TileMap;

/**
 * Camera class represents a camera; to follow the screen's/window's viewpoint 
 * @author Vachia Thoj
 *
 */
public class Camera 
{
	//To manage key events
	private KeyManager keyManager;
	
	//x and y coordinate of Camera
	private int x;
	private int y;
	
	//Lowest x and y coordinate Camera can be at
	private int minX;
	private int minY;
	
	//Highest x and y coordinate Camera can be at
	private int maxX;
	private int maxY;
	
	//How much of the screen the Camera can see (viewpoint)
	private int width;
	private int height;
	
	//Camera speed
	private int speed;
	
	//TileMap
	private TileMap tileMap;
	
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
		this.tileMap = tileMap;
		
		this.minX = 0;
		this.minY = 0;
		
		this.maxX = tileMap.getWidth() - width;
		this.maxY = tileMap.getHeight() - height;
		
		this.speed = 8;
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
	
	//Getter Methods
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
		
	//Setter Methods
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setWidth(int width) {this.width = width;}
	public void setHeight(int height) {this.height = height;}
}

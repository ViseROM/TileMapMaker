package entity;

import java.io.Serializable;

/**
 * Abstract class that represents an Entity
 * @author Vachia Thoj
 *
 */
public abstract class Entity implements Serializable
{	
	//For serialization
	private static final long serialVersionUID = 1L;
	
	//x and y coordinate of Entity
	protected int x;
	protected int y;
	
	//x and y coordinate of Entity when on the screen
	protected int screenX;
	protected int screenY;
	
	//width and height of Entity;
	protected int width;
	protected int height;
	
	protected Entity()
	{
		
	}
	
	//Getter Methods
	public int getX() {return x;}
	public int getY() {return y;}
	public int getScreenX() {return screenX;}
	public int getScreenY() {return screenY;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	//Setter Methods
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setScreenX(int screenX) {this.screenX = screenX;}
	public void setScreenY(int screenY) {this.screenY = screenY;}
	public void setWidth(int width) {this.width = width;}
	public void setHeight(int height) {this.height = height;}
}
package menu;

import java.awt.Graphics2D;

import entity.Entity;

/**
 * Abstract class of a Menu
 * @author Vachia Thoj
 *
 */
public abstract class Menu extends Entity
{
	private static final long serialVersionUID = 1L;
	
	//Flag to see if Menu is visible
	protected transient boolean visible;
	
	protected Menu()
	{
		
	}
	
	//Getter methods
	public boolean isVisible() {return visible;}
	
	//Setter methods
	public void setVisible(boolean visible) {this.visible = visible;}
	
	//Abstract methods
	public abstract void update();
	public abstract void draw(Graphics2D g);
}
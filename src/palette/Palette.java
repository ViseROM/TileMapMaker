package palette;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;

/**
 * Abstract class of a Palette
 * @author Vachia Thoj
 *
 */
public abstract class Palette extends Entity
{
	private static final long serialVersionUID = 1L;
	
	//Color of Palette
	protected transient Color color;
	protected transient Color borderColor;
	protected transient boolean bordered;
	
	//Default color of Palette
	protected static final Color DEFAULT_COLOR = new Color(228, 207, 184);
	protected static final Color DEFAULT_BORDER_COLOR = new Color(132, 68, 40);
	
	/**
	 * Constructor
	 */
	protected Palette()
	{
		this.color = DEFAULT_COLOR;
		this.borderColor = DEFAULT_BORDER_COLOR;
		this.bordered = true;
	}
	
	//Getter methods
	public Color getColor() {return color;}
	public Color getBorderColor() {return borderColor;}
	public boolean isBordered() {return bordered;}
	
	//Setter methods
	public void setColor(Color color) {this.color = color;}
	public void setBorderColor(Color borderColor) {this.borderColor = borderColor;}
	public void setBordered(boolean b) {this.bordered = b;}
	
	/**
	 * Method that draws the background of the Palette
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	protected void drawPaletteBackground(Graphics2D g)
	{
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
	
	/**
	 * Method that draws the border of the Palette
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	protected void drawBorder(Graphics2D g)
	{
		if(bordered)
		{
			g.setColor(borderColor);
			g.drawRect(x, y, width, height);
		}
	}
	
	//Abstract methods
	public abstract void update();
	public abstract void draw(Graphics2D g);
}
package entity;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;

import manager.MouseManager;
import map.Tile;
import helper.Collision;

/**
 * Palette class represents a palette to be used for the
 * tile map maker
 * @author Vachia Thoj
 *
 */
public class Palette extends Entity
{
	//To manage mouse events
	private MouseManager mouseManager;
	
	//Images
	private BufferedImage[] images;
	private int numImages;
	
	//For BufferedImage[] images index
	private int start;
	private int end;
	private int currentImage;
	
	//Color of Palette
	private Color color;
	
	//Tiles
	private Tile tiles[];
	
	//Default color of Palette
	private static final Color DEFAULT_COLOR = new Color(100, 100, 100);
	
	/**
	 * Constructor
	 * @param x (int) x-coordinate of Palette
	 * @param y (int) y-coordinate of Palette
	 * @param width (int) width of the Palette
	 * @param height (int) height of the Palette
	 * @param images (BufferedImages[]) images within Palette
	 */
	public Palette(int x, int y, int width, int height, BufferedImage[] images)
	{
		mouseManager = MouseManager.instance();
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.images = images;
		this.numImages = images.length;
		
		this.start = 0;
		this.end = images.length;
		this.currentImage = 0;
		
		this.color = DEFAULT_COLOR;
		
		createTiles();
	}
	
	/**
	 * Method that "creates"/obtains tiles for the Palette
	 */
	private void createTiles()
	{
		//Initialize tiles
		tiles = new Tile[images.length];
		
		//Determine x and y coordinates; to be used for tiles
		int destX = x + 32;
		int destY = y + 96;
		
		//Create tiles
		for(int i = 0; i < images.length; i++)
		{
			tiles[i] = new Tile(destX, destY, images[i].getWidth(), images[i].getHeight(), i);
			
			destX = destX + images[i].getWidth() + 8;
		}
	}
	
	//Getter methods
	public BufferedImage[] getImages() {return images;}
	public int getNumImages() {return numImages;}
	public int getStart() {return start;}
	public int getEnd() {return end;}
	public int getCurrentImage() {return currentImage;}
	public Color getColor() {return color;}
	
	//Setter methods
	public void setImages(BufferedImage[] images) {this.images = images;}
	public void setStart(int start) {this.start = start;}
	public void setEnd(int end) {this.end = end;}
	public void setColor(Color color) {this.color = color;}
	
	/**
	 * Method that sets the range for tiles array
	 * @param start (int) start index for tiles array
	 * @param end (int) end index for tiles array
	 */
	public void setRange(int start, int end)
	{
		this.start = start;
		this.end = end;
		
		//Determine x and y coordinates for tiles
		int destX = tiles[0].getX() + tiles[0].getWidth() + 8;
		int destY = tiles[0].getY();
		
		for(int i = start; i <= end; i++)
		{
			tiles[i].setX(destX);
			tiles[i].setY(destY);
			
			destX = destX + tiles[i].getWidth() + 8;
		}
		
		currentImage = 0;
	}
	
	/**
	 * Method that updates the Palette
	 */
	public void update()
	{		
		int destX = tiles[0].getX() + tiles[0].getWidth() + 8;
		int destY = tiles[0].getY();
		
		for(int i = start; i <= end; i++)
		{
			tiles[i].setX(destX);
			tiles[i].setY(destY);
			
			destX = destX + tiles[i].getWidth() + 8;
		}
		
		//Check if mouse clicked on a tile
		if(mouseManager.getPressedPoint() != null && mouseManager.getReleasedPoint() != null)
		{
			//Obtain x and y coordinates where the mouse pressed and release
			int pressedX = mouseManager.getPressedPoint().getX();
			int pressedY = mouseManager.getPressedPoint().getY();
			int releasedX = mouseManager.getReleasedPoint().getX();
			int releasedY = mouseManager.getReleasedPoint().getY();
			
			//Check if mouse clicked on tile[0]
			if(Collision.pointEntityCollision(pressedX, pressedY, tiles[0]) &&
			   Collision.pointEntityCollision(releasedX, releasedY, tiles[0]))
			{
				//Set currentImage to 0
				currentImage = 0;
				mouseManager.clearPressedPoint();
				mouseManager.clearReleasedPoint();
				return;
			}
			
			for(int i = start; i <= end; i++)
			{
				//Check if mouse clicked on any of the tiles[]
				if(Collision.pointEntityCollision(pressedX, pressedY, tiles[i]) &&
				   Collision.pointEntityCollision(releasedX, releasedY, tiles[i]))
				{
					//Set currentImage to the image mouse clicked on
					currentImage = i;
					mouseManager.clearPressedPoint();
					mouseManager.clearReleasedPoint();
					break;
				}
			}
		}
	}
	
	/**
	 * Method that draws images within Palette
	 * @param g (Graphics2D) the Graphics2D object to be drawn on
	 */
	private void drawImages(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		
		g.drawImage(images[0], tiles[0].getX(), tiles[0].getY(), null);
		g.drawRect(tiles[0].getX(), tiles[0].getY(), tiles[0].getWidth(), tiles[0].getHeight());
		
		//Draw tile images
		for(int i = start; i <= end; i++)
		{
			g.drawImage(images[i], tiles[i].getX(), tiles[i].getY(), null);
			
			if(i != currentImage)
			{
				g.drawRect(tiles[i].getX(), tiles[i].getY(), tiles[i].getWidth(), tiles[i].getHeight());
			}
		}
		
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(2));
		g.drawRect(tiles[currentImage].getX(), tiles[currentImage].getY(), 
				   tiles[currentImage].getWidth(), tiles[currentImage].getHeight());
		g.setStroke(new BasicStroke(1));
	}
	
	/**
	 * Method that draws the currentImage
	 * @param g (Graphics2D) the Graphics2D object to be drawn on
	 */
	private void drawCurrentImage(Graphics2D g)
	{
		g.drawImage(images[currentImage], 896, y + 96, null);
		
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(2));
		g.drawRect(896, y + 96, images[currentImage].getWidth(), images[currentImage].getHeight());
		g.setStroke(new BasicStroke(1));
	}
	
	
	/**
	 * Method that draws the Palette
	 * @param g The Graphics2D object to be drawn on
	 */
	public void drawPalette(Graphics2D g)
	{
		//Draw Palette
		g.setColor(color);
		g.fillRect(x, y, width, height);
				
		//Draw border for Palette
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
	}
	
	/**
	 * Method that draws the Palette
	 * @param g (Graphics2D) the Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		//Draw Palette
		drawPalette(g);
		
		//Draw Images
		drawImages(g);
		
		//Draw currentImage
		drawCurrentImage(g);
	}
}

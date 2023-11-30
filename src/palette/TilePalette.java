package palette;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Tile;
import helper.Collision;
import main.GamePanel;
import manager.MouseManager;

/**
 * Palette class represents a palette to be used for the
 * tile map maker
 * @author Vachia Thoj
 *
 */
public class TilePalette extends Palette
{
	private static final long serialVersionUID = 1L;
	
	//To manage mouse events
	private transient MouseManager mouseManager;
	
	//Images
	private transient BufferedImage[] images;
	private transient int numImages;
	
	//For BufferedImage[] images index
	private transient int start;
	private transient int end;
	private transient int currentImage;
	
	//Tiles
	private transient Tile tiles[];
	
	/**
	 * Constructor
	 * @param x (int) x-coordinate of Palette
	 * @param y (int) y-coordinate of Palette
	 * @param width (int) width of the Palette
	 * @param height (int) height of the Palette
	 * @param images (BufferedImages[]) images within Palette
	 */
	public TilePalette(int x, int y, int width, int height, BufferedImage[] images)
	{
		super();
		
		this.mouseManager = MouseManager.instance();
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.images = images;
		this.numImages = images.length;
		
		this.start = 0;
		this.end = images.length;
		this.currentImage = 0;
		
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
		int destX = x + 16;
		int destY = y + 32;
		
		//Create tiles
		for(int i = 0; i < images.length; i++)
		{
			tiles[i] = new Tile(destX, destY, images[i].getWidth(), images[i].getHeight(), i);
			
			destX = destX + images[i].getWidth() + 4;
		}
	}
	
	//Getter methods
	public BufferedImage[] getImages() {return images;}
	public int getNumImages() {return numImages;}
	public int getStart() {return start;}
	public int getEnd() {return end;}
	public int getCurrentImage() {return currentImage;}
	
	//Setter methods
	public void setImages(BufferedImage[] images) {this.images = images;}
	public void setStart(int start) {this.start = start;}
	public void setEnd(int end) {this.end = end;}
	
	/**
	 * Method that sets the range for tiles array
	 * @param start (int) start index for tiles array
	 * @param end (int) end index for tiles array
	 */
	public void setRange(int start, int end)
	{
		this.start = start;
		this.end = end;
		
		int colCounter = 0;
		int maxColSize = 15;
		
		//Determine x and y coordinates for tiles
		int destX = tiles[0].getX() + tiles[0].getWidth() + 4;
		int destY = tiles[0].getY();
		
		for(int i = start; i <= end; i++)
		{
			tiles[i].setX(destX);
			tiles[i].setY(destY);
			
			++colCounter;
			
			if(colCounter == maxColSize)
			{
				colCounter = 0;
				destX = tiles[0].getX() + tiles[0].getWidth() + 4;
				destY = destY + tiles[i].getHeight() + 4;
				
			}
			else
			{
				destX = destX + tiles[i].getWidth() + 4;
			}
		}
		
		currentImage = 0;
	}
	
	/**
	 * Method that updates the Palette
	 */
	public void update()
	{	
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
		
		g.drawImage(
				images[0],
				tiles[0].getX(),
				tiles[0].getY(),
				null
		);
		
		g.drawRect(
				tiles[0].getX(),
				tiles[0].getY(),
				tiles[0].getWidth() - 1,
				tiles[0].getHeight() - 1
		);
		
		//Draw tile images
		for(int i = start; i <= end; i++)
		{
			g.drawImage(
					images[i],
					tiles[i].getX(),
					tiles[i].getY(),
					null
			);
			
			if(i != currentImage)
			{
				g.drawRect(
						tiles[i].getX(),
						tiles[i].getY(),
						tiles[i].getWidth() - 1,
						tiles[i].getHeight() - 1
				);
			}
		}
		
		g.setColor(Color.RED);
		g.drawRect(
				tiles[currentImage].getX(),
				tiles[currentImage].getY(),
				tiles[currentImage].getWidth() - 1,
				tiles[currentImage].getHeight() - 1
		);
	}
	
	/**
	 * Method that draws the currentImage
	 * @param g (Graphics2D) the Graphics2D object to be drawn on
	 */
	private void drawCurrentImage(Graphics2D g)
	{
		g.drawImage(
				images[currentImage],
				x + GamePanel.WIDTH - 256,
				y + 32,
				null
		);
		
		g.setColor(Color.RED);
		g.drawRect(
				x + GamePanel.WIDTH - 256,
				y + 32,
				images[currentImage].getWidth() - 1,
				images[currentImage].getHeight() - 1
		);
	}
	
	/**
	 * Method that draws the Palette
	 * @param g (Graphics2D) the Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		drawPaletteBackground(g);
		drawBorder(g);
		drawImages(g);
		drawCurrentImage(g);
	}
}
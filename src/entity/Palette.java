package entity;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;

import manager.MouseManager;
import map.Tile;
import helper.Collision;

public class Palette extends Entity
{
	//To manage mouse events
	private MouseManager mouseManager;
	
	private BufferedImage[] images;
	private int numImages;
	private int start;
	private int end;
	private int currentImage;
	
	//Color of Palette
	private Color color;
	
	private Tile tiles[];
	
	//Default color of Palette
	private static final Color DEFAULT_COLOR = new Color(100, 100, 100);
	
	/**
	 * Constructor
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 * @param width integer width of the Palette
	 * @param height integer height of the Palette
	 * @param images BufferedImages within Palette
	 */
	public Palette(int x, int y, int width, int height, BufferedImage images[])
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
	
	private void createTiles()
	{
		tiles = new Tile[images.length];
		
		int destX = x + 32;
		int destY = y + 96;
		
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
	
	public void setRange(int start, int end)
	{
		this.start = start;
		this.end = end;
		
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
		
		//Check if mouse clicked on an image
		if(mouseManager.getPressedPoint() != null && mouseManager.getReleasedPoint() != null)
		{
			int pressedX = mouseManager.getPressedPoint().getX();
			int pressedY = mouseManager.getPressedPoint().getY();
			int releasedX = mouseManager.getReleasedPoint().getX();
			int releasedY = mouseManager.getReleasedPoint().getY();
			
			if(Collision.pointEntityCollision(pressedX, pressedY, tiles[0]) &&
			   Collision.pointEntityCollision(releasedX, releasedY, tiles[0]))
			{
				currentImage = 0;
				mouseManager.clearPressedPoint();
				mouseManager.clearReleasedPoint();
				return;
			}
			
			for(int i = start; i <= end; i++)
			{
				if(Collision.pointEntityCollision(pressedX, pressedY, tiles[i]) &&
				   Collision.pointEntityCollision(releasedX, releasedY, tiles[i]))
				{
					currentImage = i;
					mouseManager.clearPressedPoint();
					mouseManager.clearReleasedPoint();
					break;
				}
			}
		}
	}
	
	private void drawImages(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		
		g.drawImage(images[0], tiles[0].getX(), tiles[0].getY(), null);
		g.drawRect(tiles[0].getX(), tiles[0].getY(), tiles[0].getWidth(), tiles[0].getHeight());
		
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
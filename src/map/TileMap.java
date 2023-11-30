package map;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import entity.Camera;
import entity.Tile;
import graph.*;

/**
 * TileMap class represents a map (Tile Map)
 * @author Vachia Thoj
 *
 */
public class TileMap implements Serializable
{
	//For serialization
	private static final long serialVersionUID = 1L;
	
	//x and y coordinate of where TileMap starts
	private int startX;
	private int startY;
	
	//x and y coordinate of where TileMap ends
	private int endX;
	private int endY;
	
	//width and height of TileMap
	private int width;
	private int height;
	
	//Size (pixels) of a tile in TileMap
	private int tileSize;
	
	//Number of columns and rows in TileMap
	private int numCols;
	private int numRows;
	
	//The TileMap as a 2D array
	private Tile[][] map;
	
	//Column and row of map to start and end render/draw map
	private transient int startCol;
	private transient int endCol;
	private transient int startRow;
	private transient int endRow;
	
	//The maps offset from the screen/window
	private transient int offSetX;
	private transient int offSetY;
	
	//Camera
	private transient Camera camera;
	
	private transient BufferedImage[] images;
	
	/**
	 * Constructor 
	 * Creates a default "blank" TileMap
	 * 
	 * @param startX (int) x-coordinate where the TileMap should start
	 * @param startY (int) y-coordinate where the TileMap should start
	 * @param numCols (int) number of columns in TileMap
	 * @param numRows (int) number of rows in TileMap
	 * @param tileSize (int) size of a Tile (pixels)
	 */
	public TileMap(int startX, int startY, int numCols, int numRows, int tileSize)
	{
		this.startX = startX;
		this.startY = startY;
		this.numCols = numCols;
		this.numRows = numRows;
		this.tileSize = tileSize;
		
		this.width = numCols * tileSize;
		this.height = numRows * tileSize;
		
		this.endX = startX + width;
		this.endY = startY + height;
		
		//Create a "blank" map
		createMap();
		
		init();
	}
	
	/**
	 * Method that creates tile map "blank"
	 */
	private void createMap()
	{
		map = new Tile[numRows][numCols];
		
		for(int i = 0; i < numRows; i++)
		{
			for(int j = 0; j < numCols; j++)
			{
				int x = startX + (j * tileSize);
				int y = startY + (i * tileSize);
				int value = 0;
				
				map[i][j] = new Tile(x, y, tileSize, tileSize, value);
			}
		}
	}
	
	/**
	 * Init method
	 */
	public void init()
	{		
		this.images = null;
		this.camera = null;
	}
	
	//Getter Methods
	public int getStartX() {return startX;}
	public int getStartY() {return startY;}
	public int getEndX() {return endX;}
	public int getEndY() {return endY;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getTileSize() {return tileSize;}
	public int getNumCols() {return numCols;}
	public int getNumRows() {return numRows;}
	public int getStartCol() {return startCol;}
	public int getEndCol() {return endCol;}
	public int getStartRow() {return startRow;}
	public int getEndRow() {return endRow;}
	public Tile[][] getTileMap() {return map;}
	public Tile getTile(int col, int row) {return map[row][col];}
	
	//Setter methods
	public void setCamera(Camera camera) {this.camera = camera;}
	public void setImages(BufferedImage[] images) {this.images = images;}
	
	/**
	 * Method to change a Tile's value 
	 * "Changing a Tile from one image to another"
	 * @param changeCol (int) column of Tile to change
	 * @param changeRow (int) row of Tile to change
	 * @param value (int) value to change Tile to
	 */
	public void changeTile(int changeCol, int changeRow, int value)
	{
		if(value >= 0 && value < images.length)
		{
			if(changeCol > -1 && changeRow > -1)
			{
				map[changeRow][changeCol].setValue(value);
			}
		}
	}
	
	public void fillTiles(int changeCol, int changeRow, int value)
	{
		if(value >= 0 && value < images.length)
		{
			if(changeCol > -1 && changeRow > -1)
			{
				Graph graph = new Graph(this);
				FillPath fillPath = new FillPath(graph, changeCol, changeRow);
				fillPath.bfs();
				ArrayList<Node> nodesToFill = fillPath.getNodesToFill();
				
				for(int i = 0; i < nodesToFill.size(); i++)
				{
					Node tempNode = nodesToFill.get(i);
					
					map[tempNode.getRow()][tempNode.getCol()].setValue(value);
				}
			}
		}
	}
	
	/**
	 * Method that updates the TileMap
	 */
	public void update()
	{	
		//Update where the starting column and row to render the map
		//Update where the ending column and row to render the map
		startCol = (int) (Math.floor(camera.getX() / tileSize));
		endCol = (int) (startCol + (camera.getWidth() / tileSize));
		startRow = (int) (Math.floor(camera.getY() / tileSize));
		endRow = (int) (startRow + (camera.getHeight() / tileSize));
		
		//Update the maps offset from the screen/window relative to the Camera's position
		offSetX = -camera.getX() + (startCol * tileSize);
		offSetY = -camera.getY() + (startRow * tileSize);
		
		//Index array out of bounds adjustments for tile map
		if(endCol > numCols - 1)
		{ 
			endCol = numCols - 1;
		}
		
		if(endRow > numRows - 1)
		{
			endRow = numRows - 1;
		}
	}
	
	/**
	 * Method that draws the map
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawMap(Graphics2D g)
	{		
		//Draw tileMap
		for(int i = startRow; i <= endRow ; i++)
		{
			for(int j = startCol; j <= endCol; j++)
			{
				int x = (j - startCol) * tileSize + offSetX;
				int y = (i - startRow) * tileSize + offSetY;
				int index = map[i][j].getValue();
						
				if(index == 0)
				{
					continue;
				}
					//Draw tileImage
					g.drawImage(images[index], x, y, null);
				}
			}
	}
	
	/**
	 * Method that draws the TileMap
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		drawMap(g);
	}
}
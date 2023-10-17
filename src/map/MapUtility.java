package map;

import java.awt.Graphics2D;
import java.awt.Color;

import entity.Camera;
import manager.MouseManager;
import manager.KeyManager;

/**
 * MapUtility class helps out the user for better UI for the TileMap
 * i.e. create a grid overlay for the TileMap, highlight the current tile the
 * mouse is touching
 * @author Vachia Thoj
 *
 */
public class MapUtility 
{
	//To manage mouse events
	private MouseManager mouseManager;
	private KeyManager keyManager;
	
	//TileMap
	private TileMap tileMap;
	
	//Camera
	private Camera camera;
	
	//Flag to see if grid overlay is on
	private boolean grid;
	
	//Flag to see if a Tile is highlighted
	private boolean highlighted;
	
	//Column and row of grid overlay to start and end render/draw grid overlay
	private int startCol;
	private int endCol;
	private int startRow;
	private int endRow;
	
	//The grid overlay's offset from the screen/window
	private int offSetX;
	private int offSetY;
	
	//To determine if TileMap has bee clicked on
	private boolean clicked;
	
	//Column and Row of TileMap to change Tile
	private int changeCol;
	private int changeRow;
	
	//For keyboard input; SHIFT key
	private static final int SHIFT = 4;
	
	/**
	 * Constructor
	 * @param tileMap (TileMap) A TileMap object
	 * @param camera (Camera) A Camera object
	 */
	public MapUtility(TileMap tileMap, Camera camera)
	{
		//Obtain managers
		this.mouseManager = MouseManager.instance();
		this.keyManager = KeyManager.instance();
		
		this.tileMap = tileMap;
		this.camera = camera;
		
		this.grid = true;
		this.highlighted = true;
		
		this.startCol = (int) (Math.floor(camera.getX() / tileMap.getTileSize()));
		this.endCol = (int) (startCol + (camera.getWidth() / tileMap.getTileSize()));
		this.startRow = (int) (Math.floor(camera.getY() / tileMap.getTileSize()));
		this.endRow = (int) (startRow + (camera.getHeight() / tileMap.getTileSize()));
		
		this.offSetX = -camera.getX() + (startCol * tileMap.getTileSize());
		this.offSetY = -camera.getY() + (startRow * tileMap.getTileSize());
		
		this.clicked = false;
		this.changeCol = -1;
		this.changeRow = -1;
	}
	
	//Getter Methods
	public boolean isGrid() {return grid;}
	public boolean isHighlighted() {return highlighted;}
	public boolean isClicked() {return clicked;}
	public int getChangeCol() {return changeCol;}
	public int getChangeRow() {return changeRow;}
	
	//Setter Methods
	public void setGrid(boolean b) {this.grid = b;}
	public void setHighlighted(boolean b) {this.highlighted = b;}
	public void setClicked(boolean b) {this.clicked = b;}
	public void setChangeCol(int changeCol) {this.changeCol = changeCol;}
	public void setChangeRow(int changeRow) {this.changeRow = changeRow;}
	
	/**
	 * Method that updates MapUtility
	 */
	public void update()
	{
		//Update where the starting column and row to render the grid overlay
		//Update where the ending column and row to render the grid overlay
		startCol = (int) (Math.floor(camera.getX() / tileMap.getTileSize()));
		endCol = (int) (startCol + (camera.getWidth() / tileMap.getTileSize()));
		startRow = (int) (Math.floor(camera.getY() / tileMap.getTileSize()));
		endRow = (int) (startRow + (camera.getHeight() / tileMap.getTileSize()));
		
		//Update the grid overlay's offset from the screen/window relative to the Camera's position
		offSetX = -camera.getX() + (startCol * tileMap.getTileSize());
		offSetY = -camera.getY() + (startRow * tileMap.getTileSize());
		
		//If SHIFT key pressed, check where the mouse is
		if(keyManager.isKeyDown(SHIFT) == true && mouseManager.getCurrentPoint() != null)
		{
			//Obtain x and y coordinate of mouse
			int mouseX = mouseManager.getCurrentPoint().getX();
			int mouseY = mouseManager.getCurrentPoint().getY();
			
			//If mouse is within the Camera's viewpoint..
			if(mouseX < camera.getWidth() && mouseY < camera.getHeight())
			{
				//Determine column and row to map in order to change a Tile
				mouseX = camera.getX() + mouseX;
				mouseY = camera.getY() + mouseY;
				
				changeCol = (int) (mouseX / 32);
				changeRow = (int) (mouseY / 32);
				
				clicked = true;
			}
		}
		
		//Check if mouse has been clicked
		if(mouseManager.getPressedPoint() != null && mouseManager.getReleasedPoint() != null)
		{
			//Determine where the mouse pressed and released
			int pressedX = mouseManager.getPressedPoint().getX();
			int pressedY = mouseManager.getPressedPoint().getY();
			int releasedX = mouseManager.getReleasedPoint().getX();
			int releasedY = mouseManager.getReleasedPoint().getY();
			
			//Check if where the mouse was clicked is within a certain range
			if(pressedX < 960 && pressedY < 480 && releasedX < 960 && releasedY < 480)
			{
				//Determine which column and row to change Tile for the TileMap
				pressedX = camera.getX() + pressedX;
				pressedY = camera.getY() + pressedY;
				releasedX = camera.getX() + releasedX;
				releasedY = camera.getY() + releasedY;
				
				if(((pressedX / 32) == (releasedX / 32)) &&
				   ((pressedY / 32) == (releasedY / 32)))
				{
					changeCol = (int) (pressedX / 32);
					changeRow = (int) (pressedY / 32);
					clicked = true;
					
					mouseManager.clearPressedPoint();
					mouseManager.clearReleasedPoint();
				}
			}
		}
	}
	
	/**
	 * Method that draws the grid
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawGrid(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		
		//Draw outlines for map; grid overlay
		for(int i = startRow; i <= endRow; i++)
		{
			for(int j = startCol; j <= endCol; j++)
			{
				int x = (j - startCol) * tileMap.getTileSize() + offSetX;
				int y = (i - startRow) * tileMap.getTileSize() + offSetY;
				
				//Draw tileImage
				g.drawRect(x, y, tileMap.getTileSize(), tileMap.getTileSize());
			}
		}
	}
	
	/**
	 * Method that draws a highlighted square for where mouse 
	 * is located at on the TileMap
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawHighlighted(Graphics2D g)
	{
		if(mouseManager.getCurrentPoint() != null)
		{
			//Determine where the mouse currently at
			int mouseX = mouseManager.getCurrentPoint().getX();
			int mouseY = mouseManager.getCurrentPoint().getY();
			
			//Check if the mouse is within the Camera's viewpoint
			if(mouseX < camera.getWidth() && mouseY < camera.getHeight())
			{
				//Determine where to draw the highlighted square
				mouseX = mouseX + camera.getX();
				mouseY = mouseY + camera.getY();
				
				int col = (int) (mouseX / 32);
				int row = (int) (mouseY / 32);
				
				int x = -camera.getX() + (col * 32);
				int y = -camera.getY() + (row * 32);
				
				g.setColor(new Color(255, 255, 0, 100));
				g.fillRect(x, y, 32, 32);
			}
		}
	}
	
	/**
	 * Method that draws the MapUtility
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		if(grid)
		{
			//Draw grid overlay
			drawGrid(g);
		}
		
		if(highlighted)
		{
			//Draw highlighted square
			drawHighlighted(g);
		}
	}
}

package palette;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.GameObject;
import entity.GameObjectType;
import helper.Collision;
import manager.MouseManager;

/**
 * ObjectPalette class represents a Palette of objects
 * that can be added onto a map
 * @author Vachia Thoj
 *
 */
public class ObjectPalette extends Palette
{
	private static final long serialVersionUID = 1L;
	
	//MouseManager
	private transient MouseManager mouseManager;
	
	//Images
	private transient BufferedImage[] treeImages;
	private transient BufferedImage[] bushImages;
	private transient BufferedImage[] rockImages;
	private transient BufferedImage[] structureImages;
	private transient BufferedImage[] buildingImages;
	private transient BufferedImage[] miscImages;
	private transient BufferedImage[] currentImages;
	
	//GameObjects
	private transient GameObject[] trees;
	private transient GameObject[] bushes;
	private transient GameObject[] rocks;
	private transient GameObject[] structures;
	private transient GameObject[] buildings;
	private transient GameObject[] miscs;
	private transient GameObject[] currentGameObjects;
	
	//To keep track of the current object that ObjectPalette is on
	private transient int currentGameObjectIndex;
	
	/**
	 * Constructor
	 * @param x (int) x-coordinate of ObjectPalette
	 * @param y (int) y-coordinate of ObjectPalette
	 * @param width (int) width of the ObjectPalette
	 * @param height (int) height of the ObjectPalette
	 * @param treeImages (BufferedImage[]) An array of tree images
	 * @param bushImages (BufferedImage[]) An array of bush images
	 * @param rockImages (BufferedImage[]) An array of rock images
	 * @param structureImages (BufferedImage[]) An array of structure images
	 * @param miscImages (BufferedImage[]) An array of misc images
	 * @param buildingImages (BufferedImage[]) An array of building images
	 */
	public ObjectPalette(int x, int y, int width, int height, BufferedImage[] treeImages, BufferedImage[] bushImages, BufferedImage[] rockImages, BufferedImage[] structureImages, BufferedImage[] buildingImages, BufferedImage[] miscImages)
	{
		super();
		
		this.mouseManager = MouseManager.instance();
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.treeImages = treeImages;
		this.bushImages = bushImages;
		this.rockImages = rockImages;
		this.structureImages = structureImages;
		this.buildingImages = buildingImages;
		this.miscImages = miscImages;
		
		this.currentImages = treeImages;
		
		this.bordered = true;
		
		createTrees();
		createBushes();
		createRocks();
		createStructures();
		createBuildings();
		createMiscs();
		this.currentGameObjects = trees;
		
		this.currentGameObjectIndex = 0;
	}
	
////////////////////////////////////////////// CREATE METHODS //////////////////////////////////////////////
	private void createTrees()
	{
		trees = new GameObject[treeImages.length];
		for(int i = 0; i < treeImages.length; i++)
		{
			if(i == 0)
			{
				trees[i] = new GameObject(
						i, 
						x + 16,
						y + 32,
						treeImages[i].getWidth(),
					 	treeImages[i].getHeight(),
						GameObjectType.NONE
				);
			}
			else
			{
				trees[i] = new GameObject(
						i, 
						trees[i - 1].getX() + trees[i-1].getWidth() + 4,
					 	y + 32,
						treeImages[i].getWidth(),
						treeImages[i].getHeight(),
						GameObjectType.TREE
				);
			}
		}
	}
	
	private void createBushes()
	{
		bushes = new GameObject[bushImages.length];
		
		for(int i = 0; i < bushImages.length; i++)
		{
			if(i == 0)
			{
				bushes[i] = new GameObject(
						i, 
						x + 16,
						y + 32,
						bushImages[i].getWidth(),
						bushImages[i].getHeight(),
						GameObjectType.NONE
				);
			}
			else
			{
				bushes[i] = new GameObject(
						i, 
						bushes[i - 1].getX() + bushes[i-1].getWidth() + 4,
						y + 32,
						bushImages[i].getWidth(),
						bushImages[i].getHeight(),
						GameObjectType.BUSH
				);
			}
		}
		
	}
	
	private void createRocks()
	{
		rocks = new GameObject[rockImages.length];
		
		for(int i = 0; i < rockImages.length; i++)
		{
			if(i == 0)
			{
				rocks[i] = new GameObject(
						i, 
						x + 16,
						y + 32,
						rockImages[i].getWidth(),
						rockImages[i].getHeight(),
						GameObjectType.NONE
				);
			}
			else
			{
				rocks[i] = new GameObject(i, 
									 rocks[i - 1].getX() + rocks[i-1].getWidth() + 4,
									 y + 32,
									 rockImages[i].getWidth(),
									 rockImages[i].getHeight(),
									 GameObjectType.ROCK);
			}
		}
	}
		
	private void createStructures()
	{
		structures = new GameObject[structureImages.length];
		
		for(int i = 0; i < structureImages.length; i++)
		{
			if(i == 0)
			{
				structures[i] = new GameObject(
						i, 
						x + 16,
						y + 32,
						structureImages[i].getWidth(),
						structureImages[i].getHeight(),
						GameObjectType.NONE
				);
			}
			else
			{
				structures[i] = new GameObject(
						i, 
					 	structures[i - 1].getX() + structures[i-1].getWidth() + 4,
						y + 32,
						structureImages[i].getWidth(),
						structureImages[i].getHeight(),
						GameObjectType.STRUCTURE
				);
			}
		}
	}
	
	private void createBuildings()
	{
		buildings = new GameObject[buildingImages.length];
		
		for(int i = 0; i < buildingImages.length; i++)
		{
			if(i == 0)
			{
				buildings[i] = new GameObject(
						i, 
						x + 16,
						y + 32,
						buildingImages[i].getWidth(),
						buildingImages[i].getHeight(),
						GameObjectType.NONE
				);
			}
			else
			{
				buildings[i] = new GameObject(
						i, 
						buildings[i - 1].getX() + buildings[i-1].getWidth() + 4,
						y + 32,
						buildingImages[i].getWidth(),
						buildingImages[i].getHeight(),
						GameObjectType.BUILDING
				);
			}
		}
	}
	
	private void createMiscs()
	{
		miscs = new GameObject[miscImages.length];
		
		for(int i = 0; i < miscImages.length; i++)
		{
			if(i == 0)
			{
				miscs[i] = new GameObject(
						i, 
						x + 16,
						y + 32,
						miscImages[i].getWidth(),
						miscImages[i].getHeight(),
						GameObjectType.NONE
				);
			}
			else
			{
				miscs[i] = new GameObject(
						i, 
						miscs[i - 1].getX() + miscs[i-1].getWidth() + 4,
						y + 32,
						miscImages[i].getWidth(),
						miscImages[i].getHeight(),
						GameObjectType.MISC
				);
			}
		}
	}
	
	//Getter methods
	public BufferedImage[] getTreeImages() {return treeImages;}
	public BufferedImage[] getRockImages() {return rockImages;}
	public BufferedImage[] getBushImages() {return bushImages;}
	public BufferedImage[] getStructureImages() {return structureImages;}
	public BufferedImage[] getBuildingImages() {return buildingImages;}
	public BufferedImage[] getMiscImages() {return miscImages;}
	public BufferedImage[] getCurrentImages() {return currentImages;}
	public GameObject[] getTrees() {return trees;}
	public GameObject[] getBushes() {return bushes;}
	public GameObject[] getRocks() {return rocks;}
	public GameObject[] getStructures() {return structures;}
	public GameObject[] getBuildings() {return buildings;}
	public GameObject[] getMiscs() {return miscs;}
	public int getCurrentGameObjectIndex() {return currentGameObjectIndex;}
	
	public GameObject getCurrentGameObject(int index)
	{
		if(index >= 0 && index < currentGameObjects.length)
		{
			return currentGameObjects[index];
		}
		
		return null;
	}
	
	//Setter methods
	public void setCurrentImages(BufferedImage[] currentImages) {this.currentImages = currentImages;}
	public void setCurrentGameObjects(GameObject[] currentGameObjects) {this.currentGameObjects = currentGameObjects;}
	public void setCurrentGameObjectIndex(int currentGameObjectIndex) {this.currentGameObjectIndex = currentGameObjectIndex;}
	
	/**
	 * Method that updates the ObjectPalette
	 */
	public void update()
	{
		//Check if mouse clicked on a GameObject
		if(mouseManager.getPressedPoint() != null && mouseManager.getReleasedPoint() != null)
		{
			//Obtain x and y coordinates where the mouse pressed and release
			int pressedX = mouseManager.getPressedPoint().getX();
			int pressedY = mouseManager.getPressedPoint().getY();
			int releasedX = mouseManager.getReleasedPoint().getX();
			int releasedY = mouseManager.getReleasedPoint().getY();
			
			for(int i = 0; i < currentGameObjects.length; i++)
			{
				//Check if mouse clicked on any of the GameObjects
				if(Collision.pointEntityCollision(pressedX, pressedY, currentGameObjects[i]) &&
				   Collision.pointEntityCollision(releasedX, releasedY, currentGameObjects[i]))
				{
					currentGameObjectIndex = i;
					mouseManager.clearPressedPoint();
					mouseManager.clearReleasedPoint();
					break;
				}
			}
		}
	}
	
	/**
	 * Method that draws the current images
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawCurrentImages(Graphics2D g)
	{
		for(int i = 0; i < currentImages.length; i++)
		{
			g.drawImage(
					currentImages[i],
					currentGameObjects[i].getX(),
					currentGameObjects[i].getY(),
					null
			);
			
			if(i == currentGameObjectIndex)
			{
				g.setColor(Color.RED);
			}
			else
			{
				g.setColor(Color.BLACK);
			}
			
			g.drawRect(
					currentGameObjects[i].getX(),
					currentGameObjects[i].getY(),
					currentGameObjects[i].getWidth() - 1,
					currentGameObjects[i].getHeight() - 1
			);
		}
	}
	
	/**
	 * Method that draws the Palette
	 * @param g (Graphics2D) the Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		drawPaletteBackground(g);
		drawBorder(g);
		drawCurrentImages(g);
	}
}
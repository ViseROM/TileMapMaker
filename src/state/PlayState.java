package state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import button.*;
import entity.*;
import main.GamePanel;
import manager.ImageManager;
import manager.MouseManager;
import manager.SaveManager;
import manager.StateManager;
import map.*;
import menu.*;
import palette.ObjectPalette;
import palette.Palette;
import palette.TilePalette;
import transition.*;

/**
 * PlayState class represents the "play area" of Tile Map Maker 
 * 
 * @author Vachia Thoj
 *
 */
public class PlayState extends State
{
	//Managers
	private ImageManager imageManager;
	private SaveManager saveManager;
	
	//The next state to go to
	private StateType nextState;
	
	//Maps
	private TileMap backgroundMap;
	private TileMap tileMap;
	private ObjectMap objectMap;
	private TileMap itemMap;
	private TileMap hitboxMap;
	
	//Camera
	private Camera camera;
	
	//MapHelper
	private MapHelper mapHelper;
	
	//Palettes
	private TilePalette tilePalette;
	private ObjectPalette objectPalette;
	private TilePalette itemPalette;
	private TilePalette hitboxPalette;
	private Palette currentPalette;
	
	//VisionIcons
	private VisionIcon[] visionIcons;
	
	//Buttons
	private ImageButton tileLayerButton;
	private ImageButton objectLayerButton;
	private ImageButton itemLayerButton;
	private ImageButton hitboxLayerButton;
	private ImageButton gridButton;
	private ImageButton saveButton;
	private ImageButton mainButton;
	private ImageButton grassButton;
	private ImageButton pathButton;
	private ImageButton wallButton;
	private ImageButton waterButton;
	private ImageButton allButton;
	private ImageButton treeButton;
	private ImageButton bushButton;
	private ImageButton rockButton;
	private ImageButton structureButton;
	private ImageButton buildingButton;
	private ImageButton miscButton;
	private ImageButton itemButton;
	private ImageButton hitboxButton;
	
	private CustomIcon arrowIcon;
	private CustomIcon fillIcon;
	
	//Texts
	private Text currentLocationText;
	private Text currentLayerText;
	
	//Save Menu
	private SaveMenu saveMenu;
	
	//The current drawing mode
	private DrawMode currentDrawMode;
	
	//Transition
	private FadeToBlack fadeToBlack;
	
	//Default Values
	private static final int DEFAULT_NUM_COLS = 150;
	private static final int DEFAULT_NUM_ROWS = 90;
	private static final int DEFAULT_TILE_SIZE = 16;

	/**
	 * Constructor
	 */
	public PlayState()
	{
		//Obtain managers
		this.imageManager = ImageManager.instance();
		this.saveManager = SaveManager.instance();
		
		this.nextState = null;
		
		//Initialize and set various objects and variables
		createMaps();
		createCamera();		
		setCameraToMaps();
		createMapHelper();
		createPalettes();
		createVisionIcons();
		createButtons();
		createTexts();
		createMenus();
		createTransitions();
		
		this.currentDrawMode = DrawMode.ARROW;
	}
	
////////////////////////////////////////////// CREATE METHODS //////////////////////////////////////////////
	private void createMaps()
	{
		//Check if a save file exists
		if(saveManager.getCurrentSaveFile() != null)
		{
			//Load maps from save file
			this.tileMap = saveManager.getCurrentSaveFile().getTileMap();
			this.objectMap = saveManager.getCurrentSaveFile().getObjectMap();
			this.itemMap = saveManager.getCurrentSaveFile().getItemMap();
			this.hitboxMap = saveManager.getCurrentSaveFile().getHitboxMap();
		}
		else
		{
			//Create default, "empty" maps
			this.tileMap = new TileMap(0, 0, DEFAULT_NUM_COLS, DEFAULT_NUM_ROWS, DEFAULT_TILE_SIZE);
			this.objectMap = new ObjectMap();
			this.itemMap = new TileMap(0, 0, DEFAULT_NUM_COLS, DEFAULT_NUM_ROWS, DEFAULT_TILE_SIZE);
			this.hitboxMap = new TileMap(0, 0, DEFAULT_NUM_COLS, DEFAULT_NUM_ROWS, DEFAULT_TILE_SIZE);
		}
		
		this.backgroundMap = new TileMap(0, 0, tileMap.getNumCols(), tileMap.getNumRows(), tileMap.getTileSize());
		
		//Set images to maps
		this.tileMap.setImages(imageManager.getTileImages());
		this.itemMap.setImages(imageManager.getItemImages());
		this.hitboxMap.setImages(imageManager.getHitboxImages());
		
		this.objectMap.setTreeImages(imageManager.getTreeImages());
		this.objectMap.setBushImages(imageManager.getBushImages());
		this.objectMap.setRockImages(imageManager.getRockImages());
		this.objectMap.setStructureImages(imageManager.getStructureImages());
		this.objectMap.setBuildingImages(imageManager.getBuildingImages());
		this.objectMap.setMiscImages(imageManager.getMiscImages());
	}	
	
	private void createCamera()
	{
		this.camera = new Camera(0, 0, 480, 240, backgroundMap);
	}
	
	private void setCameraToMaps()
	{
		//Set camera to maps
		this.backgroundMap.setCamera(camera);
		this.tileMap.setCamera(camera);
		this.objectMap.setCamera(camera);
		this.itemMap.setCamera(camera);
		this.hitboxMap.setCamera(camera);
		
		//Update maps so that they are aligned to
		//where the camera is looking
		this.backgroundMap.update();
		this.tileMap.update();
		this.objectMap.init();
		this.objectMap.update();
		this.itemMap.update();
		this.hitboxMap.update();
	}
	
	private void createMapHelper()
	{
		this.mapHelper = new MapHelper(backgroundMap, camera);
	}
	
	private void createPalettes()
	{
		this.tilePalette = new TilePalette(
				0,
				camera.getHeight(),
				GamePanel.WIDTH, GamePanel.HEIGHT - 160,
				imageManager.getTileImages()
		);
		this.tilePalette.setRange(1, 14);
		
		this.objectPalette = new ObjectPalette(
				0,
				camera.getHeight(),
				GamePanel.WIDTH,
				GamePanel.HEIGHT - 160,
				imageManager.getTreeImages(),
				imageManager.getBushImages(),
				imageManager.getRockImages(),
				imageManager.getStructureImages(),
				imageManager.getBuildingImages(),
				imageManager.getMiscImages()
		);
		
		this.itemPalette = new TilePalette(
				0,
				camera.getHeight(),
				GamePanel.WIDTH,
				GamePanel.HEIGHT - 160,
				imageManager.getItemImages()
		);
		this.itemPalette.setRange(1, 28);
		
		this.hitboxPalette = new TilePalette(
				0,
				camera.getHeight(),
				GamePanel.WIDTH, GamePanel.HEIGHT - 160,
				imageManager.getHitboxImages()
		);
		this.hitboxPalette.setRange(1, 1);
		
		this.currentPalette = tilePalette;
	}

	private void createVisionIcons()
	{
		BufferedImage[] buttonImages = imageManager.getButtonImages();
		
		visionIcons = new VisionIcon[4];
		for(int i = 0; i < visionIcons.length; i++)
		{
			if(i == 0)
			{
				visionIcons[i] = new VisionIcon(camera.getWidth() + 4,
												32, 
												buttonImages[4],
												buttonImages[5], 
												buttonImages[6]);
			}
			else
			{
				visionIcons[i] = new VisionIcon(camera.getWidth() + 4,
												visionIcons[i-1].getY() + visionIcons[i-1].getHeight() + 2, 
												buttonImages[4],
												buttonImages[5], 
												buttonImages[6]);
			}

			visionIcons[i].setVisible(true);
			visionIcons[i].setDisabled(false);
		}
	}
	
	private void createButtons()
	{
		BufferedImage[] buttonImages = imageManager.getButtonImages();
		
		this.tileLayerButton = new ImageButton(
				visionIcons[0].getX() + visionIcons[0].getWidth() + 2,
				32,
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"TILE LAYER"
		);
				
		this.objectLayerButton = new ImageButton(
				visionIcons[1].getX() + visionIcons[1].getWidth() + 2,
				tileLayerButton.getY() + tileLayerButton.getHeight() + 2,
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"OBJECT LAYER"
		);
		
		this.itemLayerButton = new ImageButton(
				visionIcons[2].getX() + visionIcons[2].getWidth() + 2,
				objectLayerButton.getY() + objectLayerButton.getHeight() + 2,
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"ITEM LAYER"
		);
				
		this.hitboxLayerButton = new ImageButton(
				visionIcons[3].getX() + visionIcons[3].getWidth() + 2,
				itemLayerButton.getY() + itemLayerButton.getHeight() + 2,
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"HITBOX LAYER"
		);
		
		this.gridButton = new ImageButton(
				camera.getWidth() + 4,
				tilePalette.getY() - buttonImages[2].getHeight() * 4,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"GRID"
		);
		
		this.saveButton = new ImageButton(
				camera.getWidth() + 4,
				gridButton.getY() + gridButton.getHeight() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"SAVE"
		);
		
		this.mainButton = new ImageButton(
				camera.getWidth() + 4,
				saveButton.getY() + saveButton.getHeight() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"MAIN"
				);
		
		this.arrowIcon = new CustomIcon(buttonImages[7], buttonImages[8], buttonImages[9]);
		this.arrowIcon.setX(GamePanel.WIDTH - 64);
		this.arrowIcon.setY(gridButton.getY());
		
		this.fillIcon = new CustomIcon(buttonImages[7], buttonImages[8], buttonImages[10]);
		this.fillIcon.setX(arrowIcon.getX() + arrowIcon.getWidth() + 2);
		this.fillIcon.setY(arrowIcon.getY());
		
		this.grassButton = new ImageButton(
				2,
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"GRASS"
		);
		
		this.pathButton = new ImageButton(
				grassButton.getX() + grassButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"PATH"
		);
		
		this.wallButton = new ImageButton(
				pathButton.getX() + pathButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"WALL"
		);
		
		this.waterButton = new ImageButton(
				wallButton.getX() + wallButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"WATER"
		);
		
		this.allButton = new ImageButton(
				waterButton.getX() + waterButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"ALL"
		);
		
		this.treeButton = new ImageButton(
				2,
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"TREE"
		);
		
		this.bushButton = new ImageButton(
				treeButton.getX() + treeButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"BUSH"
		);
		
		this.rockButton = new ImageButton(
				bushButton.getX() + bushButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"ROCK"
		);
		
		this.structureButton = new ImageButton(
				rockButton.getX() + rockButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"STRUCTURE"
		);
		
		this.buildingButton = new ImageButton(
				structureButton.getX() + structureButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"BUILDING"
		);
		
		this.miscButton = new ImageButton(
				buildingButton.getX() + buildingButton.getWidth(),
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"MISC"
		);
		
		this.itemButton = new ImageButton(
				2,
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"ITEM"
		);
		
		this.hitboxButton = new ImageButton(
				2,
				tilePalette.getY() + 2,
				buttonImages[2].getWidth(),
				buttonImages[2].getHeight(),
				buttonImages[2],
				buttonImages[3],
				"HITBOX"
		);
	}
	
	private void createTexts()
	{
		this.currentLayerText = new Text("CURRENT LAYER: TILE LAYER");
		this.currentLayerText.setX(camera.getWidth() + 4);
		this.currentLayerText.setY(hitboxLayerButton.getY() + hitboxLayerButton.getHeight() + 16);

		this.currentLocationText = new Text("[" + mapHelper.getCurrentRow() + " " + mapHelper.getCurrentCol() + "]");
		this.currentLocationText.setX(camera.getWidth() + 4);
		this.currentLocationText.setY(currentLayerText.getY() + currentLayerText.getHeight() + 8);
	}
	
	private void createMenus()
	{
		this.saveMenu = new SaveMenu(
				16,
				16,
				GamePanel.WIDTH - 32,
				GamePanel.HEIGHT - 32,
				tileMap,
				objectMap,
				itemMap,
				hitboxMap
			);
	}
	
	private void createTransitions()
	{
		this.fadeToBlack = new FadeToBlack(GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
////////////////////////////////////////////// UPDATE METHODS //////////////////////////////////////////////
	
	/**
	 * Method that updates the maps
	 */
	private void updateMaps()
	{
		backgroundMap.update();
		tileMap.update();
		objectMap.update();
		itemMap.update();
		hitboxMap.update();
	}
	
	/**
	 * Method that updates mapHelper
	 */
	private void updateMapHelper()
	{
		mapHelper.update();
		
		//If mapHelper has been clicked on;
		//The map area has been clicked on
		if(mapHelper.isClicked())
		{
			mapHelper.setClicked(false);
			
			if(currentPalette == tilePalette)
			{
				//Change a tile on the tileMap (Add a tile to the tileMap)
				switch(currentDrawMode)
				{
					case ARROW:
						tileMap.changeTile(
								mapHelper.getChangeCol(),
								mapHelper.getChangeRow(),
								tilePalette.getCurrentImage()
						);
						break;
					case FILL:
						tileMap.fillTiles(
								mapHelper.getChangeCol(),
								mapHelper.getChangeRow(),
								tilePalette.getCurrentImage()
						);
						break;
					default:
						break;
				}
	
				mapHelper.setChangeCol(-1);
				mapHelper.setChangeCol(-1);
			}
			else if(currentPalette == objectPalette)
			{
				//Add a GameObject to the objectMap
				GameObject temp = objectPalette.getCurrentGameObject(objectPalette.getCurrentGameObjectIndex());
				int x = mapHelper.getChangeCol() * backgroundMap.getTileSize();
				int y = mapHelper.getChangeRow() * backgroundMap.getTileSize();
				objectMap.addGameObject(
						temp.getId(),
						x,
						y,
						temp.getWidth(),
						temp.getHeight(),
						temp.getGameObjectType()
				);
				
				mapHelper.setChangeCol(-1);
				mapHelper.setChangeCol(-1);
			}
			else if(currentPalette == itemPalette)
			{
				//Change a tile on the itemMap (Add an item to the itemMap)
				itemMap.changeTile(
						mapHelper.getChangeCol(),
						mapHelper.getChangeRow(),
						itemPalette.getCurrentImage()
				);
		
				mapHelper.setChangeCol(-1);
				mapHelper.setChangeCol(-1);
			}
			else if(currentPalette == hitboxPalette)
			{
				//Change a tile on the hitboxMap (Add or remove a hitbox tile to hitboxMap)
				switch(currentDrawMode)
				{
					case ARROW:
						hitboxMap.changeTile(
								mapHelper.getChangeCol(),
								mapHelper.getChangeRow(),
								hitboxPalette.getCurrentImage()
						);
						break;
					case FILL:
						hitboxMap.fillTiles(
								mapHelper.getChangeCol(),
								mapHelper.getChangeRow(),
								hitboxPalette.getCurrentImage()
						);
						break;
					default:
						break;
				}
		
				mapHelper.setChangeCol(-1);
				mapHelper.setChangeCol(-1);
			}
		}
		
		//Obtain the row and column on the map that the mouse is currently at
		//Store it as a Text object, so that it can be displayed on screen
		currentLocationText.setText("[" + mapHelper.getCurrentRow() + " " + mapHelper.getCurrentCol() + "]");
	}
	
	/**
	 * Method that updates the camera
	 */
	private void updateCamera()
	{
		camera.update();
	}
	
	/**
	 * Method that updates the currentPalette
	 */
	private void updateCurrentPalette()
	{
		currentPalette.update();
	}
	
	/**
	 * Method that updates the visionIcons
	 */
	private void updateVisionIcons()
	{
		for(int i = 0; i < visionIcons.length; i++)
		{
			visionIcons[i].update();
			
			if(visionIcons[i].isMouseClickingButton())
			{
				visionIcons[i].setMouseClickingButton(false);
				visionIcons[i].setVision(!(visionIcons[i].hasVision()));
			}
		}
	}
	
	/**
	 * Method that updates the Buttons
	 */
	private void updateButtons()
	{
		tileLayerButton.update();
		objectLayerButton.update();
		itemLayerButton.update();
		hitboxLayerButton.update();
		
		gridButton.update();
		saveButton.update();
		mainButton.update();
		
		//Update certain Buttons depending on
		//which Palette is the currentPalette
		if(currentPalette == tilePalette)
		{
			arrowIcon.update();
			fillIcon.update();
			
			grassButton.update();
			pathButton.update();
			wallButton.update();
			waterButton.update();
			allButton.update();
		}
		else if(currentPalette == objectPalette)
		{
			treeButton.update();
			bushButton.update();
			rockButton.update();
			structureButton.update();
			buildingButton.update();
			miscButton.update();
		}
		else if(currentPalette == itemPalette)
		{
			itemButton.update();
		}
		else if(currentPalette == hitboxPalette)
		{
			arrowIcon.update();
			fillIcon.update();
			
			hitboxButton.update();
		}

		//Perform an action if a Button has been clicked on
		performButtonAction();
	}
	
	/**
	 * Method that performs an action if a Button has been clicked on
	 */
	private void performButtonAction()
	{
		if(tileLayerButton.isMouseClickingButton())
		{
			tileLayerButton.setMouseClickingButton(false);
			
			//Change the currentPalette to the tilePalette
			if(currentPalette != tilePalette)
			{
				currentPalette = tilePalette;
				currentLayerText.setText("CURRENT LAYER: TILE LAYER");
			}
			
		}
		else if(objectLayerButton.isMouseClickingButton())
		{
			objectLayerButton.setMouseClickingButton(false);
			
			//Change the currentPalette to the objectPalette
			if(currentPalette != objectPalette)
			{
				currentPalette = objectPalette;
				currentLayerText.setText("CURRENT LAYER: OBJECT LAYER");
				
				currentDrawMode = DrawMode.ARROW;
				mapHelper.setCurrentDrawMode(currentDrawMode);
			}
		}
		else if(itemLayerButton.isMouseClickingButton())
		{
			itemLayerButton.setMouseClickingButton(false);
			
			//Change the currentPalette to the itemPalette
			if(currentPalette != itemPalette)
			{
				currentPalette = itemPalette;
				currentLayerText.setText("CURRENT LAYER: ITEM LAYER");
			
				currentDrawMode = DrawMode.ARROW;
				mapHelper.setCurrentDrawMode(currentDrawMode);
			}
		}
		else if(hitboxLayerButton.isMouseClickingButton())
		{
			hitboxLayerButton.setMouseClickingButton(false);
			
			//Change the currentPalette to the hitboxPalette
			if(currentPalette != hitboxPalette)
			{
				currentPalette = hitboxPalette;
				currentLayerText.setText("CURRENT LAYER: HITBOX LAYER");
			}
		}
		else if(gridButton.isMouseClickingButton())
		{
			gridButton.setMouseClickingButton(false);
			
			//Turn the grid layout on or off
			mapHelper.setGrid(!(mapHelper.isGrid()));
		}
		else if(saveButton.isMouseClickingButton())
		{
			saveButton.setMouseClickingButton(false);
			
			//Bring up the saveMenu
			saveMenu.setVisible(true);
		}
		else if(mainButton.isMouseClickingButton())
		{
			mainButton.setMouseClickingButton(false);
			
			//Run the fadeToBlack transition
			fadeToBlack.setRunning(true);
			
			//Indicate that the next state to go to is the MainState
			nextState = StateType.MAIN;
		}
		else if(arrowIcon.isMouseClickingButton())
		{
			arrowIcon.setMouseClickingButton(false);
			currentDrawMode = DrawMode.ARROW;
			mapHelper.setCurrentDrawMode(currentDrawMode);
		}
		else if(fillIcon.isMouseClickingButton())
		{
			fillIcon.setMouseClickingButton(false);
			currentDrawMode = DrawMode.FILL;
			mapHelper.setCurrentDrawMode(currentDrawMode);
			
		}
		else if(grassButton.isMouseClickingButton())
		{
			grassButton.setMouseClickingButton(false);
			
			//Tell tilePalette to display the grass tiles
			tilePalette.setRange(1, 14);
		}
		else if(pathButton.isMouseClickingButton())
		{
			pathButton.setMouseClickingButton(false);
			
			//Tell tilePalette to display the path tiles
			tilePalette.setRange(15, 28);
		}
		else if(wallButton.isMouseClickingButton())
		{
			wallButton.setMouseClickingButton(false);
			
			//Tell tilePalette to display the wall tiles
			tilePalette.setRange(43, 58);
		}
		else if(waterButton.isMouseClickingButton())
		{
			waterButton.setMouseClickingButton(false);
			
			//Tell tilePalette to display the water tiles
			tilePalette.setRange(29, 42);
		}
		else if(allButton.isMouseClickingButton())
		{
			allButton.setMouseClickingButton(false);
			
			//Tell tilePalette to display all of the tiles
			tilePalette.setRange(1, 58);
		}
		else if(treeButton.isMouseClickingButton())
		{
			treeButton.setMouseClickingButton(false);
			
			//Tell the objectPalette to display the tree objects
			objectPalette.setCurrentImages(objectPalette.getTreeImages());
			objectPalette.setCurrentGameObjects(objectPalette.getTrees());
			objectPalette.setCurrentGameObjectIndex(0);
		}
		else if(bushButton.isMouseClickingButton())
		{
			bushButton.setMouseClickingButton(false);
			
			//Tell the objectPalette to display the bush objects
			objectPalette.setCurrentImages(objectPalette.getBushImages());
			objectPalette.setCurrentGameObjects(objectPalette.getBushes());
			objectPalette.setCurrentGameObjectIndex(0);
		}
		else if(rockButton.isMouseClickingButton())
		{
			rockButton.setMouseClickingButton(false);
			
			//Tell the objectPalette to display the rock objects
			objectPalette.setCurrentImages(objectPalette.getRockImages());
			objectPalette.setCurrentGameObjects(objectPalette.getRocks());
			objectPalette.setCurrentGameObjectIndex(0);
		}
		else if(structureButton.isMouseClickingButton())
		{
			structureButton.setMouseClickingButton(false);
			
			//Tell the objectPalette to display the structure objects
			objectPalette.setCurrentImages(objectPalette.getStructureImages());
			objectPalette.setCurrentGameObjects(objectPalette.getStructures());
			objectPalette.setCurrentGameObjectIndex(0);
		}
		else if(buildingButton.isMouseClickingButton())
		{
			buildingButton.setMouseClickingButton(false);
			
			//Tell the objectPalette to display the building objects
			objectPalette.setCurrentImages(objectPalette.getBuildingImages());
			objectPalette.setCurrentGameObjects(objectPalette.getBuildings());
			objectPalette.setCurrentGameObjectIndex(0);
		}
		else if(miscButton.isMouseClickingButton())
		{
			miscButton.setMouseClickingButton(false);
			
			//Tell the objectPalette to display the misc objects
			objectPalette.setCurrentImages(objectPalette.getMiscImages());
			objectPalette.setCurrentGameObjects(objectPalette.getMiscs());
			objectPalette.setCurrentGameObjectIndex(0);
		}
		else if(itemButton.isMouseClickingButton())
		{
			itemButton.setMouseClickingButton(false);
			
			//Tell the itemPalette to display the items
			itemPalette.setRange(1, 28);
		}
		else if(hitboxButton.isMouseClickingButton())
		{
			hitboxButton.setMouseClickingButton(false);
			
			//Tell the hitbox Palette to display the hitbox tiles
			hitboxPalette.setRange(1, 1);
		}
	}
	
	/**
	 * Method that updates the menus
	 */
	private void updateMenus()
	{
		saveMenu.update();
	}
	
	/**
	 * Method that updates the Transitions
	 */
	private void updateTransitions()
	{
		fadeToBlack.update();
	}
	
	/**
	 * Method that checks if a change of state is necessary
	 */
	private void changeState()
	{
		//If fadeToBlack transition is done and
		//a next state has been decided..
		if(fadeToBlack.isDone() && nextState != null)
		{
			MouseManager.instance().clearCurrentPoint();
			MouseManager.instance().clearReleasedPoint();
			
			//Go to the next state
			StateManager.instance().nextState(nextState);
		}
	}
	
	/**
	 * Method that updates the PlayState
	 */
	public void update()
	{
		updateTransitions();
		if(fadeToBlack.isRunning())
		{
			return;
		}
		changeState();
		
		updateMenus();
		
		if(saveMenu.isVisible())
		{
			return;
		}
		
		updateMaps();
		updateMapHelper();
		updateCamera();
		updateCurrentPalette();
		updateVisionIcons();
		updateButtons();
	}
	

////////////////////////////////////////////// DRAW METHODS //////////////////////////////////////////////	
	
	/**
	 * Method that draws the background
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawBackground(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
	/**
	 * Method that draws the maps
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawMaps(Graphics2D g)
	{
		backgroundMap.draw(g);
		
		//Draw each map depending on if the visibility
		//of a map has been turned on or off
		if(visionIcons[0].hasVision())
		{
			tileMap.draw(g);
		}
		
		if(visionIcons[1].hasVision())
		{
			objectMap.draw(g);
		}
		
		if(visionIcons[2].hasVision())
		{
			itemMap.draw(g);
		}
		
		if(visionIcons[3].hasVision())
		{
			hitboxMap.draw(g);
		}
	}
	
	/**
	 * Method that draws a side bar
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawSideBar(Graphics2D g)
	{
		g.setColor(new Color(228, 207, 184));
		g.fillRect(
				camera.getWidth(),
				0, GamePanel.WIDTH - camera.getWidth(), 
				camera.getHeight()
		);
		
		g.setColor(new Color(132, 68, 40));
		g.drawRect(
				camera.getWidth(),
				0,
				GamePanel.WIDTH - camera.getWidth() - 1, 
				camera.getHeight()
		);
	}
	
	/**
	 * Method that draws the mapHelper
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawMapHelper(Graphics2D g)
	{
		mapHelper.draw(g);
	}
	
	/**
	 * Method that draws the currentPalette
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawCurrentPalette(Graphics2D g)
	{
		currentPalette.draw(g);
	}
	
	/**
	 * Method that draws the VisionIcons
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawVisionIcons(Graphics2D g)
	{
		for(int i = 0; i < visionIcons.length; i++)
		{
			visionIcons[i].draw(g);
		}
	}
	
	/**
	 * Method that draws the Buttons
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawButtons(Graphics2D g)
	{
		tileLayerButton.draw(g);
		objectLayerButton.draw(g);
		itemLayerButton.draw(g);
		hitboxLayerButton.draw(g);
		
		gridButton.draw(g);
		saveButton.draw(g);
		mainButton.draw(g);
		
		//Draw certain Buttons depending on
		//which Palette is the current Palette
		if(currentPalette == tilePalette)
		{
			arrowIcon.draw(g);
			fillIcon.draw(g);
			
			grassButton.draw(g);
			pathButton.draw(g);
			wallButton.draw(g);
			waterButton.draw(g);
			allButton.draw(g);
		}
		else if(currentPalette == objectPalette)
		{
			treeButton.draw(g);
			bushButton.draw(g);
			rockButton.draw(g);
			structureButton.draw(g);
			buildingButton.draw(g);
			miscButton.draw(g);
		}
		else if(currentPalette == itemPalette)
		{
			itemButton.draw(g);
		}
		else if(currentPalette == hitboxPalette)
		{
			arrowIcon.draw(g);
			fillIcon.draw(g);
			
			hitboxButton.draw(g);
		}
	}
	
	private void highlightCurrentMode(Graphics2D g)
	{
		if(currentPalette == objectPalette || currentPalette == itemPalette)
		{
			return;
		}
		
		g.setColor(Color.RED);
		
		switch(currentDrawMode)
		{
			case ARROW:
				g.drawRect(
						arrowIcon.getX(),
						arrowIcon.getY(),
						arrowIcon.getWidth() - 1,
						arrowIcon.getHeight() - 1
				);
				break;
			case FILL:
				g.drawRect(
						fillIcon.getX(),
						fillIcon.getY(),
						fillIcon.getWidth() - 1,
						fillIcon.getHeight() - 1
				);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Method that draws the Texts
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTexts(Graphics2D g)
	{
		currentLocationText.draw(g);
		currentLayerText.draw(g);
	}
	
	/**
	 * Method that draws the Menus
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawMenus(Graphics2D g)
	{
		if(saveMenu.isVisible())
		{
			saveMenu.draw(g);
		}
	}
	
	/**
	 * Method that draws the Transitions
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTransitions(Graphics2D g)
	{
		fadeToBlack.draw(g);
	}
	
	/**
	 * Method that draws the PlayState
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		drawBackground(g);
		drawMaps(g);
		drawMapHelper(g);
		drawSideBar(g);
		drawCurrentPalette(g);
		drawVisionIcons(g);
		drawButtons(g);
		highlightCurrentMode(g);
		drawTexts(g);
		drawMenus(g);
		drawTransitions(g);
	}
}
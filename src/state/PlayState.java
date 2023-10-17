package state;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import main.GamePanel;
import manager.ImageManager;
import manager.SaveManager;
import manager.StateManager;
import manager.TransitionManager;
import entity.Camera;
import entity.Palette;
import helper.TextSize;
import entity.Button;
import entity.VisibleIcon;
import map.TileMap;
import map.MapUtility;
import transition.TransitionType;



/**
 * PlayState class represents the "play area" of the game 
 * This is where the user sees the game being played
 * 
 * @author Vachia Thoj
 *
 */
public class PlayState extends State
{	
	//To manage images
	private ImageManager imageManager;
	
	//To manage saves
	private SaveManager saveManager;
	
	//To manage Transition
	private TransitionManager transitionManager;
	
	//TileMaps
	private TileMap backgroundMap;
	private TileMap[] layers;
	private TileMap currentLayer;
	
	//Camera
	private Camera camera;
	
	//Palettes
	private Palette tilePalette;
	private Palette itemPalette;
	private Palette currentPalette;
	
	//MapUtility
	private MapUtility mapUtility;
	
	//Buttons
	private Button waterButton;
	private Button pathsButton;
	private Button grassButton;
	private Button plantsButton;
	private Button otherButton;
	private Button itemsButton;
	private Button gridButton;
	private Button saveButton;
	private Button menuButton;
	private VisibleIcon[] visibleIcons;
	private Button[] layerButtons;
	
	private StateType nextState;
	
	private String[] layerTexts;
	private String currentLayerText;
	
	private int frames;
	
	private static final int NUM_LAYERS = 4;
	
	/**
	 * Constructor
	 */
	public PlayState()
	{
		//Initialize managers
		this.imageManager = ImageManager.instance();
		this.saveManager = SaveManager.instance();
		this.transitionManager = new TransitionManager(GamePanel.WIDTH, GamePanel.HEIGHT);
		this.transitionManager.setTransition(TransitionType.FADE_TO_BLACK);
		
		//Initialize layers; "maps"
		this.layers = new TileMap[NUM_LAYERS];
		
		//Check if there are files of TileMaps
		if(saveManager.getLayer1() != null && saveManager.getLayer2() != null && 
		   saveManager.getCollisionLayer() != null && saveManager.getItemLayer() != null)
		{
			//Obtain TileMaps from saveManager
			layers[0] = saveManager.getLayer1();
			layers[1] = saveManager.getLayer2();
			layers[2] = saveManager.getCollisionLayer();
			layers[3] = saveManager.getItemLayer();
			
			//Initialize layers (TileMaps)
			for(int i = 0; i < NUM_LAYERS; i++)
			{
				layers[i].init();
			}
			
			saveManager.setLayer1(null);
			saveManager.setLayer2(null);
			saveManager.setCollisionLayer(null);
			saveManager.setItemLayer(null);
		}
		else
		{		
			//Create "blank" layers (TileMaps)
			for(int i = 0; i < NUM_LAYERS; i++)
			{
				this.layers[i] = new TileMap(0, 0, 60, 51, 32);
			}
		}
		
		//Set currentLayer to layers[0]
		this.currentLayer = layers[0];
		
		//Strings
		this.layerTexts = new String[NUM_LAYERS];
		this.layerTexts[0] = "Current Layer: Layer 1";
		this.layerTexts[1] = "Current Layer: Layer 2";
		this.layerTexts[2] = "Current Layer: Layer 3";
		this.layerTexts[3] = "Current Layer: Item Layer";
		this.currentLayerText = layerTexts[0];
		
		//Create a "background map"
		this.backgroundMap = new TileMap(0, 0, 60, 51, 32);
		
		//Create camera
		this.camera = new Camera(0, 0, 960, 480, backgroundMap);
		
		//Set camera to backgroundMap
		this.backgroundMap.setCamera(camera);
		this.backgroundMap.setImages(imageManager.getTileImages());
		
		//Set camera to the layers (TileMaps)
		//Set images to layers (TileMaps)
		for(int i = 0; i < 3; i++)
		{
			layers[i].setCamera(camera);
			layers[i].setImages(imageManager.getTileImages());
		}
		
		//layer[3] is a special layer (TileMap) meant for only items
		layers[3].setCamera(camera);
		layers[3].setImages(imageManager.getItemImages());
		
		//Initialize tilePalette
		this.tilePalette = new Palette(0, 
				  				  480, 
				  				  GamePanel.WIDTH, 
				  				  GamePanel.HEIGHT - 480,
				  				  imageManager.getTileImages());
		this.tilePalette.setRange(6, 19);
		
		//Initialize itemPalette
		this.itemPalette = new Palette(0,
								  480,
								  GamePanel.WIDTH,
								  GamePanel.HEIGHT - 480,
								  imageManager.getItemImages());
		this.itemPalette.setRange(1, 3);
		
		this.currentPalette = tilePalette;
		
		//Create MapUtility
		this.mapUtility = new MapUtility(backgroundMap, camera);
		
		this.nextState = null;
		
		this.frames = 0;
		
		createButtons();
	}
	
	/**
	 * Method that creates/initializes Button
	 */
	private void createButtons()
	{
		//Obtain button images
		BufferedImage[] buttonImages = imageManager.getSmallButtonImages();
		
		//Water Button
		waterButton = new Button(buttonImages[0], buttonImages[1]);
		waterButton.setX(0);
		waterButton.setY(tilePalette.getY());
		
		//Paths Button
		pathsButton = new Button(buttonImages[2], buttonImages[3]);
		pathsButton.setX(waterButton.getX() + waterButton.getWidth() + 1);
		pathsButton.setY(tilePalette.getY());
		
		//Grass Button
		grassButton = new Button(buttonImages[4], buttonImages[5]);
		grassButton.setX(pathsButton.getX() + pathsButton.getWidth() + 1);
		grassButton.setY(tilePalette.getY());
		
		//Plants Button
		plantsButton = new Button(buttonImages[6], buttonImages[7]);
		plantsButton.setX(grassButton.getX() + grassButton.getWidth() + 1);
		plantsButton.setY(tilePalette.getY());
		
		//Others Button
		otherButton = new Button(buttonImages[8], buttonImages[9]);
		otherButton.setX(plantsButton.getX() + plantsButton.getWidth() + 1);
		otherButton.setY(tilePalette.getY());
		
		//Items Button
		itemsButton = new Button(buttonImages[10], buttonImages[11]);
		itemsButton.setX(otherButton.getX() + otherButton.getWidth());
		itemsButton.setY(tilePalette.getY());
		
		//Grid Button
		gridButton = new Button(buttonImages[12], buttonImages[13]);
		gridButton.setX(968);
		gridButton.setY(96);
		
		//Save Button
		saveButton = new Button(buttonImages[14], buttonImages[15]);
		saveButton.setX(968);
		saveButton.setY(gridButton.getY() + gridButton.getHeight() + 8);
	
		//Menu Button
		menuButton = new Button(buttonImages[16], buttonImages[17]);
		menuButton.setX(968);
		menuButton.setY(saveButton.getY() + saveButton.getHeight() + 8);
		
		//Initialize visibleIcons
		visibleIcons = new VisibleIcon[NUM_LAYERS];
		
		visibleIcons[0] = new VisibleIcon(buttonImages[26], buttonImages[27],
				                          buttonImages[28], buttonImages[29],
				                          1048, 256);
		visibleIcons[1] = new VisibleIcon(buttonImages[26], buttonImages[27],
                						  buttonImages[28], buttonImages[29],
                						  1048, visibleIcons[0].getY() + visibleIcons[0].getHeight() + 2);
		visibleIcons[2] = new VisibleIcon(buttonImages[26], buttonImages[27],
				 						  buttonImages[28], buttonImages[29],
				 						  1048, visibleIcons[1].getY() + visibleIcons[1].getHeight() + 2);
		visibleIcons[3] = new VisibleIcon(buttonImages[26], buttonImages[27],
				  						  buttonImages[28], buttonImages[29],
				  						  1048, visibleIcons[2].getY() + visibleIcons[2].getHeight() + 2);
		
		//Initialize layerButtons
		layerButtons = new Button[NUM_LAYERS];
		
		layerButtons[0] = new Button(buttonImages[18], buttonImages[19]);
		layerButtons[0].setX(visibleIcons[0].getX() + visibleIcons[0].getWidth() + 2);
		layerButtons[0].setY(256);
		
		layerButtons[1] = new Button(buttonImages[20], buttonImages[21]);
		layerButtons[1].setX(visibleIcons[1].getX() + visibleIcons[1].getWidth() + 2);
		layerButtons[1].setY(layerButtons[0].getY() + layerButtons[0].getHeight() + 2);
		
		layerButtons[2] = new Button(buttonImages[22], buttonImages[23]);
		layerButtons[2].setX(visibleIcons[2].getX() + visibleIcons[2].getWidth() + 2);
		layerButtons[2].setY(layerButtons[1].getY() + layerButtons[1].getHeight() + 2);
		
		layerButtons[3] = new Button(buttonImages[24], buttonImages[25]);
		layerButtons[3].setX(visibleIcons[3].getX() + visibleIcons[3].getWidth() + 2);
		layerButtons[3].setY(layerButtons[2].getY() + layerButtons[2].getHeight() + 2);
	}
	
	/**
	 * Method that updates maps
	 */
	private void updateMaps()
	{
		//Update layers
		for(int i = 0; i < NUM_LAYERS; i++)
		{
			layers[i].update();
		}
		
		//If the currentPalette is the tilePalette and the currentLayer is not layers[3]
		if(currentPalette.equals(tilePalette) && currentLayer != layers[3])
		{
			//Check if a Tile within the layer (TileMap) has been clicked
			if(mapUtility.isClicked())
			{
				//Change a Tile within the layer (TileMap)
				currentLayer.changeTile(mapUtility.getChangeCol(),
										mapUtility.getChangeRow(),
										tilePalette.getCurrentImage());
				
				mapUtility.setClicked(false);
				mapUtility.setChangeCol(-1);
				mapUtility.setChangeRow(-1);
			}
		}
		else if(currentPalette.equals(itemPalette) && currentLayer == layers[3])
		{
			//Check if a Tile within the layer (TileMap) has been clicked
			if(mapUtility.isClicked())
			{
				//Change a Tile within the layer (TileMap)
				currentLayer.changeTile(mapUtility.getChangeCol(),
										mapUtility.getChangeRow(),
										itemPalette.getCurrentImage());
				mapUtility.setClicked(false);
				mapUtility.setChangeCol(-1);
				mapUtility.setChangeRow(-1);
			}
		}
		else
		{
			mapUtility.setClicked(false);
			mapUtility.setChangeCol(-1);
			mapUtility.setChangeRow(-1);
		}
		
		backgroundMap.update();
	}
	
	/**
	 * Method that updates Buttons
	 */
	private void updateButtons()
	{
		waterButton.update();
		pathsButton.update();
		grassButton.update();
		plantsButton.update();
		otherButton.update();
		itemsButton.update();
		gridButton.update();
		saveButton.update();
		menuButton.update();
		
		for(int i = 0; i < NUM_LAYERS; i++)
		{
			visibleIcons[i].update();
		}
		
		for(int i = 0; i < NUM_LAYERS; i++)
		{
			layerButtons[i].update();
		}
	}
	
	/**
	 * Method that performs an action when a Button
	 * has been clicked on
	 */
	private void buttonActions()
	{
		if(waterButton.isMouseClickingButton()) //waterButton has been clicked
		{
			waterButton.setMouseClickingButton(false);
			
			if(currentPalette.equals(tilePalette) == false)
			{
				currentPalette = tilePalette;
			}
			
			currentPalette.setRange(6, 19);
		}
		else if(pathsButton.isMouseClickingButton()) //pathsButton has been clicked
		{
			pathsButton.setMouseClickingButton(false);
			
			if(currentPalette.equals(tilePalette) == false)
			{
				currentPalette = tilePalette;
			}
			
			currentPalette.setRange(20, 32);
		}
		else if(grassButton.isMouseClickingButton()) //grassButton has been clicked
		{
			grassButton.setMouseClickingButton(false);
			
			if(currentPalette.equals(tilePalette) == false)
			{
				currentPalette = tilePalette;
			}
			
			currentPalette.setRange(33, 38);
		}
		else if(plantsButton.isMouseClickingButton()) //plantsButton has been clicked
		{
			plantsButton.setMouseClickingButton(false);
			
			if(currentPalette.equals(tilePalette) == false)
			{
				currentPalette = tilePalette;
			}
			
			currentPalette.setRange(39, 41);
			
		}
		else if(otherButton.isMouseClickingButton()) //othersButton has been clicked
		{
			otherButton.setMouseClickingButton(false);
			
			if(currentPalette.equals(tilePalette) == false)
			{
				currentPalette = tilePalette;
			}
			
			currentPalette.setRange(1, 5);
		}
		else if(itemsButton.isMouseClickingButton()) //itemsButton has been clicked
		{
			itemsButton.setMouseClickingButton(false);
			
			if(currentPalette.equals(itemPalette) == false)
			{
				currentPalette = itemPalette;
			}
			
			currentPalette.setRange(1, 3);
		}
		else if(gridButton.isMouseClickingButton()) //gridButton has been clicked
		{
			gridButton.setMouseClickingButton(false);
			
			mapUtility.setGrid((!mapUtility.isGrid()));
		}
		else if(saveButton.isMouseClickingButton())
		{
			saveButton.setMouseClickingButton(false);
			
			//Attempt to save layers (Tilemaps) to files
			saveManager.save(layers[0], layers[1], layers[2], layers[3]);
		}
		else if(menuButton.isMouseClickingButton()) //menuButton has been clicked
		{
			menuButton.setMouseClickingButton(false);
			
			//Indicate the next State to go to; MenuState
			nextState = StateType.MENU_STATE;
			
			//Start transition
			transitionManager.startTransition();
		}
		
		//Check if a layer button has been clicked
		for(int i = 0; i < 4; i++)
		{
			if(layerButtons[i].isMouseClickingButton())
			{
				layerButtons[i].setMouseClickingButton(false);
				
				//Change the current layer to the indicated layer
				currentLayer = layers[i];
				currentLayerText = layerTexts[i];
				break;
			}
		}
	}
	
	/**
	 * Method that updates the PlayState
	 */
	public void update()
	{
		if(transitionManager.isDone())
		{
			StateManager.instance().nextState(nextState);
			return;
		}
		
		if(transitionManager.isRunning())
		{
			transitionManager.update();
			return;
		}
		
		//Update maps
		updateMaps();
		
		mapUtility.update();
		
		//Update Camera
		camera.update();
		
		//Update currentPalette
		currentPalette.update();
		
		//Update Buttons
		updateButtons();
		
		//Perform Button action if necessary
		buttonActions();
	}
	
	/**
	 * Method that draws the background of the PlayState
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawBackground(Graphics2D g)
	{
		backgroundMap.draw(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
	/**
	 * Method that draws the TileMaps
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawTileMaps(Graphics2D g)
	{
		for(int i = 0; i < NUM_LAYERS; i++)
		{
			if(visibleIcons[i].isEyeOn())
			{
				layers[i].draw(g);
			}
		}
	}
	
	/**
	 * Method that draws the Buttons
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawButtons(Graphics2D g)
	{
		waterButton.draw(g);
		pathsButton.draw(g);
		grassButton.draw(g);
		plantsButton.draw(g);
		otherButton.draw(g);
		itemsButton.draw(g);
		gridButton.draw(g);
		saveButton.draw(g);
		menuButton.draw(g);
		
		for(int i = 0; i < NUM_LAYERS; i++)
		{
			visibleIcons[i].draw(g);
		}
		
		for(int i = 0; i < NUM_LAYERS; i++)
		{
			layerButtons[i].draw(g);
		}
	}
	
	/**
	 * Method that draws Strings
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawStrings(Graphics2D g)
	{
		if(saveManager.isSaved())
		{
			g.setColor(Color.RED);
			g.setFont(new Font("Courier New", Font.BOLD, 24));
			int textWidth = TextSize.getTextWidth("SAVE COMPLETED", g);
			g.drawString("SAVE COMPLETED", ((GamePanel.WIDTH + 960) / 2) - (textWidth / 2), 64);
			++frames;
		}
		
		if(frames >= 90)
		{
			saveManager.setSaved(false);
			frames = 0;
		}
		
		//CurrentLayer text
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, 18));
		int textWidth = TextSize.getTextWidth(currentLayerText, g);
		g.drawString(currentLayerText, ((GamePanel.WIDTH + 960) / 2) - (textWidth / 2), 448);
	}
	
	/**
	 * Method that draws the transition
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawTransition(Graphics2D g)
	{
		if(transitionManager.isRunning())
		{
			transitionManager.draw(g);
		}
	}
	
	/**
	 * Method that draws everything in MenuState
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		//Draw background
		drawBackground(g);
		
		//Draw tileMap
		drawTileMaps(g);
		
		//Draw mapUtility
		mapUtility.draw(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(960, 0, GamePanel.WIDTH - 960, 480);
		
		//Draw Palette
		currentPalette.draw(g);
		
		//Draw Buttons
		drawButtons(g);
		
		//Draw Strings
		drawStrings(g);
		
		if(transitionManager.isDone())
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
		
		//Draw Transition
		drawTransition(g);
	}
}
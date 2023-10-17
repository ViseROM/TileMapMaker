package state;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import manager.ImageManager;
import manager.TransitionManager;
import manager.StateManager;
import manager.SaveManager;
import main.GamePanel;
import helper.TextSize;
import entity.Button;
import transition.*;


/**
 * MenuState class represents the main menu
 * This is where the user sees the main menu
 * 
 * @author Vachia Thoj
 *
 */
public class MenuState extends State
{
	//To manage images
	private ImageManager imageManager;
	
	//To manage saves
	private SaveManager saveManager;
	
	//To manage Transitions
	private TransitionManager transitionManager;
	
	//Strings
	private String title;
	private String author;
	private String version;
	
	//Buttons
	private Button newButton;
	private Button loadButton;
	private Button controlsButton;
	private Button exitButton;
	
	//Next State to go to
	private StateType nextState;
	
	/**
	 * Constructor
	 */
	public MenuState()
	{
		//Managers
		this.imageManager = ImageManager.instance();
		this.saveManager = SaveManager.instance();
		
		transitionManager = new TransitionManager(GamePanel.WIDTH, GamePanel.HEIGHT);
		transitionManager.setTransition(TransitionType.FADE_TO_BLACK);
		
		//Strings
		this.title = "Tile Map Maker";
		this.author = "Vachia Thoj (Vise)";
		this.version = "Ver. 0.1";
		
		//Create Buttons
		createButtons();
		
		this.nextState = null;
	}
	
	/**
	 * Method that creates/initializes Buttons
	 */
	private void createButtons()
	{
		BufferedImage[] buttonImages = imageManager.getBigButtonImages();
		
		//New Button
		newButton = new Button(buttonImages[0], buttonImages[1]);
		newButton.setX((GamePanel.WIDTH / 2) - (newButton.getWidth() / 2));
		newButton.setY(GamePanel.HEIGHT / 3);
		
		//Load Button
		loadButton = new Button(buttonImages[2], buttonImages[3]);
		loadButton.setX((GamePanel.WIDTH / 2) - (loadButton.getWidth() / 2));
		loadButton.setY(newButton.getY() + newButton.getHeight() + 8);
		
		//Controls Button
		controlsButton = new Button(buttonImages[4], buttonImages[5]);
		controlsButton.setX((GamePanel.WIDTH / 2) - (controlsButton.getWidth() / 2));
		controlsButton.setY(loadButton.getY() + loadButton.getHeight() + 8);
		
		//Exit Button
		exitButton = new Button(buttonImages[6], buttonImages[7]);
		exitButton.setX(GamePanel.WIDTH / 2 - exitButton.getWidth() / 2);
		exitButton.setY(controlsButton.getY() + controlsButton.getHeight() + 8);
	}
	
	/**
	 * Method that updates the Buttons
	 */
	private void updateButtons()
	{
		//Update Buttons
		newButton.update();
		loadButton.update();
		controlsButton.update();
		exitButton.update();
	}
	
	/**
	 * Method that disable Buttons
	 */
	private void disableButtons()
	{
		//Disable Buttons
		newButton.setDisabled(true);
		loadButton.setDisabled(true);
		controlsButton.setDisabled(true);
		exitButton.setDisabled(true);
	}
	
	/**
	 * Method that performs an action when a Button has been clicked on
	 */
	private void buttonActions()
	{
		if(newButton.isMouseClickingButton()) //If newButton has been clicked
		{
			newButton.setMouseClickingButton(false);
			
			//Start transition
			transitionManager.startTransition();
			
			disableButtons();
			
			//Indicate the next State to go to; PlayState
			nextState = StateType.PLAY_STATE;
		}
		else if(loadButton.isMouseClickingButton()) //If loadButton has been clicked
		{
			loadButton.setMouseClickingButton(false);
			
			//Attempt to load files
			saveManager.load();
			
			//Check if loaded files successfully
			if(saveManager.getLayer1() != null && saveManager.getLayer2() != null && saveManager.getCollisionLayer() != null)
			{
				//Start transition
				transitionManager.startTransition();
				disableButtons();
				
				//Indicate the next State to go to; PlayState
				nextState = StateType.PLAY_STATE;
			}
		}
		else if(controlsButton.isMouseClickingButton()) //If controlsButton has been clicked
		{
			controlsButton.setMouseClickingButton(false);
			disableButtons();
			
			//Start transition
			transitionManager.startTransition();
			
			//Indicate the next State to go to; ControlsState
			nextState = StateType.CONTROLS_STATE;
		}
		else if(exitButton.isMouseClickingButton()) //If exitButon has been clicked
		{
			exitButton.setMouseClickingButton(false);
			
			//Exit program
			System.exit(0);
		}
	}
	
	/**
	 * Method that updates the Menu State
	 */
	public void update()
	{
		//Update Buttons
		updateButtons();
		
		if(transitionManager.isDone())
		{
			if(nextState != null)
			{
				//Go to next State
				StateManager.instance().nextState(nextState);
				return;
			}
		}
		
		//Update Transition if Transition is running
		if(transitionManager.isRunning())
		{
			transitionManager.update();
			return;
		}
		
		//Perform Button action if necessary
		buttonActions();
	}
	
	/**
	 * Method that draws the background
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawBackground(Graphics2D g)
	{
		//Draw Background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
	/**
	 * Method that draws the Strings
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawStrings(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		
		//Draw title
		g.setFont(new Font("Courier New", Font.BOLD, 32));
		int titleWidth = TextSize.getTextWidth(title, g);
		g.drawString(title, (GamePanel.WIDTH / 2) - (titleWidth / 2), 48);
		
		//Draw author
		g.setFont(new Font("Courier New", Font.BOLD, 16));
		g.drawString(author, 4, GamePanel.HEIGHT - 4);
		
		//Draw version
		int versionWidth = TextSize.getTextWidth(version, g);
		g.drawString(version, (GamePanel.WIDTH - (versionWidth + 4)), GamePanel.HEIGHT - 4);
	}
	
	/**
	 * Method that draws the Buttons
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawButtons(Graphics2D g)
	{
		//Draw Buttons
		newButton.draw(g);
		loadButton.draw(g);
		controlsButton.draw(g);
		exitButton.draw(g);
	}
	
	/**
	 * Method that draws the Transition animation
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
		
		//Draw Strings
		drawStrings(g);
		
		//Draw Buttons
		drawButtons(g);
		
		if(transitionManager.isDone())
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
		
		//Draw Transition
		drawTransition(g);
	}
}

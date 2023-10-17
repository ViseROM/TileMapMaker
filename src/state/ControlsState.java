package state;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;

import manager.ImageManager;
import manager.StateManager;
import manager.TransitionManager;
import main.GamePanel;
import transition.TransitionType;
import entity.Button;
import helper.TextSize;

/**
 * Controls class explains to the user the control scheme for the program
 * @author Vachia Thoj
 *
 */
public class ControlsState extends State
{
	//To manage images
	private ImageManager imageManager;
	
	//To manager Transitions
	private TransitionManager transitionManager;
	
	//Buttons
	private Button menuButton;
	
	//Tutorial images
	private BufferedImage[] tutorialImages;
	
	//Strings
	private String title;
	private String mouse;
	private String arrows;
	private String shift;
	
	//Next State to go to
	private StateType nextState;
	
	/**
	 * Constructor
	 */
	public ControlsState()
	{
		//Managers
		this.imageManager = ImageManager.instance();
		this.transitionManager = new TransitionManager(GamePanel.WIDTH, GamePanel.HEIGHT);
		this.transitionManager.setTransition(TransitionType.FADE_TO_BLACK);
		
		//Strings
		this.title = "CONTROLS";
		this.mouse = "Mouse Click: click on map to place a tile";
		this.arrows = "Arrow Keys: press arrows keys to move the map";
		this.shift = "Shift + Mouse: hold SHIFT and move mouse for fast tile placement";
		
		//Create Buttons
		createButtons();
		
		//Obtain tutorial images
		this.tutorialImages = imageManager.getControlImages();
		
		this.nextState = null;
	}
	
	/**
	 * Method that initializes/creates Buttons
	 */
	private void createButtons()
	{
		//Obtain button images
		BufferedImage[] buttonImages = imageManager.getBigButtonImages();
		
		//Menu Button
		menuButton = new Button(buttonImages[8], buttonImages[9]);
		menuButton.setX((GamePanel.WIDTH / 2) - (menuButton.getWidth() / 2));
		menuButton.setY(GamePanel.HEIGHT - (menuButton.getHeight() * 2));
	}
	
	/**
	 * Method that updates Buttons
	 */
	private void updateButtons()
	{
		menuButton.update();
	}
	
	/**
	 * Method that disable Buttons
	 */
	private void disableButtons()
	{
		menuButton.setDisabled(true);
	}
	
	/**
	 * Method that performs an action when a Button has been clicked on
	 */
	private void buttonActions()
	{
		if(menuButton.isMouseClickingButton()) //If menuButton has been clicked
		{
			menuButton.setMouseClickingButton(false);
			
			disableButtons();
			
			//Start transition
			transitionManager.startTransition();
			
			//Indicate the next State to go to; MenuState
			nextState = StateType.MENU_STATE;
		}
	}
	
	/**
	 * Method that updates the ControlsState
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
		
		//Perform Button actions if necessary
		buttonActions();
	}
	
	/**
	 * Method that draws the background
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawBackground(Graphics2D g)
	{
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
		
		//Draw Strings
		g.setFont(new Font("Courier New", Font.BOLD, 20));
		g.drawString(mouse, 400, 120);
		g.drawString(arrows, 400, 278);
		g.drawString(shift, 400, 436);
	}
	
	/**
	 * Method that draws the Tutorial images
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawTutorials(Graphics2D g)
	{
		//Draw tutorial images
		g.drawImage(tutorialImages[1], 32, 96, null);
		g.drawImage(tutorialImages[2], 32, 254, null);
		g.drawImage(tutorialImages[3], 32, 412, null);
	}
	
	/**
	 * Method that draws the Buttons
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawButtons(Graphics2D g)
	{
		menuButton.draw(g);
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
	 * Method that draws the ControlsState
	 * @param g (graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		//Draw background
		drawBackground(g);
		
		//Draw Strings
		drawStrings(g);
		
		//Draw tutorial images
		drawTutorials(g);
		
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

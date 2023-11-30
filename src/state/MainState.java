package state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import button.ImageButton;
import entity.Text;
import main.GamePanel;
import manager.ImageManager;
import manager.MouseManager;
import manager.SaveManager;
import manager.StateManager;
import transition.*;

/**
 * MainState represents the main menu
 * This is what you will first see when you
 * start up the program
 * @author Vachia Thoj
 *
 */
public class MainState extends State
{
	//Managers
	private ImageManager imageManager;
	private SaveManager saveManager;
	
	//The next state to go to
	private StateType nextState;
	
	//Texts
	private Text title;
	private Text author;
	
	//Buttons
	private ImageButton newMapButton;
	private ImageButton loadMapButton;
	private ImageButton controlsButton;
	private ImageButton quitButton;
	
	//Transition
	private FadeToBlack fadeToBlack;
	
	/**
	 * Constructor
	 */
	public MainState()
	{
		//Obtain managers
		this.imageManager = ImageManager.instance();
		this.saveManager = SaveManager.instance();
		
		this.nextState = null;
		
		//Initialize and set various objects and variables
		createTexts();
		createButtons();
		createTransitions();
	}
////////////////////////////////////////////// CREATE METHODS //////////////////////////////////////////////
	private void createTexts()
	{
		this.title = new Text("TILE MAP MAKER");
		this.title.changeScale(3);
		this.title.changeColor(new Color(0, 125, 179));
		this.title.setX((GamePanel.WIDTH / 2) - (title.getWidth() / 2));
		this.title.setY(title.getHeight());
		
		this.author = new Text("VISE");
		this.author.setX(2);
		this.author.setY(GamePanel.HEIGHT - (author.getHeight() + 2));
	}
	
	private void createButtons()
	{
		BufferedImage[] buttonImages = imageManager.getButtonImages();
		
		newMapButton = new ImageButton(
				(GamePanel.WIDTH / 2) - (buttonImages[0].getWidth() / 2),
				(GamePanel.HEIGHT / 2) - (buttonImages[0].getHeight() * 2),
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"NEW MAP"
		);
		
		loadMapButton = new ImageButton(
				(GamePanel.WIDTH / 2) - (buttonImages[0].getWidth() / 2),
				newMapButton.getY() + newMapButton.getHeight() + 4,
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"LOAD MAP"
			);
		
		controlsButton = new ImageButton(
				(GamePanel.WIDTH / 2) - (buttonImages[0].getWidth() / 2),
				loadMapButton.getY() + loadMapButton.getHeight() + 4,
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"CONTROLS"
			);
		
		quitButton = new ImageButton(
				(GamePanel.WIDTH / 2) - (buttonImages[0].getWidth() / 2),
				controlsButton.getY() + controlsButton.getHeight() + 4,
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"QUIT"
		);
	}
	
	private void createTransitions()
	{
		this.fadeToBlack = new FadeToBlack(GamePanel.WIDTH, GamePanel.HEIGHT);
	}

////////////////////////////////////////////// UPDATE METHODS //////////////////////////////////////////////
	
	/**
	 * Method that updates the Buttons
	 */
	private void updateButtons()
	{
		newMapButton.update();
		loadMapButton.update();
		controlsButton.update();
		quitButton.update();
		
		//Perform an action if a Button has been clicked on
		performButtonAction();
	}
	
	/**
	 * Method that performs an action if a Button has been clicked on
	 */
	private void performButtonAction()
	{
		if(newMapButton.isMouseClickingButton())
		{
			newMapButton.setMouseClickingButton(false);
			
			//Create a "blank" new save file
			saveManager.setCurrentSaveFile(null);
			
			//Run the fadeToBlack transition
			fadeToBlack.setRunning(true);
			
			//Indicate that the next state to go to is the PlayState
			nextState = StateType.PLAY;
		}
		else if(loadMapButton.isMouseClickingButton())
		{
			loadMapButton.setMouseClickingButton(false);
			
			//Run the fadeToBlack transition
			fadeToBlack.setRunning(true);
			
			//Indicate that the next state to go to is the SaveState
			nextState = StateType.SAVE;
		}
		else if(controlsButton.isMouseClickingButton())
		{
			controlsButton.setMouseClickingButton(false);
			
			//Run the fadeToBlack transition
			fadeToBlack.setRunning(true);
			
			//Indicate that the next state to go to is the ControlsState
			nextState = StateType.CONTROLS;
		}
		else if(quitButton.isMouseClickingButton())
		{
			quitButton.setMouseClickingButton(false);
			
			//Exit the program
			System.exit(0);
		}
	}
	
	/**
	 * Method that updates the Transitions
	 */
	private void updateTransition()
	{
		fadeToBlack.update();
	}
	
	/**
	 * Method that checks if a change of state is necessary
	 */
	private void changeState()
	{
		if(fadeToBlack.isDone() && nextState != null)
		{
			MouseManager.instance().clearPressedPoint();
			MouseManager.instance().clearReleasedPoint();
			
			//Go to the next state
			StateManager.instance().nextState(nextState);
		}
	}
	
	/**
	 * Method that updates the MainState
	 */
	public void update()
	{
		updateTransition();
		
		if(fadeToBlack.isRunning())
		{
			return;
		}
		
		changeState();
		updateButtons();
	}
	
////////////////////////////////////////////// DRAW METHODS //////////////////////////////////////////////
	
	/**
	 * Method that draws the background
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawBackground(Graphics2D g)
	{
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
	/**
	 * Method that draws the title text
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTitleText(Graphics2D g)
	{
		title.draw(g);
		g.setColor(new Color(0, 97, 138));
		g.fillRect(title.getX() - 6, title.getY() + title.getHeight() + 4, title.getWidth() + 12, 4);
		g.fillRect(title.getX() - 14, title.getY() + title.getHeight() + 2, 8, 8);
		g.fillRect(title.getX() + title.getWidth() + 6, title.getY() + title.getHeight() + 2, 8, 8);
	}
	
	/**
	 * Method that draws the author text
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawAuthorText(Graphics2D g)
	{
		author.draw(g);
	}
	
	/**
	 * Method that draws the Buttons
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawButtons(Graphics2D g)
	{
		newMapButton.draw(g);
		loadMapButton.draw(g);
		controlsButton.draw(g);
		quitButton.draw(g);
	}
	
	/**
	 * Method that draws the Transitions
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTransition(Graphics2D g)
	{
		fadeToBlack.draw(g);
	}
	
	/**
	 * Method that draws the MainState
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		drawBackground(g);
		drawTitleText(g);
		drawAuthorText(g);
		drawButtons(g);
		drawTransition(g);
	}
}